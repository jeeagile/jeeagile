drop table if exists agile_quartz_job;
drop table if exists agile_quartz_logger;

/*==============================================================*/
/* table: agile_quartz_job 定时任务表                            */
/*==============================================================*/
create table agile_quartz_job
(
  id                    varchar(32) not null comment '主键',
  job_name              varchar(100) not null comment '任务名称',
  job_code              varchar(100) not null comment '任务编码',
  job_group             varchar(100) not null comment '任务分组',
  job_cron              varchar(150) not null comment 'cron 表达式',
  job_status            char(1) not null comment '状态（0:暂停 1:启用）',
  bean_name             varchar(200) not null comment 'bean名称',
  method_name           varchar(100) not null comment '执行方法',
  method_param          varchar(300) default null comment '执行参数',
  init_misfire          char(1) not null comment '初始策略（0:默认 1:立即触发执行 2:触发一次执行 3:不触发立即执行）',
  concurrent            char(1) not null comment '并发策略（0:允许 1:禁止）',
  remark                varchar(250) default null comment '备注',
  create_user           varchar(32) default null comment '创建人',
  create_time           datetime default null comment '创建时间',
  update_user           varchar(32) default null comment '修改人',
  update_time           datetime default null comment '修改时间',
  primary key (id)
);
alter table agile_quartz_job comment '定时任务表';

INSERT INTO agile_quartz_job VALUES ('1','无参同名方法','001','task','0/20 * * * * ? *','0','agileTask','task','','1','1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_quartz_job VALUES ('2','一个参数同名方法','002','task','0/20 * * * * ? *','0','agileTask','task','66','1','1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_quartz_job VALUES ('3','两个参数同名方法','003','task','0/20 * * * * ? *','0','agileTask','task','88&true','1','1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_quartz_job VALUES ('4','三个参数同名方法（复杂对象）','004','task','0/20 * * * * ? *','0','agileTask','task','66&66.88&{code:33,name:\"agile\",group:\"task\"}','1','1',NULL,NULL,NULL,NULL,NULL);


/*==============================================================*/
/* table: agile_quartz_job_log 定时任务执行日志表                */
/*==============================================================*/
create table agile_quartz_logger
(
  id                    varchar(32) not null comment '主键',
  job_name              varchar(100) not null comment '任务名称',
  job_code              varchar(100) not null comment '任务编码',
  job_group             varchar(100) not null comment '任务分组',
  job_cron              varchar(150) not null comment 'cron 表达式',
  bean_name             varchar(200) not null comment 'bean名称',
  method_name           varchar(100) not null comment '执行方法',
  method_param          varchar(300) default null comment '执行参数',
  start_time            datetime not null comment '开始时间',
  stop_time             datetime not null comment '结束时间',
  execute_time          decimal(10,0) not null comment '执行时间(毫秒)',
  status                varchar(1) not null comment '执行状态（0：成功 1：失败）',
  message               longtext comment '异常信息',
  primary key (id)
);
alter table agile_quartz_logger comment '定时任务执行日志表';
/*==============================================================*/
/* 菜单配置                                                      */
/*==============================================================*/
INSERT INTO agile_sys_menu VALUES ('4','0','任务管理',4,'','quartz','quartz','M','0','0','1','','任务管理目录',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('401','4','定时任务',1,'quartz/job/index','job','job','C','0','0','1','quartz:job:page','定时任务菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40101','401','任务明细',1,'','','#','F','0','0','1','quartz:job:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40102','401','任务新增',2,'','','#','F','0','0','1','quartz:job:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40103','401','任务修改',3,'','','#','F','0','0','1','quartz:job:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40104','401','任务删除',4,'','','#','F','0','0','1','quartz:job:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40105','401','任务导入',5,'','','#','F','0','0','1','quartz:job:import','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40106','401','任务导出',6,'','','#','F','0','0','1','quartz:job:export','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40107','401','任务执行',7,'','','#','F','0','0','1','quartz:job:execute','',NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('402','4','执行日志',1,'quartz/logger/index','quartzLogger','logger','C','0','0','1','job:logger:page','操作日志菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40201','402','查看',1,'','','','F','0','0','1','quartz:logger:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40202','402','删除',2,'','#','#','F','0','0','1','quartz:logger:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40203','402','清空',3,'','','','F','0','0','1','quartz:logger:clear','',NULL,NULL,NULL,NULL);



/*==============================================================*/
/* spring boot quartz 默认表（可到官方自行下载）                 */
/* 官网地址：http://www.quartz-scheduler.org/                   */
/*==============================================================*/