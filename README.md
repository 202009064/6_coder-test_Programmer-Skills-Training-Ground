# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在此仓库中工作提供指导。

## 项目概述

**程序员技术练兵场（coder-test）** 项目，基于鱼皮（Codefather）的「AI + 全栈项目」课程构建。后端采用 **Spring Boot 3.x + Java 21 单体应用**架构。

## 技术栈

- **Java 21** + **Spring Boot 3.5.16** + **Maven**（使用 `mvnw` 包装器）
- **MyBatis Plus 3.5.17** — ORM，支持分页插件和逻辑删除（`isDelete` 字段）
- **Knife4j 4.4.0** — API 文档（Swagger UI，中文界面），扫描 `com.yupi.codertestbackend.controller` 包
- **Hutool 5.8.46** — Java 通用工具库
- **Lombok** — 减少样板代码
- **MySQL** — 主数据库（`application.yml` 中的驱动和 URL 为占位符，待配置）

## 常用命令

```bash
# 构建 + 运行测试
./mvnw verify              # Linux/macOS
./mvnw.cmd verify          # Windows

# 启动应用（端口 8123，上下文路径 /api）
cd e:\devlop\work\6_coder-test\coder-test-backend; .\mvnw.cmd spring-boot:run

# 运行全部测试
./mvnw test

# 运行指定测试类
./mvnw test -Dtest=com.yupi.codertestbackend.CoderTestBackendApplicationTests

# 打包为 JAR（跳过测试）
./mvnw package -DskipTests
```

## 包结构

```
com.yupi.codertestbackend
├── config/              — @Configuration 配置类（如 MybatisPlusConfig）
├── mapper/              — MyBatis Plus Mapper 接口（由 MybatisPlusConfig 通过 @MapperScan 扫描）
├── controller/          — REST 控制器（由 springdoc 扫描以生成 API 文档）
└── CodTestBackendApplication.java — Spring Boot 启动类
```

预期分层架构：`controller/` → service 层 → `mapper/`（MyBatis Plus）。目前仅存在 `config/` 和启动类，项目处于早期骨架阶段。

## 配置说明

- **`application.yml`**：数据源驱动和 URL 尚未填写（标记为 `# <todo> 待生成`），运行前必须配置。
- **服务端口**：`8123`，上下文路径 `/api`，所有接口通过 `http://localhost:8123/api/**` 访问。
- **逻辑删除**：MyBatis Plus 使用 `isDelete` 作为逻辑删除字段名。
- **数据库类型**：MySQL（在 `MybatisPlusConfig` 的分页插件中配置）。
