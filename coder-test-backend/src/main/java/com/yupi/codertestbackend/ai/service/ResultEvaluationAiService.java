package com.yupi.codertestbackend.ai.service;

import com.yupi.codertestbackend.ai.model.EvaluationResult;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 结果评估 AI Service
 */
public interface ResultEvaluationAiService {

    @SystemMessage(fromResource = "prompts/result-evaluation-system-prompt.txt")
    @UserMessage("""
            请评估以下闯关结果：

            【关卡名称】{{levelName}}
            【关卡描述】{{levelDesc}}
            【用户选择的选项】{{userOptions}}
            【正确选项】{{trueOptions}}
            【用户当前薪资】{{salary}} 元

            请直接返回 JSON，不需要额外解释。
            """)
    EvaluationResult evaluate(
            @V("levelName") String levelName,
            @V("levelDesc") String levelDesc,
            @V("userOptions") String userOptions,
            @V("trueOptions") String trueOptions,
            @V("salary") Integer salary);
}