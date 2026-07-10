package com.yupi.codertestbackend.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.yupi.codertestbackend.common.ErrorCode;
import com.yupi.codertestbackend.mapper.UserLevelMapper;
import com.yupi.codertestbackend.mapper.UserMapper;
import com.yupi.codertestbackend.mapper.LevelMapper;
import com.yupi.codertestbackend.model.dto.level.UserLevelSubmitRequest;
import com.yupi.codertestbackend.model.entity.Level;
import com.yupi.codertestbackend.model.entity.User;
import com.yupi.codertestbackend.model.entity.UserLevel;
import com.yupi.codertestbackend.service.UserLevelService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 用户关卡 Service 实现
 */
@Service
public class UserLevelServiceImpl extends ServiceImpl<UserLevelMapper, UserLevel> implements UserLevelService {

    @Resource
    private LevelMapper levelMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public UserLevel submitLevel(UserLevelSubmitRequest request, Long userId) {
        // 参数校验
        if (request.getLevelId() == null || request.getUserOptions() == null) {
            throw new RuntimeException(ErrorCode.PARAMS_ERROR.getMessage());
        }
        if (userId == null) {
            throw new RuntimeException(ErrorCode.NOT_LOGIN.getMessage());
        }

        // 查询关卡是否存在
        Level level = levelMapper.selectById(request.getLevelId());
        if (level == null) {
            throw new RuntimeException(ErrorCode.LEVEL_NOT_FOUND.getMessage());
        }

        // 查询用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException(ErrorCode.USER_NOT_FOUND.getMessage());
        }

        // ========== 以下逻辑由 AI 计算得出，当前为占位实现 ==========
        // todo: 调用 AI，根据关卡内容、用户选项、正确选项、用户当前薪资生成结果报告

        // todo: 以下为 AI 生成结果的占位变量，后续替换为 AI 的实际返回值
        int score = 60;
        String comment = "todo: AI 生成评价";
        int salaryChange = 500;
        String suggest = "todo: AI 生成公司投递建议";
        String reason = "todo: AI 生成评分原因";
        String standardAnswer = "todo: AI 生成标准答案解析";

        // 保存用户闯关记录
        UserLevel userLevel = new UserLevel();
        userLevel.setUserId(userId);
        userLevel.setLevelId(request.getLevelId());
        userLevel.setUserOptions(request.getUserOptions());
        userLevel.setScore(score);
        userLevel.setComment(comment);
        userLevel.setSalaryChange(salaryChange);
        userLevel.setSuggest(suggest);
        userLevel.setReason(reason);
        userLevel.setTrueOptions(level.getTrueOptions());
        userLevel.setStandardAnswer(standardAnswer);
        this.save(userLevel);

        // 更新用户薪资
        user.setSalary(user.getSalary() + salaryChange);
        userMapper.updateById(user);

        return userLevel;
    }
}
