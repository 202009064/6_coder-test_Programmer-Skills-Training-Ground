package com.yupi.codertestbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.codertestbackend.common.BaseResponse;
import com.yupi.codertestbackend.common.ErrorCode;
import com.yupi.codertestbackend.common.ResultUtils;
import com.yupi.codertestbackend.model.entity.Level;
import com.yupi.codertestbackend.service.LevelService;
import com.yupi.codertestbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 关卡 Controller
 */
@Slf4j
@RestController
@RequestMapping("/level")
public class LevelController {

    @Resource
    private LevelService levelService;

    @Resource
    private UserService userService;

    /**
     * 检查当前用户是否为管理员
     */
    private void checkAdmin(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException(ErrorCode.NOT_LOGIN.getMessage());
        }
        var userVO = userService.getCurrentUser(userId);
        if (!"admin".equals(userVO.getUserRole())) {
            throw new RuntimeException(ErrorCode.FORBIDDEN.getMessage());
        }
    }

    /**
     * AI 生成关卡（根据用户薪资动态调整难度）
     */
    @PostMapping("/generate")
    public BaseResponse<Level> generate(@RequestBody SalaryRequest request) {
        try {
            if (request == null || request.getSalary() == null) {
                throw new RuntimeException(ErrorCode.PARAMS_ERROR.getMessage());
            }
            Level level = levelService.generateLevel(request.getSalary(), request.getDirection());
            return ResultUtils.success(level);
        } catch (Exception e) {
            log.error("AI 生成关卡失败, salary={}", request != null ? request.getSalary() : null, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 根据 ID 查询关卡
     */
    @GetMapping("/detail")
    public BaseResponse<Level> detail(@RequestParam Long id) {
        try {
            Level level = levelService.getById(id);
            return ResultUtils.success(level);
        } catch (Exception e) {
            log.error("查询关卡失败, id={}", id, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 分页获取精选关卡（priority >= 999，供普通用户选择挑战）
     */
    @GetMapping("/featured")
    public BaseResponse<Page<Level>> featured(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<Level> page = levelService.listFeaturedLevels(current, size);
            return ResultUtils.success(page);
        } catch (Exception e) {
            log.error("查询精选关卡列表失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    // ==================== 管理员接口 ====================

    /**
     * 管理员分页查询所有关卡
     */
    @GetMapping("/list")
    public BaseResponse<Page<Level>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Integer priority,
            HttpSession session) {
        try {
            checkAdmin(session);
            Page<Level> page = levelService.listLevels(current, size, keyword, difficulty, priority);
            return ResultUtils.success(page);
        } catch (Exception e) {
            log.error("管理员查询关卡列表失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 管理员更新关卡
     */
    @PutMapping("/update")
    public BaseResponse<Level> update(@RequestBody Level level, HttpSession session) {
        try {
            checkAdmin(session);
            Level updated = levelService.updateLevel(level);
            return ResultUtils.success(updated);
        } catch (Exception e) {
            log.error("管理员更新关卡失败, levelId={}", level != null ? level.getId() : null, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 管理员删除关卡
     */
    @DeleteMapping("/delete")
    public BaseResponse<Void> delete(@RequestParam Long id, HttpSession session) {
        try {
            checkAdmin(session);
            levelService.deleteLevel(id);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("管理员删除关卡失败, levelId={}", id, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 管理员设置关卡优先级（精选=999 / 置顶=9999 / 普通=0）
     */
    @PutMapping("/priority")
    public BaseResponse<Level> setPriority(
            @RequestParam Long id,
            @RequestParam Integer priority,
            HttpSession session) {
        try {
            checkAdmin(session);
            Level level = levelService.setLevelPriority(id, priority);
            return ResultUtils.success(level);
        } catch (Exception e) {
            log.error("管理员设置关卡优先级失败, levelId={}, priority={}", id, priority, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 薪资请求参数
     */
    public static class SalaryRequest {
        private Integer salary;
        private String direction;

        public Integer getSalary() {
            return salary;
        }

        public void setSalary(Integer salary) {
            this.salary = salary;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }
    }
}