-- ============================================================
-- 程序员技术练兵场（coder-test）数据库建表语句
-- 数据库名：coder-test
-- 数据库版本：MySQL 8.x
-- ============================================================

CREATE DATABASE IF NOT EXISTS `coder-test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `coder-test`;

-- ============================================================
-- 1. 用户表
-- 提供最基础的用户登录、用户昵称能力
-- 薪资字段用于动态调整关卡难度
-- ============================================================
CREATE TABLE IF NOT EXISTS `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    VARCHAR(256) NOT NULL COMMENT '登录账号',
    `password`    VARCHAR(512) NOT NULL COMMENT '登录密码',
    `nickname`    VARCHAR(256) DEFAULT NULL COMMENT '用户昵称',
    `salary`      INT          NOT NULL DEFAULT 0 COMMENT '当前薪资（单位：元），用于动态调整关卡难度',
    `createTime`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`    TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_username` (`username`),
    INDEX `idx_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================================
-- 2. 关卡表
-- 存储 AI 曾经生成过的关卡信息
-- options 和 trueOptions 以 JSON 格式存储，避免额外的选项关联表
-- ============================================================
CREATE TABLE IF NOT EXISTS `level` (
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `levelName`        VARCHAR(256) NOT NULL COMMENT '关卡名称',
    `levelDesc`        TEXT         NOT NULL COMMENT '关卡需求描述（AI 模拟产品经理给出的详细业务场景）',
    `options`          TEXT         NOT NULL COMMENT '全部关卡选项（JSON 数组，包含正确答案和干扰项，至少 10 个以上）',
    `trueOptions`      TEXT         NOT NULL COMMENT '正确选项（JSON）',
    `difficulty`       VARCHAR(256) NOT NULL COMMENT '关卡难度（简单，中等，困难）',
    `targetSalary`     INT          NOT NULL DEFAULT 10000 COMMENT '目标薪资（单位：元），用于动态调整关卡难度',
    `createTime`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`         TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    INDEX `idx_levelName` (`levelName`),
    INDEX `idx_createTime` (`createTime`),
    INDEX `idx_difficulty` (`difficulty`),
    INDEX `idx_targetSalary` (`targetSalary`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关卡表';

-- ============================================================
-- 3. 用户关卡表
-- 记录用户闯关的信息，包括选择的选项和 AI 生成的结果报告
-- ============================================================
CREATE TABLE IF NOT EXISTS `user_level` (
    `id`              BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `userId`          BIGINT   NOT NULL COMMENT '用户 ID',
    `levelId`         BIGINT   NOT NULL COMMENT '关卡 ID',
    `userOptions`     TEXT     NOT NULL COMMENT '用户选择的选项（JSON）',
    `score`           INT      DEFAULT 0 COMMENT '作答分数（满分 100 分）',
    `comment`         TEXT     DEFAULT NULL COMMENT '评价（对关卡的评价、建议等）',
    `salaryChange`    INT      DEFAULT 0 COMMENT '薪资调整（单位：元，正数为增加，负数为减少），根据用户选择的选项动态调整',
    `suggest`         TEXT     DEFAULT NULL COMMENT '公司投递建议（对用户投递的简历的建议）',
    `reason`          TEXT     DEFAULT NULL COMMENT '评分原因',
    `trueOptions`     TEXT     DEFAULT NULL COMMENT '正确选项（JSON）',
    `standardAnswer`  TEXT     DEFAULT NULL COMMENT '标准答案解析（对正确选项的详细解析）',
    `createTime`      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`        TINYINT  NOT NULL DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    INDEX `idx_userId` (`userId`),
    INDEX `idx_levelId` (`levelId`),
    INDEX `idx_userId_levelId` (`userId`, `levelId`),
    INDEX `idx_score` (`score`),
    INDEX `idx_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户关卡表';
