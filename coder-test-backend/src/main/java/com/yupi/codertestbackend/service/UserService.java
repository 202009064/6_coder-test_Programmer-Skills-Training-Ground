package com.yupi.codertestbackend.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.yupi.codertestbackend.model.dto.user.UserLoginRequest;
import com.yupi.codertestbackend.model.dto.user.UserRegisterRequest;
import com.yupi.codertestbackend.model.entity.User;
import com.yupi.codertestbackend.model.vo.UserVO;

import java.util.List;

/**
 * 用户 Service
 */
public interface UserService extends IService<User> {

    /**
     * 注册
     */
    UserVO register(UserRegisterRequest request);

    /**
     * 登录
     */
    UserVO login(UserLoginRequest request);

    /**
     * 获取当前用户信息
     */
    UserVO getCurrentUser(Long userId);

    /**
     * 获取薪资排行榜前 N 名
     */
    List<UserVO> listTopUsers(Integer limit);
}
