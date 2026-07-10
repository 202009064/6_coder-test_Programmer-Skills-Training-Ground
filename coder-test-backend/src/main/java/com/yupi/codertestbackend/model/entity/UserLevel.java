package com.yupi.codertestbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户关卡实体（记录用户闯关信息）
 */
@Data
@TableName("user_level")
public class UserLevel {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 关卡 ID
     */
    private Long levelId;

    /**
     * 用户选择的选项（JSON）
     */
    private String userOptions;

    /**
     * 作答分数（满分 100 分）
     */
    private Integer score;

    /**
     * 评价
     */
    private String comment;

    /**
     * 薪资调整（单位：元，正数为增加，负数为减少）
     */
    private Integer salaryChange;

    /**
     * 公司投递建议
     */
    private String suggest;

    /**
     * 评分原因
     */
    private String reason;

    /**
     * 正确选项（JSON）
     */
    private String trueOptions;

    /**
     * 标准答案解析
     */
    private String standardAnswer;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDelete;
}
