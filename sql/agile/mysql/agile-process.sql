drop table if exists agile_process_form;
drop table if exists agile_process_model;
drop table if exists agile_process_definition;
drop table if exists agile_process_instance;
drop table if exists agile_process_task;
/*==============================================================*/
/* table: agile_process_form 流程表单表                          */
/*==============================================================*/
CREATE TABLE agile_process_form (
  id                            varchar(32) NOT NULL COMMENT '表单主键ID',
  form_code                     varchar(20) NOT NULL COMMENT '表单编码',
  form_name                     varchar(100) NOT NULL COMMENT '表单名称',
  form_status                   varchar(1) NOT NULL COMMENT '表单状态（0:正常 1:停用）',
  form_conf                     text COMMENT '表单配置',
  form_fields                   text COMMENT '表单字段',
  remark                        varchar(300) DEFAULT NULL COMMENT '备注',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
);

alter table agile_process_form comment '流程表单表';

/*==============================================================*/
/* table: agile_process_model 流程模型表                        */
/*==============================================================*/
CREATE TABLE agile_process_model (
  id                            varchar(32) NOT NULL COMMENT '流程模型主键ID',
  model_code                    varchar(20) NOT NULL COMMENT '流程模型编码',
  model_name                    varchar(100) NOT NULL COMMENT '流程模型名称',
  model_version                 int NOT NULL COMMENT '流程模型版本',
  model_xml                     text COMMENT '流程模型设计XMl',
  form_type                     varchar(32) NOT NULL COMMENT '流程表单类型（1:流程表单 2:业务表单）',
  form_id                       varchar(32)  COMMENT '流程表单ID',
  form_name                     varchar(100) COMMENT '流程表单名称',
  form_url                      varchar(32) COMMENT '流程表单地址',
  deployment_status             varchar(1) DEFAULT '2' COMMENT '流程部署状态（1:已发布 2:未发布）',
  deployment_time               datetime DEFAULT NULL COMMENT '流程部署时间',
  remark                        varchar(300) DEFAULT NULL COMMENT '备注',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
);
alter table agile_process_model comment '流程模型表';

/*==============================================================*/
/* table: agile_process_definition 流程定义表                   */
/*==============================================================*/
CREATE TABLE agile_process_definition (
  id                            varchar(100) NOT NULL COMMENT '流程定义主键ID(流程组件生成)',
  model_id                      varchar(32) NOT NULL COMMENT '流程模型主键ID',
  model_code                    varchar(20) NOT NULL COMMENT '流程模型编码',
  model_name                    varchar(100) NOT NULL COMMENT '流程流程名称',
  model_version                 int NOT NULL COMMENT '流程模型版本',
  model_xml                     text COMMENT '流程模型设计XMl',
  form_type                     varchar(32) NOT NULL COMMENT '流程表单类型',
  form_name                     varchar(100) NOT NULL COMMENT '表单名称',
  form_conf                     text COMMENT '流程表单配置',
  form_fields                   text COMMENT '流程表单字段',
  form_url                      varchar(32) DEFAULT NULL COMMENT '流程表单地址',
  main_version                  int NOT NULL COMMENT '流程定义主版本（1：主版本 2：非主版本）',
  deployment_time               datetime DEFAULT NULL COMMENT '流程部署时间',
  suspension_state              int NOT NULL COMMENT '挂起状态',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
);
alter table agile_process_definition comment '流程定义表';


/*==============================================================*/
/* table: agile_process_instance 流程实例表                      */
/*==============================================================*/
CREATE TABLE agile_process_instance (
  id                            varchar(100) NOT NULL COMMENT '流程实例主键ID（流程组件生成）',
  model_id                      varchar(32) NOT NULL COMMENT '流程模型主键ID',
  model_code                    varchar(20) NOT NULL COMMENT '流程模型编码',
  model_name                    varchar(100) NOT NULL COMMENT '流程流程名称',
  model_version                 int NOT NULL COMMENT '流程模型版本',
  model_xml                     text COMMENT '流程模型设计XMl',
  form_type                     varchar(32) NOT NULL COMMENT '流程表单类型',
  form_name                     varchar(100) NOT NULL COMMENT '表单名称',
  form_conf                     text COMMENT '流程表单配置',
  form_fields                   text COMMENT '流程表单字段',
  form_data                     text COMMENT '流程表单数据',
  form_url                      varchar(32) DEFAULT NULL COMMENT '流程表单地址',
  deployment_time               datetime DEFAULT NULL COMMENT '流程部署时间',
  definition_id                 varchar(100) DEFAULT NULL COMMENT '流程定义ID',
  instance_status               varchar(2) DEFAULT NULL COMMENT '流程实例状态（1：正在办理中）',
  suspension_state              int NOT NULL COMMENT '挂起状态',
  start_user                    varchar(32) NOT NULL COMMENT '流程实例发起人',
  start_user_name               varchar(100) NOT NULL COMMENT '流程发起人名称',
  start_time                    datetime DEFAULT NULL COMMENT '流程实例启动时间',
  end_time                      datetime DEFAULT NULL COMMENT '流程实例结束时间',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
);
alter table agile_process_instance comment '流程实例表';



/*==============================================================*/
/* table: agile_process_task 流程任务表                         */
/*==============================================================*/
CREATE TABLE agile_process_task (
  id                            varchar(100) NOT NULL COMMENT '流程任务主键ID(流程组件生成)',
  model_id                      varchar(32) NOT NULL COMMENT '流程模型主键ID',
  model_code                    varchar(20) NOT NULL COMMENT '流程模型编码',
  model_name                    varchar(100) NOT NULL COMMENT '流程流程名称',
  form_name                     varchar(100) NOT NULL COMMENT '表单名称',
  definition_id                 varchar(100) DEFAULT NULL COMMENT '流程定义ID',
  instance_id                   varchar(100) DEFAULT NULL COMMENT '流程实例ID',
  task_name                     varchar(100) DEFAULT NULL COMMENT '流程任务名称',
  task_user                     varchar(32) NOT NULL COMMENT '流程任务执行人',
  task_user_name               varchar(100) NOT NULL COMMENT '流程任务执行人名称',
  task_status                   varchar(2) NOT NULL COMMENT '流程任务状态(0:已撤销 1:办理中 2：已完成)',
  start_user                    varchar(32) NOT NULL COMMENT '流程发起人',
  start_user_name               varchar(100) NOT NULL COMMENT '流程发起人名称',
  start_time                    datetime DEFAULT NULL COMMENT '流程任务启动时间',
  end_time                      datetime DEFAULT NULL COMMENT '流程任务结束时间',
  approve_message               varchar(500) DEFAULT NULL COMMENT '审批意见',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
);
alter table agile_process_task comment '流程任务表';