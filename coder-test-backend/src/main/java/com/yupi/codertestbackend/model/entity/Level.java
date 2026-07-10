package com.yupi.codertestbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 关卡实体
 */
@Data
@TableName("level")
public class Level {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关卡名称
     */
    private String levelName;

    /**
     * 关卡需求描述（AI 模拟产品经理给出的详细业务场景）
     */
    private String levelDesc;

    /**
     * 全部关卡选项（JSON 数组，包含正确答案和干扰项，至少 10 个以上）
     */
    private String options;

    /**
     * 正确选项（JSON）
     */
    private String trueOptions;

    /**
     * 关卡难度（简单，中等，困难）
     */
    private String difficulty;

    /**
     * 目标薪资（单位：元），用于动态调整关卡难度
     */
    private Integer targetSalary;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDelete;
}
