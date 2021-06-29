drop table if exists agile_quartz_job;

/*==============================================================*/
/* table: agile_quartz_job 定时任务表                            */
/*==============================================================*/
create table agile_quartz_job
(
  id                    varchar(32) not null comment '主键',
  job_name              varchar(100) not null comment '任务名称',
  job_code              varchar(100) not null comment '任务编码',
  job_cron              varchar(150) not null comment 'cron 表达式',
  job_status            char(1) not null comment '状态（0:暂停 1:启用）',
  bean_name             varchar(200) not null comment 'bean名称',
  method_name           varchar(100) not null comment '执行方法',
  method_param          varchar(300) default null comment '执行参数',
  remark                varchar(250) default null comment '备注',
  create_user           varchar(32) default null comment '创建人',
  create_time           datetime default null comment '创建时间',
  update_user           varchar(32) default null comment '修改人',
  update_time           datetime default null comment '修改时间',
  primary key (id)
);
alter table agile_quartz_job comment '定时任务表';
