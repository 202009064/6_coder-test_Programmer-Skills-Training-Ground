package com.yupi.codertestbackend.ai.model;

import lombok.Data;

/**
 * AI 生成的闯关结果评估报告
 */
@Data
public class EvaluationResult {

    /**
     * 分数（0~100）
     */
    private Integer score;

    /**
     * 幽默评价
     */
    private String comment;

    /**
     * 薪资调整（单位：元，正数为增加，负数为减少）
     */
    private Integer salaryChange;

    /**
     * 公司投递建议（使用虚构公司名）
     */
    private String suggest;

    /**
     * 评分原因
     */
    private String reason;

    /**
     * 标准答案详细解析
     */
    private String standardAnswer;
}