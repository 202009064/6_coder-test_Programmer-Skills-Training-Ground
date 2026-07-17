package com.yupi.codertestbackend.controller;

import com.yupi.codertestbackend.common.BaseResponse;
import com.yupi.codertestbackend.common.ErrorCode;
import com.yupi.codertestbackend.common.ResultUtils;
import com.yupi.codertestbackend.model.dto.user.UserLoginRequest;
import com.yupi.codertestbackend.model.dto.user.UserRegisterRequest;
import com.yupi.codertestbackend.model.vo.UserVO;
import com.yupi.codertestbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户 Controller
 */
@Slf4j
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
        try {
            UserVO userVO = userService.register(request);
            return ResultUtils.success(userVO);
        } catch (Exception e) {
            log.error("注册失败, username={}", request != null ? request.getUsername() : null, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public BaseResponse<UserVO> login(@RequestBody UserLoginRequest request, HttpSession session) {
        try {
            UserVO userVO = userService.login(request);
            session.setAttribute("userId", userVO.getId());
            return ResultUtils.success(userVO);
        } catch (Exception e) {
            log.error("登录失败, username={}", request != null ? request.getUsername() : null, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 注销
     */
    @PostMapping("/logout")
    public BaseResponse<Void> logout(HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                return new BaseResponse<>(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMessage());
            }
            session.removeAttribute("userId");
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("注销失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/current")
    public BaseResponse<UserVO> current(HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                return new BaseResponse<>(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMessage());
            }
            UserVO userVO = userService.getCurrentUser(userId);
            return ResultUtils.success(userVO);
        } catch (Exception e) {
            log.error("获取当前用户信息失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}