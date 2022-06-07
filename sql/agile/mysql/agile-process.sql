drop table if exists agile_process_model;
drop table if exists agile_process_form;
drop table if exists agile_process_definition;

/*==============================================================*/
/* table: agile_process_form 流程表单表                          */
/*==============================================================*/
CREATE TABLE agile_process_form (
  id                            varchar(32) NOT NULL COMMENT '表单主键ID',
  form_code                     varchar(20) NOT NULL COMMENT '表单编码',
  form_name                     varchar(100) NOT NULL COMMENT '表单名称',
  form_status                   varchar(1) NOT NULL COMMENT '表单状态（0:正常 1:停用）',
  form_config                   text COMMENT '表单配置',
  form_fields                   text COMMENT '表单字段',
  remark                        varchar(300) DEFAULT NULL COMMENT '备注',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
) 

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
  form_url                      varchar(32) COMMENT '流程表单地址',
  deployment_status             varchar(1) DEFAULT '2' COMMENT '流程部署状态（1:已发布 2:未发布）',
  deployment_time               datetime DEFAULT NULL COMMENT '流程部署时间',
  deployment_id                 varchar(50) DEFAULT NULL COMMENT '流程部署ID（流程组件生成）',
  remark                        varchar(300) DEFAULT NULL COMMENT '备注',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
)
alter table agile_sys_login comment '流程模型表';

/*==============================================================*/
/* table: agile_process_definition 流程定义表                   */
/*==============================================================*/
CREATE TABLE agile_process_definition (
  id                            varchar(32) NOT NULL COMMENT '流程定义主键ID',
  model_id                      varchar(32) NOT NULL COMMENT '流程模型主键ID',
  model_code                    varchar(20) NOT NULL COMMENT '流程模型编码',
  model_name                    varchar(100) NOT NULL COMMENT '流程流程名称',
  model_version                 int NOT NULL COMMENT '流程模型版本',
  model_xml                     text COMMENT '流程模型设计XMl',
  form_type                     varchar(32) NOT NULL COMMENT '流程表单类型',
  form_conf                     text COMMENT '流程表单配置',
  form_fields                   text COMMENT '流程表单字段',
  form_url                      varchar(32) DEFAULT NULL COMMENT '流程表单地址',
  deployment_id                 varchar(50) DEFAULT NULL COMMENT '流程部署ID（流程组件生成）',
  definition_id                 varchar(100) DEFAULT NULL COMMENT '流程定义ID（流程组件生成）',
  deployment_time               datetime DEFAULT NULL COMMENT '流程部署时间',
  suspension_state              int NOT NULL COMMENT '挂起状态',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
)

alter table agile_process_definition comment '流程定义表';