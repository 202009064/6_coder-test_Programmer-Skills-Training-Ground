package com.yupi.codertestbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.IService;
import com.yupi.codertestbackend.model.entity.Level;

/**
 * 关卡 Service
 */
public interface LevelService extends IService<Level> {

    /**
     * AI 生成关卡（根据用户薪资动态调整难度）
     *
     * @param salary 用户当前薪资（单位：元）
     * @return 生成的关卡
     */
    Level generateLevel(Integer salary);

    /**
     * 管理员分页查询所有关卡（支持按名称、难度、优先级筛选）
     */
    Page<Level> listLevels(Integer current, Integer size, String keyword, String difficulty, Integer priority);

    /**
     * 管理员更新关卡信息
     */
    Level updateLevel(Level level);

    /**
     * 管理员删除关卡
     */
    void deleteLevel(Long id);

    /**
     * 管理员设置关卡优先级（精选/置顶）
     */
    Level setLevelPriority(Long id, Integer priority);
}