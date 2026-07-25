package com.yupi.codertestbackend.ai.service;

import com.yupi.codertestbackend.ai.model.GeneratedLevel;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 关卡生成 AI Service
 */
public interface LevelGenerationAiService {

    @SystemMessage(fromResource = "prompts/level-generation-system-prompt.txt")
    @UserMessage("""
            请生成编程关卡，参数如下：
            - 用户当前薪资：{{currentSalary}} 元
            - 关卡目标薪资：{{targetSalary}} 元
            - 题目方向：{{questionType}}
            - 技术栈：{{techStack}}
            - 关卡难度：{{difficulty}}
            - 学习方向：{{direction}}

            请直接返回 JSON，不需要额外解释。
            """
        )
    GeneratedLevel generateLevel(
            @V("currentSalary") Integer currentSalary,
            @V("targetSalary") Integer targetSalary,
            @V("questionType") String questionType,
            @V("techStack") String techStack,
            @V("difficulty") String difficulty,
            @V("direction") String direction
        );
}