# Personal Financial

一个基于 Spring Boot 的个人与群组记账项目，包含账单记录、分类、收支统计、群组和用户管理接口。

## 技术

Java 11、Spring Boot 2.6、MyBatis Plus、MySQL、Maven。

## 配置

1. 准备 Java 11、Maven 和 MySQL。
2. 复制 src/main/resources/application.example.yaml 为同目录下的 application.yaml。
3. 设置数据库连接，并通过环境变量 DB_USERNAME、DB_PASSWORD 和 JWT_SECRET 提供配置。
4. 使用 Maven 导入或构建项目；运行入口是 com.u.Application。

