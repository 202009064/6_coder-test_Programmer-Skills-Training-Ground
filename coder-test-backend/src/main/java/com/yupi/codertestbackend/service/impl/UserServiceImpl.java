package com.yupi.codertestbackend.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.yupi.codertestbackend.common.ErrorCode;
import com.yupi.codertestbackend.mapper.UserMapper;
import com.yupi.codertestbackend.model.dto.user.UserLoginRequest;
import com.yupi.codertestbackend.model.dto.user.UserRegisterRequest;
import com.yupi.codertestbackend.model.entity.User;
import com.yupi.codertestbackend.model.vo.UserVO;
import com.yupi.codertestbackend.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户 Service 实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 默认头像 URL 模板（根据用户 ID 取模固定分配）
     */
    private static final String[] DEFAULT_AVATARS = {
        "https://api.dicebear.com/9.x/adventurer/svg?seed=warrior",
        "https://api.dicebear.com/9.x/adventurer/svg?seed=knight",
        "https://api.dicebear.com/9.x/adventurer/svg?seed=archer",
        "https://api.dicebear.com/9.x/adventurer/svg?seed=mage"
    };

    @Override
    public UserVO register(UserRegisterRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String checkPassword = request.getCheckPassword();

        // 参数校验
        if (username == null || username.isBlank()) {
            throw new RuntimeException(ErrorCode.PARAMS_ERROR.getMessage());
        }
        if (password == null || password.length() < 6) {
            throw new RuntimeException("密码长度不能少于 6 位");
        }
        if (checkPassword == null || !checkPassword.equals(password)) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        // 检查账号是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new RuntimeException(ErrorCode.USER_EXIST.getMessage());
        }

        // 加密密码
        String encryptedPassword = DigestUtil.md5Hex(password);

        // 保存用户（先保存以获取自增 ID）
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setNickname(request.getNickname() != null ? request.getNickname() : username);
        this.save(user);

        // 根据用户 ID 固定分配头像（4 选 1），确保同一用户每次查询头像一致
        user.setAvatar(DEFAULT_AVATARS[(int) (user.getId() % DEFAULT_AVATARS.length)]);
        this.updateById(user);

        return toUserVO(user);
    }

    @Override
    public UserVO login(UserLoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        // 参数校验
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new RuntimeException(ErrorCode.PARAMS_ERROR.getMessage());
        }

        // 根据用户名查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new RuntimeException(ErrorCode.USER_NOT_FOUND.getMessage());
        }

        // 校验密码
        String encryptedPassword = DigestUtil.md5Hex(password);
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new RuntimeException(ErrorCode.PASSWORD_ERROR.getMessage());
        }

        return toUserVO(user);
    }

    @Override
    public UserVO getCurrentUser(Long userId) {
        if (userId == null) {
            throw new RuntimeException(ErrorCode.PARAMS_ERROR.getMessage());
        }
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException(ErrorCode.USER_NOT_FOUND.getMessage());
        }
        return toUserVO(user);
    }

    /**
     * 实体转视图对象（脱敏）
     */
    private UserVO toUserVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        // 头像为空时（老用户），根据 ID 固定分配默认头像
        String avatar = user.getAvatar();
        if (avatar == null || avatar.isBlank()) {
            avatar = DEFAULT_AVATARS[(int) (user.getId() % DEFAULT_AVATARS.length)];
        }
        vo.setAvatar(avatar);
        vo.setSalary(user.getSalary());
        vo.setUserRole(user.getUserRole() != null ? user.getUserRole() : "user");
        vo.setCreateTime(user.getCreateTime());
        return vo;
    }
}
