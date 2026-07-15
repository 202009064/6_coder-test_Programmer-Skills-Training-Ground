package com.yupi.codertestbackend.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.yupi.codertestbackend.ai.model.EvaluationResult;
import com.yupi.codertestbackend.ai.service.ResultEvaluationAiService;
import com.yupi.codertestbackend.common.ErrorCode;
import com.yupi.codertestbackend.mapper.UserLevelMapper;
import com.yupi.codertestbackend.mapper.UserMapper;
import com.yupi.codertestbackend.model.dto.level.UserLevelSubmitRequest;
import com.yupi.codertestbackend.model.entity.Level;
import com.yupi.codertestbackend.model.entity.User;
import com.yupi.codertestbackend.model.entity.UserLevel;
import com.yupi.codertestbackend.service.LevelService;
import com.yupi.codertestbackend.service.UserLevelService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户关卡 Service 实现 —— AI 评估闯关结果
 */
@Service
public class UserLevelServiceImpl extends ServiceImpl<UserLevelMapper, UserLevel> implements UserLevelService {

    @Resource
    private LevelService levelService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ResultEvaluationAiService resultEvaluationAiService;

    @Override
    public UserLevel submitLevel(UserLevelSubmitRequest request, Long userId) {
        // 校验参数
        if (request == null || request.getLevelId() == null || request.getUserOptions() == null) {
            throw new RuntimeException(ErrorCode.PARAMS_ERROR.getMessage());
        }

        // 校验关卡是否存在
        Level level = levelService.getById(request.getLevelId());
        if (level == null) {
            throw new RuntimeException(ErrorCode.LEVEL_NOT_FOUND.getMessage());
        }

        // 校验用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException(ErrorCode.USER_NOT_FOUND.getMessage());
        }

        // ========== 调用 AI 生成结果报告 ==========
        EvaluationResult result;
        try {
            result = resultEvaluationAiService.evaluate(
                    level.getLevelName(),
                    level.getLevelDesc(),
                    request.getUserOptions(),
                    level.getTrueOptions(),
                    user.getSalary());
        } catch (Exception e) {
            throw new RuntimeException("AI 评估失败: " + e.getMessage(), e);
        }

        // 验证 AI 响应
        if (result == null || result.getScore() == null) {
            throw new RuntimeException("AI 评估结果为空");
        }

        int score = result.getScore();
        String comment = result.getComment();
        int salaryChange = result.getSalaryChange();
        String suggest = result.getSuggest();
        String reason = result.getReason();
        String standardAnswer = result.getStandardAnswer();
        // ==========================================

        // 构建 UserLevel 记录
        UserLevel userLevel = new UserLevel();
        userLevel.setUserId(userId);
        userLevel.setLevelId(request.getLevelId());
        userLevel.setUserOptions(request.getUserOptions());
        userLevel.setScore(score);
        userLevel.setComment(comment);
        userLevel.setSalaryChange(salaryChange);
        userLevel.setSuggest(suggest);
        userLevel.setReason(reason);
        userLevel.setTrueOptions(level.getTrueOptions());
        userLevel.setStandardAnswer(standardAnswer);
        userLevel.setCreateTime(LocalDateTime.now());
        userLevel.setUpdateTime(LocalDateTime.now());

        // 保存闯关记录
        this.save(userLevel);

        // 更新用户薪资
        user.setSalary(user.getSalary() + salaryChange);
        userMapper.updateById(user);

        return userLevel;
    }
}