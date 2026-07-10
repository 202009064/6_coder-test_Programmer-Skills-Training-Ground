package com.yupi.codertestbackend.controller;

import com.yupi.codertestbackend.common.BaseResponse;
import com.yupi.codertestbackend.common.ResultUtils;
import com.yupi.codertestbackend.model.dto.user.UserLoginRequest;
import com.yupi.codertestbackend.model.dto.user.UserRegisterRequest;
import com.yupi.codertestbackend.model.vo.UserVO;
import com.yupi.codertestbackend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 用户 Controller
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注册
     */
    @PostMapping("/register")
    public BaseResponse<UserVO> register(@RequestBody UserRegisterRequest request) {
        UserVO userVO = userService.register(request);
        return ResultUtils.success(userVO);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public BaseResponse<UserVO> login(@RequestBody UserLoginRequest request) {
        UserVO userVO = userService.login(request);
        return ResultUtils.success(userVO);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    public BaseResponse<UserVO> current(@RequestParam Long userId) {
        UserVO userVO = userService.getCurrentUser(userId);
        return ResultUtils.success(userVO);
    }
}
