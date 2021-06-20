if exists (select 1
            from  sysobjects
           where  id = object_id('agile_logger_login')
            and   type = 'U')
   drop table agile_logger_login
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_logger_operate')
            and   type = 'U')
   drop table agile_logger_operate
go


/*==============================================================*/
/* Table: agile_logger_login 用户登录日志表                      */
/*==============================================================*/
create table agile_logger_login (
   id                   varchar(32)          not null,
   login_name           varchar(150)         not null,
   login_time           datetime             not null,
   remote_ip            varchar(50)          not null,
   remote_location      varchar(150)         null,
   server_address       varchar(50)          not null,
   device_name          varchar(100)         not null,
   os_name              varchar(100)         not null,
   browser_name         varchar(100)         not null,
   status               varchar(1)           not null,
   message              text                 null,
   constraint PK_agile_logger_login primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '用户登录日志表',
   'user', @CurrentUser, 'table', 'agile_logger_login'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '日志id',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '登录用户名',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '登录时间',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作ip地址',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'remote_ip'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求地点',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'remote_location'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '服务器ip地址',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'server_address'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求设备名称',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'device_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求设备操作系统名称',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'os_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求浏览器名称',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'browser_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '登录状态',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'status'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '登录信息',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'message'
go

/*==============================================================*/
/* Table: agile_logger_operate 用户操作日志表                    */
/*==============================================================*/
create table agile_logger_operate (
   id                   varchar(32)          not null,
   title                varchar(100)         not null,
   type                 varchar(10)          not null,
   user_name            varchar(10)          not null,
   req_uri              varchar(150)         not null,
   req_method           varchar(30)          not null,
   req_param            text                 null,
   res_param            text                 null,
   handle_method        varchar(150)         null,
   remote_ip            varchar(50)          not null,
   remote_location      varchar(150)         null,
   server_address       varchar(50)          not null,
   device_name          varchar(100)         not null,
   os_name              varchar(100)         not null,
   browser_name         varchar(100)         not null,
   execute_time         decimal(10,0)        not null,
   status               varchar(1)           not null,
   message              text                 null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_logger_operate primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '用户操作日志表',
   'user', @CurrentUser, 'table', 'agile_logger_operate'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '日志id',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '日志标题',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'title'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '日志类型',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'type'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作人员名称',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'user_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求uri',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'req_uri'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求方式',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'req_method'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求参数',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'req_param'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '返回参数',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'res_param'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作方法',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'handle_method'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作ip地址',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'remote_ip'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求地点',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'remote_location'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '服务器ip地址',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'server_address'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求设备名称',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'device_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求设备操作系统名称',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'os_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求浏览器名称',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'browser_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '执行时间(毫秒)',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'execute_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '日志状态',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'status'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '异常信息',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'message'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '创建人',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '创建时间',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '修改人',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '修改时间',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'update_time'
go
