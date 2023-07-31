drop table if exists agile_online_dict;
drop table if exists agile_online_form;
drop table if exists agile_online_table;
drop table if exists agile_online_column;

/*==============================================================*/
/* table: agile_online_dict 字典类型表                          */
/*==============================================================*/
CREATE TABLE agile_online_dict (
  id                            varchar(32) NOT NULL COMMENT '字典主键ID',
  dict_name                     varchar(100) NOT NULL COMMENT '字典名称',
  dict_type                     varchar(2) NOT NULL COMMENT '字典类型（01:数据表字典 02:系统管理字典 99:自定义字典）',
  system_dict_type              varchar(100) DEFAULT NULL COMMENT '系统字典类型',
  table_name                    varchar(50) DEFAULT NULL COMMENT '字典表名称',
  tree_flag                     varchar(1) DEFAULT NULL COMMENT '字典表树形标识',
  parent_key_column_name        varchar(50) DEFAULT NULL COMMENT '字典表父字段名称',
  key_column_name               varchar(50) DEFAULT NULL COMMENT '字典表键字段名称',
  value_column_name             varchar(50) DEFAULT NULL COMMENT '字典值字段名称',
  label_column_name             varchar(50) DEFAULT NULL COMMENT '字典标签字段名称',
  dict_data_json                text COMMENT '字典数据JSON',
  dict_param_json               text COMMENT '字典参数JSON',
  dict_filter_json              text COMMENT '字典过滤JSON',
  remark                        varchar(300) DEFAULT NULL COMMENT '备注',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
) ;
alter table agile_online_dict comment '字典类型表';

/*==============================================================*/
/* table: agile_online_form 在线表单信息表                       */
/*==============================================================*/
CREATE TABLE agile_online_form (
  id                            varchar(32) NOT NULL COMMENT '表单主键ID',
  form_code                     varchar(20) NOT NULL COMMENT '表单编码',
  form_name                     varchar(100) NOT NULL COMMENT '表单名称',
  form_type                     varchar(2) NOT NULL COMMENT '表单类型（01:业务表单 02:流程表单）',
  form_status                   varchar(2) NOT NULL COMMENT '表单状态（01:编辑基础信息 02：编辑数据模型 03：表单页面设计）',
  publish_status                varchar(2) DEFAULT '02' COMMENT '发布状态（01:已发布 02:未发布）',
  jdbc_id                       varchar(32) DEFAULT NULL COMMENT '数据源ID',
  remark                        varchar(300) DEFAULT NULL COMMENT '备注',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
);
alter table agile_online_form comment '在线表单信息表';

/*==============================================================*/
/* table: agile_online_table 在线表单数据表                      */
/*==============================================================*/
CREATE TABLE agile_online_table (
  id                            varchar(32) NOT NULL COMMENT '数据表主键id',
  form_id                       varchar(32) NOT NULL COMMENT '在线表单主键ID',
  table_name                    varchar(100) NOT NULL COMMENT '数据表名称',
  table_label                   varchar(100) NOT NULL COMMENT '数据表描述',
  table_type                    varchar(2) NOT NULL COMMENT '数据表类型（01:数据主表 02:一对一从表 03:一对多从表）',
  model_name                    varchar(100) NOT NULL COMMENT '数据模型名称',
  master_table_id               varchar(32) DEFAULT NULL COMMENT '主表ID',
  master_column_id              varchar(32) DEFAULT NULL COMMENT '主表字段ID',
  master_column_name            varchar(50) DEFAULT NULL COMMENT '主表字段名称',
  slave_column_id               varchar(32) DEFAULT NULL COMMENT '从表字段ID',
  slave_column_name             varchar(50) DEFAULT NULL COMMENT '从表字段称',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
);
alter table agile_online_table comment '在线表单 数据表';



/*==============================================================*/
/* table: agile_online_column 在线表单数据表字段                 */
/*==============================================================*/
CREATE TABLE agile_online_column (
  id                            varchar(32) NOT NULL COMMENT '数据表主键id',
  form_id                       varchar(32) NOT NULL COMMENT '在线表单主键ID',
  table_id                      varchar(32) NOT NULL COMMENT '在线表单数据表主键',
  column_name                   varchar(100) NOT NULL COMMENT '字段名称',
  column_comment                varchar(200) NOT NULL COMMENT '字段描述',
  column_sort                   int NOT NULL COMMENT '字段排序',
  column_nullable               char(1) NOT NULL COMMENT '字段必填标识（0:否 1:是）',
  column_length                 int DEFAULT NULL COMMENT '字段长度',
  column_precision              int DEFAULT NULL COMMENT '字段精度',
  column_default                varchar(50) DEFAULT NULL COMMENT '字段默认值',
  column_extra                  varchar(200) DEFAULT NULL COMMENT '字段扩展',
  column_type                   varchar(50) NOT NULL COMMENT '字段类型',
  data_type                     varchar(20) NOT NULL COMMENT '数据类型',
  primary_flag                  char(1) NOT NULL COMMENT '主键标识（0:否 1:是）',
  primary_type                  varchar(20) DEFAULT NULL COMMENT '主键类型',
  field_name                    varchar(100) NOT NULL COMMENT '数据对象数据名称',
  field_type                    varchar(50) NOT NULL COMMENT '数据对象数据类型',
  field_label                   varchar(100) NOT NULL COMMENT '数据对象显示标签',
  field_classify                varchar(2) DEFAULT NULL COMMENT '数据对象字段分类',
  filter_type                   varchar(2) NOT NULL DEFAULT '01' COMMENT '字段过滤类型',
  dict_id                       varchar(32) DEFAULT NULL COMMENT '字典ID',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
);
alter table agile_online_column comment '在线表单 数据表字段';


/*==============================================================*/
/* table: agile_online_page 在线表单 页面管理                    */
/*==============================================================*/
CREATE TABLE agile_online_page (
  id                            varchar(32) NOT NULL COMMENT '页面主键ID',
  form_id                       varchar(32) NOT NULL COMMENT '表单主键ID',
  table_id                      varchar(32) NOT NULL COMMENT '数据表主键ID',
  page_code                     varchar(20) NOT NULL COMMENT '页面编码',
  page_name                     varchar(100) NOT NULL COMMENT '页面名称',
  page_kind                     varchar(2) NOT NULL COMMENT '页面类别',
  page_type                     varchar(2) NOT NULL COMMENT '页面类型',
  widget_json                   text COMMENT '页面组件JSON',
  param_json                    text COMMENT '页面参数JSON',
  remark                        varchar(300) DEFAULT NULL COMMENT '备注',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
);
alter table agile_online_page comment '在线表单 表单页面';

INSERT INTO agile_sys_menu VALUES ('5', '0', '在线表单', '5', '', 'online', 'online', 'M', '0', '0', '1', '', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('501','5','字典管理',1,'online/dict/index','dict','dict','C','0','0','1','online:dict:page','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50101','501','字典明细',1,'','','#','F','0','0','1','online:dict:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50102','501','新增字典',2,'','','#','F','0','0','1','online:dict:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50103','501','修改字典',3,'','','#','F','0','0','1','online:dict:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50104','501','删除字典',4,'','','#','F','0','0','1','online:dict:delete','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('502','5','表单管理',1,'online/form/index','form','form','C','0','0','1','online:form:page','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50201','502','表单明细',1,'','','#','F','0','0','1','online:form:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50202','502','新增表单',2,'','','#','F','0','0','1','online:form:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50203','502','修改表单',3,'','','#','F','0','0','1','online:form:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50204','502','删除表单',4,'','','#','F','0','0','1','online:form:delete','',NULL,NULL,NULL,NULL);
