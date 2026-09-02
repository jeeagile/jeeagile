drop table if exists agile_drools_model;
drop table if exists agile_drools_model_field;
drop table if exists agile_drools_rule;
drop table if exists agile_drools_rule_model;
drop table if exists agile_drools_scene;
drop table if exists agile_drools_scene_rule;
/*==============================================================*/
/* table: agile_drools_model 规则引擎 数据对象                     */
/*==============================================================*/
CREATE TABLE agile_drools_model (
    id varchar(32)  NOT NULL COMMENT '对象主键id',
    model_name varchar(100)  NOT NULL COMMENT '对象名称',
    model_label varchar(100)  NOT NULL COMMENT '对象标签',
    model_type varchar(20)  NOT NULL COMMENT '对象类型（java、declare）',
    model_package varchar(300)  NOT NULL COMMENT '对象包名',
    super_model varchar(300)  DEFAULT NULL COMMENT '父级对象',
    model_status varchar(2)  DEFAULT '0' COMMENT '对象状态',
    input_flag varchar(2)  DEFAULT '1' COMMENT '入参标识（1：是 0：否）',
    output_flag varchar(2)  DEFAULT '0' COMMENT '出参标识（1：是 0：否）',
    model_desc varchar(300)  DEFAULT NULL COMMENT '对象描述',
    create_user varchar(32)  DEFAULT NULL COMMENT '创建人',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_user varchar(32)  DEFAULT NULL COMMENT '修改人',
    update_time datetime DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
);
alter table agile_drools_model comment '规则引擎 数据对象';

/*==============================================================*/
/* table: agile_drools_model_field 规则引擎 数据对象字段表          */
/*==============================================================*/
CREATE TABLE agile_drools_model_field (
    id varchar(32) NOT NULL COMMENT '对象字段主键id',
    model_id varchar(32) NOT NULL COMMENT '对象主键id',
    field_name varchar(100) NOT NULL COMMENT '字段名称',
    field_label varchar(100) NOT NULL COMMENT '字段标签',
    field_type varchar(50) NOT NULL COMMENT '字段类型',
    object_id varchar(32) DEFAULT NULL COMMENT '对象主键id',
    field_desc varchar(300) DEFAULT NULL COMMENT '字段描述',
    field_sort int DEFAULT NULL COMMENT '字段排序',
    list_flag varchar(1) DEFAULT '0' COMMENT '是否列表标识（1：是 0：否）',
    input_flag varchar(1) DEFAULT '0' COMMENT '入参标识（1：是 0：否）',
    create_user varchar(32) DEFAULT NULL COMMENT '创建人',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_user varchar(32) DEFAULT NULL COMMENT '修改人',
    update_time datetime DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
);
alter table agile_drools_model_field comment '规则引擎 数据对象字段表';

/*==============================================================*/
/* table: agile_drools_rule 规则引擎 规则配置表                    */
/*==============================================================*/
CREATE TABLE agile_drools_rule (
    id varchar(32)  NOT NULL DEFAULT '0' COMMENT '规则主键ID',
    rule_code varchar(20)  NOT NULL COMMENT '规则编码',
    rule_name varchar(100)  NOT NULL COMMENT '规则名称',
    rule_type varchar(20)  NOT NULL DEFAULT '0' COMMENT '规则类型',
    rule_package varchar(100)  NOT NULL COMMENT '规则包名',
    rule_status varchar(1)  DEFAULT '0' COMMENT '规则状态',
    rule_content text  COMMENT '规则内容',
    rule_desc varchar(300)  DEFAULT NULL COMMENT '规则描述',
    create_user varchar(32)  DEFAULT NULL COMMENT '创建人',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_user varchar(32)  DEFAULT NULL COMMENT '修改人',
    update_time datetime DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id) 
);
alter table agile_drools_rule comment '规则引擎 规则配置表';

/*==============================================================*/
/* table: agile_drools_rule_model 规则引擎 规则数据对象映射表        */
/*==============================================================*/
CREATE TABLE agile_drools_rule_model (
    id varchar(32)  NOT NULL COMMENT '规则对象主键id',
    rule_id varchar(32)  NOT NULL COMMENT '规则主键id',
    model_id varchar(32)  NOT NULL COMMENT '对象主键id',
    create_user varchar(32)  DEFAULT NULL COMMENT '创建人',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_user varchar(32)  DEFAULT NULL COMMENT '修改人',
    update_time datetime DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
);
alter table agile_drools_rule_model comment '规则引擎 规则数据对象关联表';

/*==============================================================*/
/* table: agile_drools_rule_model 规则引擎 规则场景表              */
/*==============================================================*/
CREATE TABLE agile_drools_scene (
    id varchar(32)  NOT NULL DEFAULT '0' COMMENT '场景主键ID',
    scene_code varchar(20)  NOT NULL COMMENT '场景编码',
    scene_name varchar(100)  NOT NULL COMMENT '场景名称',
    scene_status varchar(1)  DEFAULT '0' COMMENT '场景状态',
    scene_desc varchar(300)  DEFAULT NULL COMMENT '场景描述',
    create_user varchar(32)  DEFAULT NULL COMMENT '创建人',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_user varchar(32)  DEFAULT NULL COMMENT '修改人',
    update_time datetime DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
) ;
alter table agile_drools_scene comment '规则引擎 规则场景表';

/*==============================================================*/
/* table: agile_drools_rule_model 规则引擎 场景规则关联表           */
/*==============================================================*/
CREATE TABLE agile_drools_scene_rule (
    id varchar(32)  NOT NULL COMMENT '场景规则主键id',
    scene_id varchar(32)  NOT NULL COMMENT '场景主键id',
    rule_id varchar(32)  NOT NULL COMMENT '规则主键ID',
    create_user varchar(32)  DEFAULT NULL COMMENT '创建人',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_user varchar(32)  DEFAULT NULL COMMENT '修改人',
    update_time datetime DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id) 
);
alter table agile_drools_scene_rule comment '规则引擎 场景规则关联表';

/*==============================================================*/
/* table: agile_drools_scene_logger 规则引擎 场景执行日志           */
/*==============================================================*/
CREATE TABLE agile_drools_scene_logger (
    id varchar(32) NOT NULL DEFAULT '0' COMMENT '日志主键ID',
    scene_id varchar(32) NOT NULL COMMENT '场景ID',
    scene_code varchar(50) NOT NULL COMMENT '场景编码',
    scene_name varchar(100) NOT NULL COMMENT '场景名称',
    rule_count INT DEFAULT NULL COMMENT '执行规则个数',
    execute_param text COMMENT '执行参数',
    execute_result text COMMENT '执行结果',
    execute_status varchar(1) NOT NULL COMMENT '执行状态（0：失败 1：成功）',
    start_time datetime DEFAULT NULL COMMENT '开发时间',
    end_time datetime DEFAULT NULL COMMENT '结束时间',
    execute_time decimal(10,0) NOT NULL COMMENT '执行时间(毫秒)',
    error_msg text COMMENT '执行错误异常信息',
    create_user varchar(32) DEFAULT NULL COMMENT '创建人',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_user varchar(32) DEFAULT NULL COMMENT '修改人',
    update_time datetime DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
);
alter table agile_drools_scene_logger comment '规则引擎 场景执行日志';

/*==============================================================*/
/* table: agile_drools_rule_logger 规则引擎 场景规则执行日志         */
/*==============================================================*/
CREATE TABLE agile_drools_rule_logger (
    id varchar(32) NOT NULL DEFAULT '0' COMMENT '日志主键ID',
    logger_id varchar(32) NOT NULL COMMENT '执行日志ID',
    rule_name varchar(100) NOT NULL COMMENT '场景名称',
    start_time datetime DEFAULT NULL COMMENT '开发时间',
    end_time datetime DEFAULT NULL COMMENT '结束时间',
    execute_time decimal(10,0) NOT NULL COMMENT '执行时间(毫秒)',
    create_user varchar(32) DEFAULT NULL COMMENT '创建人',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_user varchar(32) DEFAULT NULL COMMENT '修改人',
    update_time datetime DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id) USING BTREE
);
alter table agile_drools_rule_logger comment '规则引擎 场景规则执行日志';


/*==============================================================*/
/* 菜单配置                                                       */
/*==============================================================*/
INSERT INTO agile_sys_menu VALUES ('8', '0', '规则引擎', '8', '', 'drools', 'drools', 'M', '0', '0', '0', '', '', '', '', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('801', '8', '数据对象', '0', 'drools/model/index', 'model', 'model', 'C', '0', '0', '0', '01', '', '', 'drools:model:page', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80101', '801', '数据对象明细', 1, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:model:detail', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80102', '801', '数据对象新增', 2, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:model:add', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80103', '801', '数据对象修改', 3, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:model:update', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80104', '801', '数据对象删除', 4, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:model:delete', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80105', '801', '数据对象导入', 5, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:model:import', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80106', '801', '数据对象导出', 6, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:model:export', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80107', '801', '数据对象列表', 7, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:model:list', '', NULL, NULL, NULL, NULL);

INSERT INTO agile_sys_menu VALUES ('802', '8', '规则管理', '1', 'drools/rule/index', 'rule', 'rule', 'C', '0', '0', '0', '01', '', '', 'drools:rule:page', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80201', '802', '规则明细', 1, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:rule:detail', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80202', '802', '规则新增', 2, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:rule:add', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80203', '802', '规则修改', 3, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:rule:update', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80204', '802', '规则删除', 4, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:rule:delete', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80205', '802', '规则导入', 5, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:rule:import', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80206', '802', '规则导出', 6, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:rule:export', '', NULL, NULL, NULL, NULL);

INSERT INTO agile_sys_menu VALUES ('803', '8', '场景管理', '2', 'drools/scene/index', 'scene', 'scene', 'C', '0', '0', '0', '01', '', '', 'drools:scene:page', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80301', '803', '场景明细', 1, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:scene:detail', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80302', '803', '场景新增', 2, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:scene:add', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80303', '803', '场景修改', 3, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:scene:update', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80304', '803', '场景删除', 4, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:scene:delete', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80305', '803', '场景导入', 5, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:scene:import', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80306', '803', '场景导出', 6, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:scene:export', '', NULL, NULL, NULL, NULL);

INSERT INTO agile_sys_menu VALUES ('804', '8', '执行日志', '2', 'drools/logger/index', 'logger', 'logger', 'C', '0', '0', '0', '01', '', '', 'drools:logger:page', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu VALUES ('80401', '804', '日志导出', 6, '', '', '#', 'F', '0', '0', '0', '', '', '', 'drools:logger:export', '', NULL, NULL, NULL, NULL);

