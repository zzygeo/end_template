# SpringBoot 项目初始模板

Java SpringBoot 项目初始模板，整合了常用框架和示例代码，大家可以在此基础上快速开发自己的项目。

## 项目简介

本仓库是一个**开箱即用的 Spring Boot 后端项目脚手架**，内置了企业级后端开发中最常用的功能模块（用户认证、权限管理、日志审计、文件管理、系统监控等），让你无需从零搭建，专注于业务逻辑开发。

## 模板功能

- Spring Boot 2.6.13
- Spring MVC
- MySQL 驱动
- Druid 连接池
- MyBatis
- MyBatis Plus（含逻辑删除支持）
- Redis + JWT 无状态分布式登陆
- Spring AOP
- 基于 AOP 的权限校验（`@AuthCheck` 注解）
- 基于 AOP 的操作日志记录（`@Log` 注解）
- Apache Commons Lang3 工具类
- Hutool 工具库
- Lombok 注解
- FastJson2 序列化
- Swagger + Knife4j 接口文档
- EasyExcel 文件导入导出
- OSHI 系统资源监控
- 操作日志管理 + 登陆日志管理
- 全局异常处理器
- 自定义错误码
- 封装通用响应类
- 全局拦截器 + 登陆校验
- 示例用户注册、登录、搜索功能
- 示例 SQL（用户表、操作日志表、登陆日志表、菜单表、文件表）
- 文件上传与管理
- 菜单管理
- 设备管理

## 快速开始

### 环境要求

- JDK 11+
- Maven 3.6+
- MySQL 5.7+
- Redis

### 1. 初始化数据库

执行 `sql/template_ddl.sql` 文件，创建数据库和初始表结构：

```sql
source sql/template_ddl.sql
```

### 2. 修改配置文件

修改 `src/main/resources/application-dev.yml`，填入你的数据库和 Redis 连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/biaohui?useUnicode=true&characterEncoding=utf8
    username: 你的数据库用户名
    password: 你的数据库密码
  redis:
    host: localhost
    port: 6379
```

### 3. 启动项目

```bash
./mvnw spring-boot:run
```

或者直接运行 `BiaoHuiApplication.java` 中的 main 方法。

### 4. 访问接口文档

项目启动后，访问以下地址即可在线查看并调试所有接口，无需前端配合：

```
http://localhost:7531/api/doc.html
```

## 项目结构

```
src/main/java/com/zzy/biaohui/
├── annotation/    # 自定义注解（@AuthCheck 权限校验、@Log 操作日志）
├── aop/           # AOP 切面（权限拦截、日志记录）
├── common/        # 公共枚举、常量、通用响应类
├── config/        # Spring 配置（Redis、MybatisPlus、Knife4j 等）
├── controller/    # REST 控制器（用户、文件、菜单、日志、监控等）
├── exception/     # 全局异常处理器、自定义异常
├── filter/        # 过滤器
├── interceptor/   # 登陆校验拦截器
├── manager/       # 业务管理器（异步日志写入等）
├── mapper/        # MyBatis Mapper 接口
├── model/         # 数据模型（Entity / DTO / VO / Enum）
├── service/       # Service 接口及实现
└── utils/         # 工具类（IP、时间、安全、线程、用户上下文等）
```

## Docker 部署

项目提供了 Docker 配置，可以快速启动一个配置好的 MySQL 容器：

```bash
cd docker
docker build -t biaohui-mysql .
docker run -d -p 3306:3306 --name biaohui-mysql biaohui-mysql
```

## 主要接口模块

| 模块 | 说明 |
|------|------|
| 用户管理 | 注册、登录、查询、权限控制 |
| 文件管理 | 文件上传、下载、列表查询 |
| 菜单管理 | 系统菜单的增删改查 |
| 操作日志 | 基于 AOP 自动记录接口调用日志 |
| 登陆日志 | 记录用户登录信息（IP、浏览器、OS） |
| 系统监控 | CPU、内存、磁盘等服务器资源实时监控 |
| 设备管理 | 设备信息的管理 |