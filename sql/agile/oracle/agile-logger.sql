drop table agile_logger_login cascade constraints;
drop table agile_logger_operate cascade constraints;

/*==============================================================*/
/* Table: agile_logger_login 用户登录日志表                      */
/*==============================================================*/
create table "agile_logger_login"
(
   "id"                 VARCHAR2(32)         not null,
   "login_module"       VARCHAR2(150)        not null,
   "login_name"         VARCHAR2(150)        not null,
   "login_time"         DATE                 not null,
   "login_ip"           VARCHAR2(50)         not null,
   "login_address"      VARCHAR2(150),
   "login_device"       VARCHAR2(100)        not null,
   "login_os"           VARCHAR2(100)        not null,
   "login_browser"      VARCHAR2(100)        not null,
   "server_address"     VARCHAR2(50)         not null,
   "status"             VARCHAR2(1)          not null,
   "message"            CLOB,
   constraint "PK_agile_logger_login" primary key ("id")
);

comment on table "agile_logger_login" is '用户登录日志表';
comment on column "agile_logger_login"."id" is '登录日志主键';
comment on column "agile_logger_login"."login_module" is '登录类型';
comment on column "agile_logger_login"."login_name" is '登录用户名';
comment on column "agile_logger_login"."login_time" is '登录时间';
comment on column "agile_logger_login"."login_ip" is '操作IP';
comment on column "agile_logger_login"."login_address" is '登录地址';
comment on column "agile_logger_login"."login_device" is '登录设备名称';
comment on column "agile_logger_login"."login_os" is '登录设备操作系统名称';
comment on column "agile_logger_login"."login_browser" is '登录浏览器名称';
comment on column "agile_logger_login"."server_address" is '服务器地址';
comment on column "agile_logger_login"."status" is '登录状态';
comment on column "agile_logger_login"."message" is '登录信息';

/*==============================================================*/
/* Table: agile_logger_operate 用户操作日志表                    */
/*==============================================================*/
create table "agile_logger_operate"
(
   "id"                 VARCHAR2(32)         not null,
   "operate_module"     VARCHAR2(100)        not null,
   "operate_notes"      VARCHAR2(100)        not null,
   "operate_type"       VARCHAR2(10)         not null,
   "operate_user"       VARCHAR2(10)         not null,
   "request_uri"        VARCHAR2(150)        not null,
   "request_method"     VARCHAR2(30)         not null,
   "request_param"      CLOB,
   "response_param"     CLOB,
   "execute_method"     VARCHAR2(150),
   "operate_ip"         VARCHAR2(50)         not null,
   "operate_address"    VARCHAR2(150),
   "operate_device"     VARCHAR2(100)        not null,
   "operate_os"         VARCHAR2(100)        not null,
   "operate_browser"    VARCHAR2(100)        not null,
   "server_address"     VARCHAR2(50)         not null,
   "execute_time"       NUMBER(10,0)         not null,
   "status"             VARCHAR2(1)          not null,
   "message"            CLOB,
   "create_user"        VARCHAR2(32),
   "create_time"        DATE,
   "update_user"        VARCHAR2(32),
   "update_time"        DATE,
   constraint "PK_agile_logger_operate" primary key ("id")
);

comment on table "agile_logger_operate" is '用户操作日志表';

comment on column "agile_logger_operate"."id" is '主键';
comment on column "agile_logger_operate"."operate_module" is '操作模块';
comment on column "agile_logger_operate"."operate_notes" is '操作详细描述';
comment on column "agile_logger_operate"."operate_type" is '操作类型';
comment on column "agile_logger_operate"."operate_user" is '操作人员名称';
comment on column "agile_logger_operate"."request_uri" is '请求uri';
comment on column "agile_logger_operate"."request_method" is '请求方式';
comment on column "agile_logger_operate"."request_param" is '请求参数';
comment on column "agile_logger_operate"."response_param" is '返回参数';
comment on column "agile_logger_operate"."execute_method" is '执行方法';
comment on column "agile_logger_operate"."operate_ip" is '操作ip地址';
comment on column "agile_logger_operate"."operate_address" is '操作地址';
comment on column "agile_logger_operate"."operate_device" is '操作设备名称';
comment on column "agile_logger_operate"."operate_os" is '操作设备操作系统名称';
comment on column "agile_logger_operate"."operate_browser" is '操作浏览器名称';
comment on column "agile_logger_operate"."server_address" is '服务器ip地址';
comment on column "agile_logger_operate"."execute_time" is '执行时间(毫秒)';
comment on column "agile_logger_operate"."status" is '日志状态';
comment on column "agile_logger_operate"."message" is '操作信息';
comment on column "agile_logger_operate"."create_user" is '创建人';
comment on column "agile_logger_operate"."create_time" is '创建时间';
comment on column "agile_logger_operate"."update_user" is '修改人';
comment on column "agile_logger_operate"."update_time" is '修改时间';
