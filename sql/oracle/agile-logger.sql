drop table agile_logger_login cascade constraints;
drop table agile_logger_operate cascade constraints;

/*==============================================================*/
/* Table: agile_logger_login 用户登录日志表                      */
/*==============================================================*/
create table agile_logger_login
(
   id                 VARCHAR2(32)         not null,
   login_name         VARCHAR2(150)        not null,
   login_time         DATE                 not null,
   remote_ip          VARCHAR2(50)         not null,
   remote_location    VARCHAR2(150),
   server_address     VARCHAR2(50)         not null,
   device_name        VARCHAR2(100)        not null,
   os_name            VARCHAR2(100)        not null,
   browser_name       VARCHAR2(100)        not null,
   status             VARCHAR2(1)          not null,
   message            CLOB,
   constraint PK_agile_logger_login primary key (id)
);

comment on table agile_logger_login is '用户登录日志表';

comment on column agile_logger_login.id is '日志id';
comment on column agile_logger_login.login_name is '登录用户名';
comment on column agile_logger_login.login_time is '登录时间';
comment on column agile_logger_login.remote_ip is '操作ip地址';
comment on column agile_logger_login.remote_location is '请求地点';
comment on column agile_logger_login.server_address is '服务器ip地址';
comment on column agile_logger_login.device_name is '请求设备名称';
comment on column agile_logger_login.os_name is '请求设备操作系统名称';
comment on column agile_logger_login.browser_name is '请求浏览器名称';
comment on column agile_logger_login.status is '登录状态';
comment on column agile_logger_login.message is '登录信息';

/*==============================================================*/
/* Table: agile_logger_operate 用户操作日志表                    */
/*==============================================================*/
create table agile_logger_operate
(
   id                 VARCHAR2(32)         not null,
   title              VARCHAR2(100)        not null,
   type               VARCHAR2(10)         not null,
   user_name          VARCHAR2(10)         not null,
   req_uri            VARCHAR2(150)        not null,
   req_method         VARCHAR2(30)         not null,
   req_param          CLOB,
   res_param          CLOB,
   handle_method      VARCHAR2(150),
   remote_ip          VARCHAR2(50)         not null,
   remote_location    VARCHAR2(150),
   server_address     VARCHAR2(50)         not null,
   device_name        VARCHAR2(100)        not null,
   os_name            VARCHAR2(100)        not null,
   browser_name       VARCHAR2(100)        not null,
   execute_time       NUMBER(10,0)         not null,
   status             VARCHAR2(1)          not null,
   message            CLOB,
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_logger_operate primary key (id)
);

comment on table agile_logger_operate is '用户操作日志表';

comment on column agile_logger_operate.id is '日志id';

comment on column agile_logger_operate.title is '日志标题';

comment on column agile_logger_operate.type is '日志类型';
comment on column agile_logger_operate.user_name is '操作人员名称';
comment on column agile_logger_operate.req_uri is '请求uri';
comment on column agile_logger_operate.req_method is '请求方式';
comment on column agile_logger_operate.req_param is '请求参数';
comment on column agile_logger_operate.res_param is '返回参数';
comment on column agile_logger_operate.handle_method is '操作方法';
comment on column agile_logger_operate.remote_ip is '操作ip地址';
comment on column agile_logger_operate.remote_location is '请求地点';
comment on column agile_logger_operate.server_address is '服务器ip地址';
comment on column agile_logger_operate.device_name is '请求设备名称';
comment on column agile_logger_operate.os_name is '请求设备操作系统名称';
comment on column agile_logger_operate.browser_name is '请求浏览器名称';
comment on column agile_logger_operate.execute_time is '执行时间(毫秒)';
comment on column agile_logger_operate.status is '日志状态';
comment on column agile_logger_operate.message is '异常信息';
comment on column agile_logger_operate.create_user is '创建人';
comment on column agile_logger_operate.create_time is '创建时间';
comment on column agile_logger_operate.update_user is '修改人';
comment on column agile_logger_operate.update_time is '修改时间';
