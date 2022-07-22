<h1 style="text-align: center">JeeAgile 敏捷快速开发平台</h1>

#### 项目简介

- JeeAgile参考了很多优秀的开源项目，并结合自己的一些想法和实践，研发出来的一套快速开发框架。
- JeeAgile解决了由单体应用向分布式应用转换带来的各种繁琐问题。
- JeeAgile项目模块这么多？请不要被吓到,只是为了将模块细化，让使用者有更多的选择机会。
- JeeAgile架构主要以Spring Boot为核心，分为model、api、service、web四层，基于配置和自定义注解可有单体应用向分布式应用进行切换。

**开发文档：**  [http://docs.jeeagile.vip](http://docs.jeeagile.vip)

**体验地址：**  [http://demo.jeeagile.vip](http://demo.jeeagile.vip)

**账号密码：** `admin / 123456`

#### 项目源码
- gitee      https://gitee.com/jeeagile/jeeagile
- github     https://github.com/jeeagile/jeeagile


#### 架构亮点
- 组件模块化，通过模块化的设计理念，便于灵活扩展和二次开发。
- 支持不同安全认证架构，目前提供有基于Apache Shiro、Spring Security认证组件。
- 支持不同缓存技术，可基于Spring Cache实现Redis、EhCache的灵活切换使用。
- 多数据库支持，基于MyBatis plus，可支持MySql、Oracle、SQLServer、达梦等众多数据库
- 支持单体应用和分布式应用切换，基于自定义注解可实现单体应用、dubbo分布式应用、rabbitmq分布式应用切换，
- 提供代码表缓存技术，支持通过代码值快速获取代码名称、代码实体对象以及获取代码缓存列表和刷新缓存等接口。
- 可解决网络隔离问题，通过使用RabbitMq作为消息中心对消息进行转发，解决网络通讯限制问题。
    说明：很多政府项目都存在内外网隔离情况，WEB层不允许直接访问数据库，可通过RabbitMq作为消息中心进行消息转发。

#### 技术选型
    
1、后端

* 核心框架：Spring Boot
* 安全框架：Apache Shiro(可选)、Spring Security（可选）
* 持久层框架：MyBatis plus
* 数据库连接池：Alibaba Druid 
* 服务端验证：Hibernate Validator
* 任务调度：Quartz
* 缓存框架：Ehcache(可选)、Redis(可选) ....
* 日志管理：SLF4J 
* 工具类：Apache Commons、 FastJson

2、前端

* VUE、Element UI 


#### 内置功能
1、系统管理
- 租户管理：支持无限级租户模式（可自由开启）
- 用户管理：提供用户的相关配置
- 角色管理：权限与菜单进行分配
- 菜单管理：实现菜单动态路由，后端可配置化，支持多级菜单
- 部门管理：可配置系统组织架构，树形表格展示
- 岗位管理：配置各个部门的职位
- 字典管理：可维护常用数据，如：状态，性别等

2、日志管理
- 系统日志：记录用户操作日志
- 登录日志：记录用户登录日志

3、系统监控
- SQL监控：采用druid 监控数据库访问性能
- 服务端监控：监控服务器的负载情况
- 在线用户：监控当前在线用户，可强行将用户下线

4、定时任务
- 定时任务：整合Quartz做定时任务

5、开发工具
- 表单设计：基于form-generator实现表单在线设计
- 工作流设计：基于BPMN实现工作流流程设计
- 代码生成：高灵活度生成前后端代码，减少大量重复的工作任务
- 系统接口：结合knife4j和swagger展示后端接口

6、工作流
- 工作流表单设计
- 工作流模型设计

#### 租户开启
1、配置文件开启租户
```
agile.tenant.enable=true
```
2、执行数据库脚本
```
 agile-tenant.sql
```
3、默认租户访问地址
```
http://localhost/login?tenantId=jeeagile&tenantSign=08ba7d68a2e24774ced85c281ac830de
```

#### 目录说明
-----------------------------------
```
jeeagile      
├─jeeagile-core       核心模块 包括用户安全认证、缓存、工具类等
│  
├─jeeagile-frame      开发框架基础依赖模块 
│  ├─jeeagile-frame-api       开发框架api层
│  │
│  ├─jeeagile-frame-model     开发框架model层
│  │
│  ├─jeeagile-frame-service   开发框架service层 
│  │
│  └─jeeagile-frame-web       开发框架web层
│  
├─jeeagile-module     系统默认实现模块
│  │
│  ├─jeeagile-generator     代码生成模块
│  │  ├─jeeagile-generator-api     代码生成模块api层
│  │  │
│  │  ├─jeeagile-generator-model   代码生成模块model层
│  │  │
│  │  ├─jeeagile-generator-service 代码生成模块service层
│  │  │
│  │  └─jeeagile-generator-web     代码生成模块web层
│  │  
│  ├─jeeagile-quartz         队列管理模块
│  │  ├─jeeagile-quartz-api        队列管理模块api层
│  │  │
│  │  ├─jeeagile-quartz-model      队列管理模块model层
│  │  │
│  │  ├─jeeagile-quartz-service    队列管理模块service层
│  │  │
│  │  └─jeeagile-quartz-web        队列管理模块web层
│  │  
│  ├─jeeagile-process     工作流模块
│  │  ├─jeeagile-process-api        工作流模块api层
│  │  │
│  │  ├─jeeagile-process-model      工作流模块model层
│  │  │
│  │  ├─jeeagile-process-service    工作流模块service层
│  │  │
│  │  └─jeeagile-process-web        工作流模块web层
│  │  
├─jeeagile-plugin      框架第三方插件支持
│  ├─jeeagile-plugin-crypto        加解密插件
│  │
│  ├─jeeagile-plugin-http          http请求工具类
│  │
│  └─jeeagile-plugin-redis         redis操作静态工具类
│  
├─jeeagile-security    安全框架支持
│  ├─jeeagile-security-shiro       apache shiro安全框架集成
│  │
│  └─jeeagile-security-boot        spring security安全框架集成
│  
├─jeeagile-vue-ui                  前端UI模块
│  
```
#### 特别提醒
出于各种原因，暂不上传rabbitmq插件代码，如有需要可联系作者（QQ:1393704475）。

#### 特别鸣谢

感谢[MybatisPlus](https://mp.baomidou.com/)、[RuoYi](http://www.ruoyi.vip/)、[vue-element-admin](https://panjiachen.github.io/)、[form-generator](https://gitee.com/mrhj/form-generator)等优秀开源项目。

