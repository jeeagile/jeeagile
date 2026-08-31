

# JeeAgile 敏捷快速开发平台

## 项目简介

JeeAgile 是一款参考了众多优秀开源项目，并结合自身实践研发出的快速开发框架。该平台解决了由单体应用向分布式应用转换过程中带来的各种繁琐问题。JeeAgile 采用模块化设计理念，使用者可以根据需求灵活选择模块。架构以 Spring Boot 为核心，分为 model、api、service、web 四层，支持通过配置和自定义注解在单体应用和分布式应用之间进行切换。

**开发文档：** http://docs.jeeagile.vip

**体验地址：** http://demo.jeeagile.vip

**账号密码：** admin / 123456

**项目源码：**
- Gitee：https://gitee.com/jeeagile/jeeagile
- GitHub：https://github.com/jeeagile/jeeagile

## 架构亮点

- **组件模块化**：通过模块化的设计理念，便于灵活扩展和二次开发
- **多安全框架支持**：提供 Apache Shiro 和 Spring Security 两种认证组件
- **多缓存技术**：基于 Spring Cache 可灵活切换 Redis、EhCache 等缓存方案
- **多数据库支持**：基于 MyBatis Plus，支持 MySQL、Oracle、SQLServer、达梦等众多数据库
- **单体/分布式切换**：基于自定义注解可实现单体应用、Dubbo 分布式应用、RabbitMQ 分布式应用的切换
- **代码表缓存技术**：支持通过代码值快速获取代码名称、代码实体对象及缓存列表刷新等接口
- **网络隔离解决方案**：通过 RabbitMQ 作为消息中心转发消息，解决内外网隔离带来的通讯限制问题

## 技术选型

### 后端技术

| 类别 | 技术 |
|------|------|
| 核心框架 | Spring Boot |
| 安全框架 | Apache Shiro / Spring Security |
| 持久层框架 | MyBatis Plus |
| 数据库连接池 | Alibaba Druid |
| 服务端验证 | Hibernate Validator |
| 任务调度 | Quartz |
| 缓存框架 | Ehcache / Redis |
| 日志管理 | SLF4J |
| 工具类 | Apache Commons、FastJson |

### 前端技术

- Vue
- Element UI

## 内置功能

### 1. 系统管理
- **租户管理**：支持无限级租户模式（可通过配置开启）
- **用户管理**：提供用户相关配置
- **角色管理**：权限与菜单分配
- **菜单管理**：实现菜单动态路由，后端可配置化，支持多级菜单
- **部门管理**：配置系统组织架构，树形表格展示
- **岗位管理**：配置各部门职位
- **字典管理**：维护常用数据，如状态、性别等

### 2. 日志管理
- **系统日志**：记录用户操作
- **登录日志**：记录用户登录信息

### 3. 系统监控
- **SQL监控**：采用 Druid 监控数据库访问性能
- **服务端监控**：监控服务器负载情况
- **在线用户**：监控当前在线用户，可强制下线

### 4. 定时任务
- 整合 Quartz 实现定时任务管理

### 5. 开发工具
- **表单设计**：基于 form-generator 实现表单在线设计
- **工作流设计**：基于 BPMN 实现工作流流程设计
- **代码生成**：高灵活度生成前后端代码，减少重复工作
- **系统接口**：结合 Knife4j 和 Swagger 展示后端接口

### 6. 在线表单
- 在线表单字典管理
- 在线表单页面设计

### 7. 工作流
- 工作流表单设计
- 工作流模型设计

## 快速开始

### 租户开启

1. 配置文件开启租户
```properties
agile.tenant.enable=true
```

2. 执行数据库脚本
```
09-agile-tenant.sql
```

3. 默认租户访问地址
```
http://localhost/login?tenantId=jeeagile&tenantSign=08ba7d68a2e24774ced85c281ac830de
```

## 目录结构

```
jeeagile      
├─jeeagile-core              核心模块（用户安全认证、缓存、工具类等）
│  
├─jeeagile-frame             开发框架基础依赖模块
│  ├─jeeagile-frame-api      API层
│  ├─jeeagile-frame-model    Model层
│  ├─jeeagile-frame-service  Service层 
│  └─jeeagile-frame-web      Web层
│  
├─jeeagile-module            系统默认实现模块
│  ├─jeeagile-generator      代码生成模块
│  ├─jeeagile-quartz         定时任务管理模块
│  ├─jeeagile-process        工作流模块
│  └─jeeagile-demo           示例模块
│  
├─jeeagile-plugin            框架第三方插件支持
│  ├─jeeagile-plugin-crypto  加解密插件
│  ├─jeeagile-plugin-http    HTTP请求工具类
│  ├─jeeagile-plugin-redis   Redis操作静态工具类
│  └─jeeagile-plugin-drools  规则引擎插件
│  
├─jeeagile-security          安全框架支持
│  ├─jeeagile-security-shiro Apache Shiro集成
│  └─jeeagile-security-boot  Spring Security集成
│  
├─jeeagile-protocol          分布式协议支持
│  └─jeeagile-protocol-dubbo Dubbo协议集成
│  
└─jeeagile-vue-ui            前端UI模块
```

## 特别说明

出于各种原因，暂不上传 RabbitMQ 插件代码，如有需要可联系作者（QQ:190912896）。

## 特别鸣谢

感谢以下优秀开源项目：

- [MybatisPlus](https://mp.baomidou.com/)
- [RuoYi](http://www.ruoyi.vip/)
- [vue-element-admin](https://panjiachen.github.io/)
- [form-generator](https://gitee.com/mrhj/form-generator)

## License

本项目基于 Apache License 2.0 协议开源。