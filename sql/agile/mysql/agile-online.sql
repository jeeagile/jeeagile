drop table if exists agile_online_dict;

CREATE TABLE agile_online_dict (
  id                            varchar(32) NOT NULL COMMENT '字典主键ID',
  dict_name                     varchar(100) NOT NULL COMMENT '字典名称',
  dict_type                     varchar(2) NOT NULL COMMENT '字典类型（01:数据表字典 02:系统管理字典 03:系统静态字典 99:自定义字典）',
  system_dict_type              varchar(100) DEFAULT NULL COMMENT '系统字典类型',
  table_name                    varchar(50) DEFAULT NULL COMMENT '字典表名称',
  table_tree_flag               varchar(1) DEFAULT NULL COMMENT '字典表树形标识',
  parent_key_column_name        varchar(50) DEFAULT NULL COMMENT '字典表父字段名称',
  key_column_name               varchar(50) DEFAULT NULL COMMENT '字典表键字段名称',
  value_column_name             varchar(50) DEFAULT NULL COMMENT '字典值字段名称',
  label_column_name             varchar(50) DEFAULT NULL COMMENT '字典标签字段名称',
  dict_data_json                text COMMENT '字典数据JSON',
  dict_param_json               text COMMENT '字段参数JSON',
  remark                        varchar(300) DEFAULT NULL COMMENT '备注',
  create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
  create_time                   datetime DEFAULT NULL COMMENT '创建时间',
  update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
  update_time                   datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
) ;

alter table agile_process_expression comment '在线表单 字典类型表';

