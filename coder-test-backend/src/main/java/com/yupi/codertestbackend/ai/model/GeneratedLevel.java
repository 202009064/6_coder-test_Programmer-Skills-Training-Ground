package com.yupi.codertestbackend.ai.model;

import lombok.Data;

import java.util.List;

/**
 * AI 生成的关卡内容
 */
@Data
public class GeneratedLevel {

    /**
     * 关卡名称
     */
    private String levelName;

    /**
     * 关卡需求描述（产品需求文档风格）
     */
    private String levelDesc;

    /**
     * 全部选项（10~15 个，包含正确答案和干扰项）
     */
    private List<String> options;

    /**
     * 正确选项（options 的子集）
     */
    private List<String> trueOptions;

    /**
     * 关卡难度（简单，中等，困难）
     */
    private String difficulty;

    /**
     * 目标薪资（单位：元）
     */
    private Integer targetSalary;
}