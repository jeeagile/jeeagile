<h1 style="text-align: center">JeeAgile 敏捷快速开发平台</h1>
#### 项目简介
- JeeAgile是参考了很多优秀的开源项目，并结合自己的一些想法和实践，研发出来的一套快速开发框架。
- 取之于民，用之于民。JeeAgile对广大码农同学完全开源，切会根据广大码农同学的意见不断优化和改进，同时也欢迎有志之士加入共同维护。
- 天下武功，唯快不破。JeeAgile在解决快的同时也不失灵活性，通过模块拆分和自定义注解，使开发者可以灵活组合、随心所欲使用。
- JeeAgile同时解决了由单体应用向分布式应用转换带来的各种繁琐问题——谁用谁知道，嘿嘿。
- JeeAgile项目模块这么多？请不要被吓到。当你真正去使用他时候，就会发现她有很多灵活之处，且有很多场景供你选择。
- JeeAgile架构以Spring Boot为核心，将代码架构分为model层、api层、service层和web层共四层，可基于配置和自定义注解进行单体应用和分布式应用切换。

**开发文档：**  [http://docs.jeeagile.vip](http://docs.jeeagile.vip) ---完善中，敬请期待

**体验地址：**  [http://demo.jeeagile.vip](http://demo.jeeagile.vip)

**账号密码：** `admin / 123456`

#### 架构亮点
- 完全的模块化，超前的平台架构和模块化的设计理念，便于灵活扩展和二次开发。
- 完善的组件化，JeeAgile将提供各种丰富的前后端组件，开发者可根据自身需要选择使用。
- 多数据库支持，基于MyBatis plus，可支持MySql、Oracle、SQLServer、达梦等众多数据库（由于时间问题，后续会不断进行补充数据库脚本,也可通过JeeAgile.pdm自行生成对应数据库脚本）
- 灵活支持单体应用和分布式应用，基于自定义注解开发，通过配置灵活实现单体应用、dubbo分布式应用、rabbitmq分布式应用（主要解决网络隔离问题）等快速切换，
- 灵活支持不同安全认证架构，目前已实现Apache Shiro组件支持，Spring Security集成尚在完善中，也可自定义实现安全验证架构
- 灵活支持不同缓存技术，基于Spring Cache已实现了Redis、EhCache的灵活切换使用，未来会加入更多的缓存技术供选择使用
- 灵活支持代码表缓存，基于自定义注解实现代码表缓存加载，并提供方法通过代码值快速获取代码名称、通过代码值快速获取代码实体对象以及获取代码缓存列表和刷新缓存等。
- 灵活解决网络隔离问题，通过使用RabbitMq作为消息中心对消息进行转发，解决网络通讯限制问题。
    说明：部分项目为了数据安全，在网络上做了防火墙隔离操作，WEB层不允许直接访问数据库，通过RabbitMq进行消息转发，由SERVICE层做业务逻辑处理，处理完成后在交给消息中心，并通知WEB层将数据返回前端进行展示。

## 技术选型
    
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

* VUE 、Element UI


####  系统内置模块功能
1、系统管理
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
- 代码生成：高灵活度生成前后端代码，减少大量重复的工作任务
- 在线构建器：拖动表单元素生成相应的HTML代码。
6、工作流
- 待开发


平台目录结构说明
-----------------------------------
```
jeeagile      
├─jeeagile-core       核心模块 包括用户安全认证、缓存、工具类等
├─jeeagile-frame      开发框架依赖模块 包括api层、model层、service层、web层
│  ├─jeeagile-frame-api       开发框架api层 主要是接口基类
│  │
│  ├─jeeagile-frame-model     开发框架model层 主要包括实体基类和分页工具类等
│  │
│  ├─jeeagile-frame-service   开发框架service层 主要包括接口实现层基类、事务控制、Dao层基类等
│  │
│  └─jeeagile-frame-web       开发框架web层 主要包括外部应用接口基类和安全控制等
│  
├─jeeagile-module     系统默认实现模块
│  ├─jeeagile-activiti     工作流模块（待实现）
│  │
│  ├─jeeagile-admin        后端管理模块（引入其他模块）
│  │  ├─jeeagile-admin-api         系统管理模块api层
│  │  │
│  │  ├─jeeagile-admin-model       系统管理模块model层
│  │  │
│  │  ├─jeeagile-admin-service     系统管理模块service层
│  │  │
│  │  └─jeeagile-admin-web         系统管理模块web层
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
│  ├─jeeagile-logger         日志管理模块（包括日志切面和操作日志记录）
│  │  ├─jeeagile-logger-api        日志管理模块api层
│  │  │
│  │  ├─jeeagile-logger-model      日志管理模块model层
│  │  │
│  │  ├─jeeagile-logger-service    日志管理模块service层
│  │  │
│  │  └─jeeagile-logger-web        日志管理模块web层
│  │  
│  ├─jeeagile-monitor        服务端监控模块
│  │  ├─jeeagile-monitor-api       服务监控模块api层
│  │  │
│  │  ├─jeeagile-monitor-model     服务监控模块model层
│  │  │
│  │  ├─jeeagile-monitor-service   服务监控模块service层
│  │  │
│  │  └─jeeagile-monitor-web       服务监控模块web层
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
│  ├─jeeagile-system         系统管理模块
│  │  ├─jeeagile-system-api        系统管理模块api层
│  │  │
│  │  ├─jeeagile-system-model      系统管理模块model层
│  │  │
│  │  ├─jeeagile-system-service    系统管理模块service层
│  │  │
│  │  └─jeeagile-system-web        系统管理模块web层
│  │  
├─jeeagile-plugin      框架第三方插件支持
│  ├─jeeagile-plugin-crypto        加解密插件（包括各种对称、非对称、摘要等加密方式））
│  │
│  ├─jeeagile-plugin-http          http请求插件
│  │
│  └─jeeagile-plugin-redis         redis工具插件（主要包括redis操作静态工具类）
│  
├─jeeagile-security    安全框架支持
│  ├─jeeagile-security-shiro       apache shiro安全框架集成
│  │
│  └─jeeagile-security-spring      spring security安全框架集成
│  
├─jeeagile-ui          前端UI模块（包括登录、系统管理、监控、队列、日志等）
│  
```

#### 项目源码
- github     https://github.com/jeeagile/jeeagile
- gitee      https://gitee.com/jeeagile/jeeagile

#### 特别提醒
为了平台的发展，暂不上传dubbo和rabbitmq组件代码，如有需要可联系作者。
当然只要您按照自定义注解进行模块开发，切换dubbo或者rabbitmq完全没有问题，且会全力支持解决所遇到的任何问题，感谢您的信任和使用。

#### 特别鸣谢

感谢[MybatisPlus](https://mp.baomidou.com/)、[RuoYi](http://www.ruoyi.vip/)、[vue-element-admin](https://panjiachen.github.io/)等优秀开源项目。

#### 项目捐赠
项目的发展离不开你的支持，请作者喝杯咖 吧☕  
![donate](https://images.gitee.com/uploads/images/2021/0620/225853_2fe6a9e3_9223129.png)

#### 反馈交流
- QQ交流群：796838714

