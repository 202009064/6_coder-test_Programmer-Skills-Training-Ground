package com.yupi.codertestbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.codertestbackend.common.BaseResponse;
import com.yupi.codertestbackend.common.ErrorCode;
import com.yupi.codertestbackend.common.ResultUtils;
import com.yupi.codertestbackend.model.dto.level.UserLevelSubmitRequest;
import com.yupi.codertestbackend.model.entity.UserLevel;
import com.yupi.codertestbackend.service.UserLevelService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户关卡 Controller
 */
@Slf4j
@RestController
@RequestMapping("/user-level")
public class UserLevelController {

    @Resource
    private UserLevelService userLevelService;

    /**
     * 提交关卡答案（AI 评估，需登录）
     */
    @PostMapping("/submit")
    public BaseResponse<UserLevel> submit(@RequestBody UserLevelSubmitRequest request,
                                           HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                throw new RuntimeException(ErrorCode.NOT_LOGIN.getMessage());
            }
            UserLevel userLevel = userLevelService.submitLevel(request, userId);
            if (userLevel == null) {
                throw new RuntimeException(ErrorCode.PARAMS_ERROR.getMessage());
            }
            return ResultUtils.success(userLevel);
        } catch (Exception e) {
            log.error("提交关卡答案失败, request={}", request, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 根据 URL 里的 ID，查一条答题记录的详情
     */
    @GetMapping("/get")
    public BaseResponse<UserLevel> get(@RequestParam Long id) {
        try {
            UserLevel userLevel = userLevelService.getById(id);
            if (userLevel == null) {
                throw new RuntimeException("闯关记录不存在");
            }
            return ResultUtils.success(userLevel);
        } catch (Exception e) {
            log.error("查询闯关记录失败, id={}", id, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 分页查询当前登录用户的闯关记录（不传参数时默认查第 1 页、每页 10 条）
     */
    @GetMapping("/list")
    public BaseResponse<Page<UserLevel>> list(@RequestParam(defaultValue = "1") long current,
                                              @RequestParam(defaultValue = "10") long size,
                                              HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                throw new RuntimeException(ErrorCode.NOT_LOGIN.getMessage());
            }
            LambdaQueryWrapper<UserLevel> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserLevel::getUserId, userId);
            queryWrapper.orderByDesc(UserLevel::getCreateTime);
            Page<UserLevel> page = userLevelService.page(new Page<>(current, size), queryWrapper);
            return ResultUtils.success(page);
        } catch (Exception e) {
            log.error("分页查询闯关记录失败, current={}, size={}", current, size, e);
            throw new RuntimeException(e.getMessage());
        }
    }
}