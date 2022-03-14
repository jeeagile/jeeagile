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
   login_module         varchar(150)         not null,
   login_name           varchar(150)         not null,
   login_time           datetime             not null,
   login_ip             varchar(50)          not null,
   login_address        varchar(150)         null,
   login_device         varchar(100)         not null,
   login_os             varchar(100)         not null,
   login_browser        varchar(100)         not null,
   server_address       varchar(50)          not null,
   status               varchar(1)           not null,
   message              text                 null,
   constraint PK_agile_logger_login primary key nonclustered (id)
)
go

if exists (select 1 from  sys.extended_properties
           where major_id = object_id('agile_logger_login') and minor_id = 0)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_login'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '用户登录日志表',
   'user', @CurrentUser, 'table', 'agile_logger_login'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'id')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'id'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '登录日志主键',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'id'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_module')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_module'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '登录类型',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_module'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_name')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_name'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '登录用户名',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_name'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_time')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_time'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '登录时间',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_time'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_ip')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_ip'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作IP',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_ip'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_address')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_address'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '登录地址',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_address'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_device')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_device'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '登录设备名称',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_device'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_os')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_os'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '登录设备操作系统名称',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_os'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_browser')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_browser'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '登录浏览器名称',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'login_browser'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'server_address')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'server_address'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '服务器地址',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'server_address'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'status')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'status'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '登录状态',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'status'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'message')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_login', 'column', 'message'

end


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
   operate_module       varchar(100)         not null,
   operate_notes        varchar(100)         not null,
   operate_type         varchar(10)          not null,
   operate_user         varchar(10)          not null,
   request_uri          varchar(150)         not null,
   request_method       varchar(30)          not null,
   request_param        text                 null,
   response_param       text                 null,
   execute_method       varchar(150)         null,
   operate_ip           varchar(50)          not null,
   operate_address      varchar(150)         null,
   operate_device       varchar(100)         not null,
   operate_os           varchar(100)         not null,
   operate_browser      varchar(100)         not null,
   server_address       varchar(50)          not null,
   execute_time         numeric(10,0)        not null,
   status               varchar(1)           not null,
   message              text                 null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_logger_operate primary key nonclustered (id)
)
go

if exists (select 1 from  sys.extended_properties
           where major_id = object_id('agile_logger_operate') and minor_id = 0)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '用户操作日志表',
   'user', @CurrentUser, 'table', 'agile_logger_operate'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'id')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'id'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '主键',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'id'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_module')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_module'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作模块',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_module'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_notes')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_notes'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作详细描述',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_notes'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_type')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_type'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作类型',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_type'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_user')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_user'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作人员名称',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_user'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'request_uri')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'request_uri'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求uri',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'request_uri'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'request_method')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'request_method'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求方式',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'request_method'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'request_param')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'request_param'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '请求参数',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'request_param'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'response_param')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'response_param'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '返回参数',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'response_param'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'execute_method')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'execute_method'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '执行方法',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'execute_method'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_ip')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_ip'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作ip地址',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_ip'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_address')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_address'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作地址',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_address'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_device')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_device'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作设备名称',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_device'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_os')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_os'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作设备操作系统名称',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_os'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_browser')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_browser'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作浏览器名称',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'operate_browser'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'server_address')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'server_address'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '服务器ip地址',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'server_address'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'execute_time')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'execute_time'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '执行时间(毫秒)',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'execute_time'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'status')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'status'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '日志状态',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'status'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'message')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'message'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '操作信息',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'message'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'create_user')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'create_user'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '创建人',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'create_user'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'create_time')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'create_time'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '创建时间',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'create_time'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'update_user')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'update_user'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '修改人',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'update_user'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_logger_operate')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'update_time')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'update_time'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '修改时间',
   'user', @CurrentUser, 'table', 'agile_logger_operate', 'column', 'update_time'
go
