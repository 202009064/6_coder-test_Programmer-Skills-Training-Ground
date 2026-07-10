package com.yupi.codertestbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.codertestbackend.common.BaseResponse;
import com.yupi.codertestbackend.common.ResultUtils;
import com.yupi.codertestbackend.model.dto.level.UserLevelSubmitRequest;
import com.yupi.codertestbackend.model.entity.UserLevel;
import com.yupi.codertestbackend.service.UserLevelService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 用户关卡 Controller
 */
@RestController
@RequestMapping("/user-level")
public class UserLevelController {

    @Resource
    private UserLevelService userLevelService;

    /**
     * 提交关卡答案
     */
    @PostMapping("/submit")
    public BaseResponse<UserLevel> submit(@RequestBody UserLevelSubmitRequest request,
                                          @RequestParam Long userId) {
        UserLevel userLevel = userLevelService.submitLevel(request, userId);
        return ResultUtils.success(userLevel);
    }

    /**
     * 根据 ID 查询闯关记录
     */
    @GetMapping("/get")
    public BaseResponse<UserLevel> get(@RequestParam Long id) {
        UserLevel userLevel = userLevelService.getById(id);
        return ResultUtils.success(userLevel);
    }

    /**
     * 分页查询闯关记录（按用户）
     */
    @GetMapping("/list")
    public BaseResponse<Page<UserLevel>> list(@RequestParam(defaultValue = "1") long current,
                                              @RequestParam(defaultValue = "10") long size,
                                              @RequestParam(required = false) Long userId) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserLevel> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        if (userId != null) {
            queryWrapper.eq(UserLevel::getUserId, userId);
        }
        queryWrapper.orderByDesc(UserLevel::getCreateTime);
        Page<UserLevel> page = userLevelService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(page);
    }
}
