package com.yupi.codertestbackend.controller;

import com.yupi.codertestbackend.common.BaseResponse;
import com.yupi.codertestbackend.common.ErrorCode;
import com.yupi.codertestbackend.common.ResultUtils;
import com.yupi.codertestbackend.model.entity.Level;
import com.yupi.codertestbackend.service.LevelService;
import jakarta.annotation.Resource;
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

    /**
     * AI 生成关卡（根据用户薪资动态调整难度）
     */
    @PostMapping("/generate")
    public BaseResponse<Level> generate(@RequestBody SalaryRequest request) {
        try {
            if (request == null || request.getSalary() == null) {
                throw new RuntimeException(ErrorCode.PARAMS_ERROR.getMessage());
            }
            Level level = levelService.generateLevel(request.getSalary());
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
     * 薪资请求参数
     */
    public static class SalaryRequest {
        private Integer salary;

        public Integer getSalary() {
            return salary;
        }

        public void setSalary(Integer salary) {
            this.salary = salary;
        }
    }
}