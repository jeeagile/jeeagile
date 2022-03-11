drop table if exists agile_logger_login;
drop table if exists agile_logger_operate;

/*==============================================================*/
/* table: agile_logger_login 用户登录日志表                      */
/*==============================================================*/
create table agile_logger_login
(
  id                     varchar(32) not null comment '登录日志主键',
  login_module           varchar(150) not null comment '登录类型',
  login_name             varchar(150) not null comment '登录用户名',
  login_time             datetime not null comment '登录时间',
  login_ip               varchar(50) not null comment '登录IP',
  login_address          varchar(150) default null comment '登录地址',
  login_device           varchar(100) not null comment '登录设备名称',
  login_os               varchar(100) not null comment '登录设备操作系统名称',
  login_browser          varchar(100) not null comment '登录浏览器名称',
  server_address         varchar(50) not null comment '服务器ip地址',
  status                 varchar(1) not null comment '登录状态（0：成功 1：失败）',
  message                longtext comment '登录信息',
  primary key (id)
);

alter table agile_logger_login comment '用户登录日志表';

/*==============================================================*/
/* table: agile_logger_operate 用户操作日志表                    */
/*==============================================================*/
create table agile_logger_operate
(
  id                    varchar(32) not null comment '操作日志主键',
  operate_module        varchar(100) not null comment '操作模块',
  operate_notes         varchar(100) default null comment '操作详细描述',
  operate_type          varchar(10) not null comment '操作类型',
  operate_user          varchar(10) not null comment '操作人员名称',
  request_uri           varchar(150) not null comment '请求uri',
  request_method        varchar(30) not null comment '请求方式',
  request_param         longtext comment '请求参数',
  response_param        longtext comment '返回参数',
  execute_method        varchar(150) default null comment '执行方法',
  operate_ip            varchar(50) not null comment '操作ip',
  operate_address       varchar(150) default null comment '操作地址',
  operate_device        varchar(100) not null comment '操作设备名称',
  operate_os            varchar(100) not null comment '操作设备操作系统名称',
  operate_browser       varchar(100) not null comment '操作浏览器名称',
  server_address        varchar(50) not null comment '服务器地址',
  execute_time          decimal(10,0) not null comment '执行时间(毫秒)',
  status                varchar(1) not null comment '操作状态（0：成功 1：失败）',
  message               longtext comment '操作信息',
  create_user           varchar(32) default null comment '创建人',
  create_time           datetime default null comment '创建时间',
  update_user           varchar(32) default null comment '修改人',
  update_time           datetime default null comment '修改时间',
  primary key (id)
);

alter table agile_logger_operate comment '用户操作日志表';