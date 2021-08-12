drop table agile_quartz_job cascade constraints;
drop table agile_quartz_job_logger cascade constraints;
/*==============================================================*/
/* Table: agile_quartz_job 定时任务表                            */
/*==============================================================*/
create table agile_quartz_job
(
   id                 VARCHAR2(32)         not null,
   job_name           VARCHAR2(100)        not null,
   job_code           VARCHAR2(100)        not null,
   job_group          VARCHAR2(100)        not null,
   job_cron           VARCHAR2(150)        not null,
   job_status         CHAR(1)              not null,
   bean_name          VARCHAR2(200)        not null,
   method_name        VARCHAR2(100)        not null,
   method_param       VARCHAR2(300),
   init_misfire       CHAR(1)              not null,
   concurrent         char(1)              not null,
   remark             VARCHAR2(250),
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_quartz_job primary key (id)
);

comment on table agile_quartz_job is '定时任务表';

comment on column agile_quartz_job.id is '主键';
comment on column agile_quartz_job.job_name is '任务名称';
comment on column agile_quartz_job.job_code is '任务编码';
comment on column agile_quartz_job.job_group is '任务分组';
comment on column agile_quartz_job.job_cron is 'cron 表达式';
comment on column agile_quartz_job.job_status is '状态（0:暂停 1:启用）';
comment on column agile_quartz_job.bean_name is 'bean名称';
comment on column agile_quartz_job.method_name is '执行方法';
comment on column agile_quartz_job.method_param is '执行参数';
comment on column agile_quartz_job.init_misfire is '初始策略（0:默认 1:立即触发执行 2:触发一次执行 3:不触发立即执行）';
comment on column agile_quartz_job.concurrent is '并发策略（0:允许 1:禁止）';
comment on column agile_quartz_job.remark is '备注';
comment on column agile_quartz_job.create_user is '创建人';
comment on column agile_quartz_job.create_time is '创建时间';
comment on column agile_quartz_job.update_user is '修改人';
comment on column agile_quartz_job.update_time is '修改时间';


/*==============================================================*/
/* table: agile_quartz_job_log 定时任务执行日志表                */
/*==============================================================*/
create table agile_quartz_job_logger
(
   id                 VARCHAR2(32)         not null,
   job_name           VARCHAR2(100)        not null,
   job_code           VARCHAR2(100)        not null,
   job_group          VARCHAR2(100)        not null,
   job_cron           VARCHAR2(150)        not null,
   bean_name          VARCHAR2(200)        not null,
   method_name        VARCHAR2(100)        not null,
   method_param       VARCHAR2(300),
   start_time         DATE,                not null,
   stop_time          DATE,                not null,
   execute_time       NUMBER(10,0)         not null,
   status             VARCHAR2(1)          not null,
   message            CLOB,
   constraint PK_agile_quartz_job_logger primary key (id)
);

comment on table agile_quartz_job is '定时任务执行日志表';

comment on column agile_quartz_job_logger.id is '主键';
comment on column agile_quartz_job_logger.job_name is '任务名称';
comment on column agile_quartz_job_logger.job_code is '任务编码';
comment on column agile_quartz_job_logger.job_group is '任务分组';
comment on column agile_quartz_job_logger.job_cron is 'cron 表达式';
comment on column agile_quartz_job_logger.bean_name is 'bean名称';
comment on column agile_quartz_job_logger.method_name is '执行方法';
comment on column agile_quartz_job_logger.method_param is '执行参数';
comment on column agile_quartz_job.start_time is '开始时间';
comment on column agile_quartz_job.stop_time is '结束时间';
comment on column agile_quartz_job.execute_time is '执行时间';
comment on column agile_quartz_job.status is '执行状态';
comment on column agile_quartz_job.message is '执行状态（0：成功 1：失败）';


/*==============================================================*/
/* spring boot quartz 默认表（可到官方自行下载）                 */
/*==============================================================*/