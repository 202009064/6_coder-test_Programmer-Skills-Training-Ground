package com.yupi.codertestbackend.controller;

import com.yupi.codertestbackend.common.BaseResponse;
import com.yupi.codertestbackend.common.ResultUtils;
import com.yupi.codertestbackend.model.entity.Level;
import com.yupi.codertestbackend.service.LevelService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 关卡 Controller
 */
@RestController
@RequestMapping("/level")
public class LevelController {

    @Resource
    private LevelService levelService;

    /**
     * 生成关卡
     */
    @PostMapping("/generate")
    public BaseResponse<Long> generate(@RequestBody Level level) {
        levelService.save(level);
        return ResultUtils.success(level.getId());
    }

    /**
     * 获取关卡详情
     */
    @GetMapping("/detail")
    public BaseResponse<Level> detail(@RequestParam Long id) {
        Level level = levelService.getById(id);
        return ResultUtils.success(level);
    }
}
