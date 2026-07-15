package com.yupi.codertestbackend.service;

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
}