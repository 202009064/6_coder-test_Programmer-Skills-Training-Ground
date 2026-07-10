package com.yupi.codertestbackend.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.yupi.codertestbackend.model.dto.level.UserLevelSubmitRequest;
import com.yupi.codertestbackend.model.entity.UserLevel;

/**
 * 用户关卡 Service
 */
public interface UserLevelService extends IService<UserLevel> {

    /**
     * 提交关卡答案
     */
    UserLevel submitLevel(UserLevelSubmitRequest request, Long userId);
}
