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
            【学习方向】{{direction}}
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
            @V("salary") Integer salary,
            @V("direction") String direction
    );
    /**
     * 这整个方法定义了一套 AI驱动的结构评估流程：
     *  ① 调用方传入关卡信息、用户答案、正确答案、薪资、学习方向等参数
     *  ② LangChain4j 框架将这些参数填充到 @UserMessage 模板中，配合 @SystemMessage 指定的系统提示词，组装成完整的 prompt 发送给 AI；
     *  ③ AI返回的 JSON 被自动反序列化为 EvaluationResult 对象返回给调用方
     * 
     * evaluate方法。调用此方法时，LangChain4j 会使用上面标注的 @SystemMessage 和 @UserMessage 组装提示词，发送给 AI 模型，并自动解析响应。
     * 
     * @V("levelName") String levelName,
     * @V("levelName") — LangChain4j 的 @V 注解，声明一个模板变量。"levelName" 是该变量在 UserMessage 模板中的占位符名称，对应模板中第 17 行的 {{levelName}}。
     * String levelName — 方法参数，传入关卡名称（如 "Java 基础挑战"），会在运行时替换模板中的 {{levelName}}。
     */
}