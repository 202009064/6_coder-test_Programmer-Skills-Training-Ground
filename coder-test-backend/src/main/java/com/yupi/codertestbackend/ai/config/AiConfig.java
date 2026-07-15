package com.yupi.codertestbackend.ai.config;

import com.yupi.codertestbackend.ai.service.LevelGenerationAiService;
import com.yupi.codertestbackend.ai.service.ResultEvaluationAiService;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI 配置类 —— 创建 QwenChatModel 和 AI Service Bean
 */
@Configuration
public class AiConfig {

    @Value("${langchain4j.dashscope.api-key}")
    private String apiKey;

    @Value("${langchain4j.dashscope.model-name:qwen-max}")
    private String modelName;

    @Value("${langchain4j.dashscope.temperature:0.7}")
    private Float temperature;

    @Value("${langchain4j.dashscope.max-tokens:4096}")
    private Integer maxTokens;

    @Bean
    public QwenChatModel qwenChatModel() {
        return QwenChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .build();
    }

    @Bean
    public LevelGenerationAiService levelGenerationAiService(QwenChatModel model) {
        return AiServices.create(LevelGenerationAiService.class, model);
    }

    @Bean
    public ResultEvaluationAiService resultEvaluationAiService(QwenChatModel model) {
        return AiServices.create(ResultEvaluationAiService.class, model);
    }
}