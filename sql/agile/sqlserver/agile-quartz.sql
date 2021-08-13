if exists (select 1
            from  sysobjects
           where  id = object_id('agile_quartz_job')
            and   type = 'U')
   drop table agile_quartz_job
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_quartz_job_logger')
            and   type = 'U')
   drop table agile_quartz_job_logger
go

/*==============================================================*/
/* Table: agile_quartz_job 定时任务表                            */
/*==============================================================*/
create table agile_quartz_job (
   id                   varchar(32)          not null,
   job_name             varchar(100)         not null,
   job_code             varchar(100)         not null,
   job_group            varchar(100)         not null,
   job_cron             varchar(150)         not null,
   job_status           char(1)              not null,
   bean_name            varchar(200)         not null,
   method_name          varchar(100)         not null,
   method_param         varchar(300)         null,
   init_misfire         char(1)              not null,
   concurrent           char(1)              not null,
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
   '任务分组',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'job_group'
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
   '初始策略（0:默认 1:立即触发执行 2:触发一次执行 3:不触发立即执行）',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'init_misfire'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '并发策略（0:允许 1:禁止）',
   'user', @CurrentUser, 'table', 'agile_quartz_job', 'column', 'concurrent'
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

INSERT INTO agile_quartz_job VALUES ('1','无参同名方法','001','task','0/20 * * * * ? *','0','agileTask','task','','1','1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_quartz_job VALUES ('2','一个参数同名方法','002','task','0/20 * * * * ? *','0','agileTask','task','66','1','1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_quartz_job VALUES ('3','两个参数同名方法','003','task','0/20 * * * * ? *','0','agileTask','task','88&true','1','1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_quartz_job VALUES ('4','三个参数同名方法（复杂对象）','004','task','0/20 * * * * ? *','0','agileTask','task','66&66.88&{code:33,name:\"agile\",group:\"task\"}','1','1',NULL,NULL,NULL,NULL,NULL);

/*==============================================================*/
/* Table: agile_quartz_job_logger 任务执行日志表                 */
/*==============================================================*/
create table agile_quartz_job_logger (
   id                   varchar(32)          not null,
   job_name             varchar(100)         not null,
   job_code             varchar(100)         not null,
   job_group            varchar(100)         not null,
   job_cron             varchar(150)         not null,
   bean_name            varchar(200)         not null,
   method_name          varchar(100)         not null,
   method_param         varchar(300)         null,
   start_time           datetime             not null,
   end_time             datetime             not null,
   execute_time         decimal(10,0)        not null,
   status               varchar(1)           not null,
   message              text                 null,
   constraint PK_agile_quartz_job_logger primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '任务执行日志表',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '主键',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '任务名称',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger', 'column', 'job_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '任务编码',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger', 'column', 'job_code'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '任务分组',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger', 'column', 'job_group'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   'cron 表达式',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger', 'column', 'job_cron'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   'bean名称',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger', 'column', 'bean_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '执行方法',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger', 'column', 'method_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '执行参数',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger', 'column', 'method_param'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '开始时间',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger', 'column', 'start_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '结束时间',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger', 'column', 'end_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '执行时间',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger', 'column', 'execute_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '执行状态',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger', 'column', 'status'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '异常信息',
   'user', @CurrentUser, 'table', 'agile_quartz_job_logger', 'column', 'message'
go

/*==============================================================*/
/* spring boot quartz 默认表（可到官方自行下载）                 */
/* 官网地址：http://www.quartz-scheduler.org/                   */
/*==============================================================*/