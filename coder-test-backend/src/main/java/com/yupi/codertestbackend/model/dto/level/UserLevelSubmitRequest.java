package com.yupi.codertestbackend.model.dto.level;

/**
 * 用户提交关卡答案请求
 */
public class UserLevelSubmitRequest {

    /**
     * 关卡 ID
     */
    private Long levelId;

    /**
     * 用户选择的选项（JSON 数组字符串）
     */
    private String userOptions;

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public String getUserOptions() {
        return userOptions;
    }

    public void setUserOptions(String userOptions) {
        this.userOptions = userOptions;
    }
}
