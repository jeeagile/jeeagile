drop table if exists agile_logger_login;
drop table if exists agile_logger_operate;

/*==============================================================*/
/* table: agile_logger_login 用户登录日志表                      */
/*==============================================================*/
create table agile_logger_login
(
  id                    varchar(32) not null comment '登录日志主键',
  login_name            varchar(150) not null comment '登录用户名',
  login_time            datetime not null comment '登录时间',
  remote_ip             varchar(50) not null comment '操作ip地址',
  remote_location       varchar(150) default null comment '请求地点',
  server_address        varchar(50) not null comment '服务器ip地址',
  device_name           varchar(100) not null comment '请求设备名称',
  os_name               varchar(100) not null comment '请求设备操作系统名称',
  browser_name          varchar(100) not null comment '请求浏览器名称',
  status                varchar(1) not null comment '登录状态（0：成功 1：失败）',
  message               longtext comment '登录信息',
  primary key (id)
);

alter table agile_logger_login comment '用户登录日志表';

/*==============================================================*/
/* table: agile_logger_operate 用户操作日志表                    */
/*==============================================================*/
create table agile_logger_operate
(
  id                    varchar(32) not null comment '操作日志主键',
  title                 varchar(100) not null comment '日志标题',
  type                  varchar(10) not null comment '日志类型',
  user_name             varchar(10) not null comment '操作人员名称',
  req_uri               varchar(150) not null comment '请求uri',
  req_method            varchar(30) not null comment '请求方式',
  req_param             longtext comment '请求参数',
  res_param             longtext comment '返回参数',
  handle_method         varchar(150) default null comment '操作方法',
  remote_ip             varchar(50) not null comment '操作ip地址',
  remote_location       varchar(150) default null comment '请求地点',
  server_address        varchar(50) not null comment '服务器ip地址',
  device_name           varchar(100) not null comment '请求设备名称',
  os_name               varchar(100) not null comment '请求设备操作系统名称',
  browser_name          varchar(100) not null comment '请求浏览器名称',
  execute_time          decimal(10,0) not null comment '执行时间(毫秒)',
  status                varchar(1) not null comment '日志状态（0：成功 1：失败）',
  message               longtext comment '异常信息',
  create_user           varchar(32) default null comment '创建人',
  create_time           datetime default null comment '创建时间',
  update_user           varchar(32) default null comment '修改人',
  update_time           datetime default null comment '修改时间',
  primary key (id)
);

alter table agile_logger_operate comment '用户操作日志表';