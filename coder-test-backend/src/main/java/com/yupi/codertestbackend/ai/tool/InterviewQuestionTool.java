package com.yupi.codertestbackend.ai.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;
/**
 * 面试题搜索工具 —— 从面试鸭（mianshiya.com）搜索相关面试题。
 * <p>
 * 注册为 LangChain4j 的 @Tool 后，AI 在生成结果报告时自动调用此工具搜题。
 */
@Slf4j
public class InterviewQuestionTool {

    private static final String SEARCH_URL = "https://www.mianshiya.com/search/all?searchText=";

    /**
     * 搜索结果的最大返回条目数
     */
    private static final int MAX_RESULTS = 3;

    /**
     * 搜索面试鸭题库中与指定技术相关的面试题。
     *
     * @param keyword 搜索关键词，例如 "Java 并发"、"Redis 缓存"、"微服务架构"
     * @return JSON 数组字符串，每项包含 title 和 url
     */
    @Tool("搜索面试鸭题库中与指定技术相关的面试题，返回题目列表")
    public String searchInterviewQuestions(
            @P("搜索关键词，请使用关卡涉及的核心技术点作为关键词，例如 'Java 并发'、'Redis 缓存'、'Spring Boot'")
            String keyword
    ) {
        if (keyword == null || keyword.isBlank()) {
            return "[]";
        }

        try {
            String url = SEARCH_URL + URLEncoder.encode(keyword, "UTF-8");
            log.info("搜索面试题，关键词: {}，URL: {}", keyword, url);

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .header("Accept", "text/html,application/xhtml+xml")
                    .header("Accept-Language", "zh-CN,zh;q=0.9")
                    .timeout(15000)
                    .get();

            // 找到 .question-table-view 容器
            Element table = doc.selectFirst(".question-table-view");
            if (table == null) {
                log.warn("未找到 .question-table-view 元素，关键词: {}", keyword);
                return "[]";
            }

            // 每行是一个 .ant-table-row，从中提取标题和链接
            List<QuestionItem> items = new ArrayList<>();
            Elements rows = table.select(".ant-table-row");
            for (Element row : rows) {
                // 获取该行的所有 .ant-table-cell
                Elements cells = row.select(".ant-table-cell");
                if (cells.size() < 2) {
                    continue;
                }

                // 第 2 列（index 1）包含题目标题和链接
                Element titleCell = cells.get(1);
                Element a = titleCell.selectFirst("a[href]");
                if (a == null) {
                    continue;
                }

                String href = a.attr("href");
                String title = a.text().trim();

                // 跳过空标题和过短标题
                if (title.isEmpty() || title.length() < 3) {
                    continue;
                }

                // 构建完整 URL
                String fullUrl = href.startsWith("http") ? href : "https://www.mianshiya.com" + href;

                items.add(new QuestionItem(title, fullUrl));

                if (items.size() >= MAX_RESULTS) {
                    break;
                }
            }

            if (items.isEmpty()) {
                log.warn("未从 .question-table-view 中提取到题目，关键词: {}", keyword);
                return "[]";
            }

            // 转 JSON 字符串
            String json = toJson(items);
            log.info("搜索到 {} 道面试题，关键词: {}", items.size(), keyword);
            return json;

        } catch (Exception e) {
            log.error("搜索面试题失败，关键词: {}，错误: {}", keyword, e.getMessage(), e);
            return "[]";
        }
    }

    /**
     * 简单的 JSON 序列化（避免依赖 Jackson）
     */
    private String toJson(List<QuestionItem> items) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            QuestionItem item = items.get(i);
            sb.append("{\"title\":\"")
                    .append(escapeJson(item.title))
                    .append("\",\"url\":\"")
                    .append(escapeJson(item.url))
                    .append("\"}");
        }
        sb.append("]");
        return sb.toString();
    }

    private String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * 内部题目项
     */
    private static class QuestionItem {
        final String title;
        final String url;

        QuestionItem(String title, String url) {
            this.title = title;
            this.url = url;
        }
    }
}
