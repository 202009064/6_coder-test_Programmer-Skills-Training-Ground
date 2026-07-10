package com.yupi.codertestbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 新增关卡
     */
    @PostMapping("/add")
    public BaseResponse<Long> add(@RequestBody Level level) {
        levelService.save(level);
        return ResultUtils.success(level.getId());
    }

    /**
     * 根据 ID 查询关卡
     */
    @GetMapping("/get")
    public BaseResponse<Level> get(@RequestParam Long id) {
        Level level = levelService.getById(id);
        return ResultUtils.success(level);
    }

    /**
     * 分页查询关卡列表
     */
    @GetMapping("/list")
    public BaseResponse<Page<Level>> list(@RequestParam(defaultValue = "1") long current,
                                          @RequestParam(defaultValue = "10") long size) {
        Page<Level> page = levelService.page(new Page<>(current, size));
        return ResultUtils.success(page);
    }

    /**
     * 更新关卡
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> update(@RequestBody Level level) {
        boolean result = levelService.updateById(level);
        return ResultUtils.success(result);
    }

    /**
     * 删除关卡（逻辑删除）
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestParam Long id) {
        boolean result = levelService.removeById(id);
        return ResultUtils.success(result);
    }
}
