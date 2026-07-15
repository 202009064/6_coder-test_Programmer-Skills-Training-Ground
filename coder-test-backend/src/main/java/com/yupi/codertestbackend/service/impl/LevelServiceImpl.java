package com.yupi.codertestbackend.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupi.codertestbackend.ai.model.GeneratedLevel;
import com.yupi.codertestbackend.ai.service.LevelGenerationAiService;
import com.yupi.codertestbackend.common.ErrorCode;
import com.yupi.codertestbackend.mapper.LevelMapper;
import com.yupi.codertestbackend.model.entity.Level;
import com.yupi.codertestbackend.service.LevelService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * 关卡 Service 实现 —— AI 生成关卡
 */
@Service
public class LevelServiceImpl extends ServiceImpl<LevelMapper, Level> implements LevelService {

    @Resource
    private LevelGenerationAiService levelGenerationAiService;

    @Resource
    private ObjectMapper objectMapper;

    private static final Random RANDOM = new Random();

    private static final List<String> QUESTION_TYPES = List.of(
            "后端架构设计", "数据库优化", "系统设计", "微服务治理",
            "缓存策略", "API设计", "性能优化", "安全方案");

    private static final List<String> TECH_STACKS = List.of(
            "Java + Spring Boot + MySQL + Redis",
            "Go + gRPC + PostgreSQL",
            "Python + Django + MongoDB",
            "微服务 + K8s + Docker",
            "Spring Cloud + Nacos + Sentinel");

    @Override
    public Level generateLevel(Integer salary) {
        if (salary == null || salary <= 0) {
            throw new RuntimeException(ErrorCode.PARAMS_ERROR.getMessage());
        }

        // 计算目标薪资：在当前薪资基础上浮动 ±30%
        int delta = (int) (salary * 0.3 * (RANDOM.nextDouble() * 2 - 1));
        Integer targetSalary = Math.max(5000, salary + delta);

        // 确定难度
        String difficulty;
        if (targetSalary <= 15000) {
            difficulty = "简单";
        } else if (targetSalary <= 30000) {
            difficulty = "中等";
        } else {
            difficulty = "困难";
        }

        // 随机选择题目方向和技术栈
        String questionType = QUESTION_TYPES.get(RANDOM.nextInt(QUESTION_TYPES.size()));
        String techStack = TECH_STACKS.get(RANDOM.nextInt(TECH_STACKS.size()));

        // 调用 AI 生成关卡
        GeneratedLevel generated;
        try {
            generated = levelGenerationAiService.generateLevel(
                    salary, targetSalary, questionType, techStack, difficulty);
        } catch (Exception e) {
            throw new RuntimeException("AI 生成关卡失败: " + e.getMessage(), e);
        }

        // 验证 AI 返回结果
        if (generated == null || generated.getLevelName() == null) {
            throw new RuntimeException("AI 生成关卡内容为空");
        }

        // 转换为 entity 并保存
        Level level = new Level();
        level.setLevelName(generated.getLevelName());
        level.setLevelDesc(generated.getLevelDesc());
        level.setOptions(toJson(generated.getOptions()));
        level.setTrueOptions(toJson(generated.getTrueOptions()));
        level.setDifficulty(generated.getDifficulty());
        level.setTargetSalary(generated.getTargetSalary());
        this.save(level);

        return level;
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON 序列化失败: " + e.getMessage(), e);
        }
    }
}