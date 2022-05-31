drop table if exists agile_process_model;
drop table if exists agile_process_form;

/*==============================================================*/
/* table: agile_process_form 流程表单表                         */
/*==============================================================*/
CREATE TABLE agile_process_form (
  id                            varchar(32) NOT NULL COMMENT '表单主键ID',
  form_code                     varchar(20) NOT NULL COMMENT '表单编码',
  form_name                     varchar(100) NOT NULL COMMENT '表单名称',
  form_status                   varchar(1) NOT NULL COMMENT '表单状态',
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
  process_code                  varchar(20) NOT NULL COMMENT '流程编码',
  process_name                  varchar(100) NOT NULL COMMENT '流程名称',
  process_version               int NOT NULL COMMENT '流程版本',
  process_designer              text COMMENT '流程设计XMl',
  process_form_type             varchar(32) NOT NULL COMMENT '流程表单类型',
  process_form_id               varchar(32)  COMMENT '流程表单ID',
  process_form_url              varchar(32) COMMENT '流程表单地址',
  process_deployment_status     varchar(1) DEFAULT '1' COMMENT '流程部署状态',
  process_deployment_time       datetime DEFAULT NULL COMMENT '流程部署时间',
  process_deployment_id         varchar(50) DEFAULT NULL COMMENT '流程部署ID（流程组件生成）',
  remark                        varchar(300) DEFAULT NULL COMMENT '备注',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
)
alter table agile_sys_login comment '流程模型表';