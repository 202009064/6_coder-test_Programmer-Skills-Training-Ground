package com.yupi.codertestbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 登录账号
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 当前薪资（单位：元），用于动态调整关卡难度
     */
    private Integer salary;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDelete;
}
