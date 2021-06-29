if exists (select 1
            from  sysobjects
           where  id = object_id('agile_quartz_job')
            and   type = 'U')
   drop table agile_quartz_job
go


/*==============================================================*/
/* Table: agile_quartz_job 定时任务表                            */
/*==============================================================*/
create table agile_quartz_job (
   id                   varchar(32)          not null,
   job_name             varchar(100)         not null,
   job_code             varchar(100)         not null,
   job_cron             varchar(150)         not null,
   job_status           char(1)              not null,
   bean_name            varchar(200)         not null,
   method_name          varchar(100)         not null,
   method_param         varchar(300)         null,
   remark               varchar(250)         null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_quartz_job primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '定时任务表',
   'user', @CurrentUser, 'table', 'agile_quartz_job'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '主键',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '任务名称',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'job_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '任务编码',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'job_code'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   'cron 表达式',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'job_cron'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '状态（0:暂停 1:启用）',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'job_status'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   'bean名称',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'bean_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '执行方法',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'method_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '执行参数',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'method_param'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '备注',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'remark'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '创建人',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '创建时间',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '修改人',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '修改时间',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'update_time'
go
