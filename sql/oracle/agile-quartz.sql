drop table agile_quartz_job cascade constraints;

/*==============================================================*/
/* Table: agile_quartz_job 定时任务表                            */
/*==============================================================*/
create table agile_quartz_job
(
   id                 VARCHAR2(32)         not null,
   job_name           VARCHAR2(100)        not null,
   job_code           VARCHAR2(100)        not null,
   job_cron           VARCHAR2(150)        not null,
   job_status         CHAR(1)              not null,
   bean_name          VARCHAR2(200)        not null,
   method_name        VARCHAR2(100)        not null,
   method_param       VARCHAR2(300),
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
comment on column agile_quartz_job.job_cron is 'cron 表达式';
comment on column agile_quartz_job.job_status is '状态（0:暂停 1:启用）';
comment on column agile_quartz_job.bean_name is 'bean名称';
comment on column agile_quartz_job.method_name is '执行方法';
comment on column agile_quartz_job.method_param is '执行参数';
comment on column agile_quartz_job.remark is '备注';
comment on column agile_quartz_job.create_user is '创建人';
comment on column agile_quartz_job.create_time is '创建时间';
comment on column agile_quartz_job.update_user is '修改人';
comment on column agile_quartz_job.update_time is '修改时间';