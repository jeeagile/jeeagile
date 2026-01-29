drop table if exists agile_process_form;
drop table if exists agile_process_model;
drop table if exists agile_process_definition;
drop table if exists agile_process_instance;
drop table if exists agile_process_task;
drop table if exists agile_process_expression;
/*==============================================================*/
/* table: agile_process_form 流程表单表                          */
/*==============================================================*/
CREATE TABLE agile_process_form
(
    id          varchar(32)  NOT NULL COMMENT '表单主键ID',
    form_code   varchar(20)  NOT NULL COMMENT '表单编码',
    form_name   varchar(100) NOT NULL COMMENT '表单名称',
    form_status varchar(1)   NOT NULL COMMENT '表单状态（0:正常 1:停用）',
    form_conf   text COMMENT '表单配置',
    form_fields text COMMENT '表单字段',
    remark      varchar(300) DEFAULT NULL COMMENT '备注',
    create_user varchar(32)  DEFAULT NULL COMMENT '创建人',
    create_time datetime     DEFAULT NULL COMMENT '创建时间',
    update_user varchar(32)  DEFAULT NULL COMMENT '修改人',
    update_time datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
);

alter table agile_process_form comment '流程表单表';

/*==============================================================*/
/* table: agile_process_model 流程模型表                          */
/*==============================================================*/
CREATE TABLE agile_process_model
(
    id                varchar(32)  NOT NULL COMMENT '流程模型主键ID',
    model_code        varchar(20)  NOT NULL COMMENT '流程模型编码',
    model_name        varchar(100) NOT NULL COMMENT '流程模型名称',
    model_version     int          NOT NULL COMMENT '流程模型版本',
    model_xml         text COMMENT '流程模型设计XMl',
    form_type         varchar(32)  NOT NULL COMMENT '流程表单类型（1:流程表单 2:业务表单）',
    form_id           varchar(32) COMMENT '流程表单ID',
    form_name         varchar(100) COMMENT '流程表单名称',
    form_url          varchar(100) COMMENT '流程表单地址',
    page_id           varchar(32) COMMENT '在线表单页面ID',
    deployment_status varchar(1)   DEFAULT '2' COMMENT '流程部署状态（1:已发布 2:未发布）',
    deployment_time   datetime     DEFAULT NULL COMMENT '流程部署时间',
    remark            varchar(300) DEFAULT NULL COMMENT '备注',
    create_user       varchar(32)  DEFAULT NULL COMMENT '创建人',
    create_time       datetime     DEFAULT NULL COMMENT '创建时间',
    update_user       varchar(32)  DEFAULT NULL COMMENT '修改人',
    update_time       datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
);
alter table agile_process_model comment '流程模型表';

/*==============================================================*/
/* table: agile_process_definition 流程定义表                     */
/*==============================================================*/
CREATE TABLE agile_process_definition
(
    id               varchar(100) NOT NULL COMMENT '流程定义主键ID(流程组件生成)',
    model_id         varchar(32)  NOT NULL COMMENT '流程模型主键ID',
    model_code       varchar(20)  NOT NULL COMMENT '流程模型编码',
    model_name       varchar(100) NOT NULL COMMENT '流程流程名称',
    model_version    int          NOT NULL COMMENT '流程模型版本',
    model_xml        text COMMENT '流程模型设计XMl',
    form_type        varchar(32)  NOT NULL COMMENT '流程表单类型',
    form_name        varchar(100) NOT NULL COMMENT '表单名称',
    form_conf        text COMMENT '流程表单配置',
    form_fields      text COMMENT '流程表单字段',
    form_url         varchar(32) DEFAULT NULL COMMENT '流程表单地址',
    page_id          varchar(32) COMMENT '在线表单页面ID',
    main_version     int          NOT NULL COMMENT '流程定义主版本（1：主版本 2：非主版本）',
    deployment_time  datetime    DEFAULT NULL COMMENT '流程部署时间',
    suspension_state int          NOT NULL COMMENT '挂起状态',
    create_user      varchar(32) DEFAULT NULL COMMENT '创建人',
    create_time      datetime    DEFAULT NULL COMMENT '创建时间',
    update_user      varchar(32) DEFAULT NULL COMMENT '修改人',
    update_time      datetime    DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
);
alter table agile_process_definition comment '流程定义表';


/*==============================================================*/
/* table: agile_process_instance 流程实例表                      */
/*==============================================================*/
CREATE TABLE agile_process_instance
(
    id               varchar(100) NOT NULL COMMENT '流程实例主键ID（流程组件生成）',
    model_id         varchar(32)  NOT NULL COMMENT '流程模型主键ID',
    model_code       varchar(20)  NOT NULL COMMENT '流程模型编码',
    model_name       varchar(100) NOT NULL COMMENT '流程流程名称',
    model_version    int          NOT NULL COMMENT '流程模型版本',
    model_xml        text COMMENT '流程模型设计XMl',
    form_type        varchar(32)  NOT NULL COMMENT '流程表单类型',
    form_name        varchar(100) NOT NULL COMMENT '表单名称',
    form_conf        text COMMENT '流程表单配置',
    form_fields      text COMMENT '流程表单字段',
    form_data        text COMMENT '流程表单数据',
    form_url         varchar(32)  DEFAULT NULL COMMENT '流程表单地址',
    page_id          varchar(32) COMMENT '在线表单页面ID',
    page_key         varchar(32) COMMENT '在线表单页面数据主键值',
    deployment_time  datetime     DEFAULT NULL COMMENT '流程部署时间',
    definition_id    varchar(100) DEFAULT NULL COMMENT '流程定义ID',
    instance_status  varchar(2)   DEFAULT NULL COMMENT '流程实例状态(0:已撤销 1:办理中 2:已完成)',
    suspension_state int          NOT NULL COMMENT '挂起状态',
    start_user       varchar(32)  NOT NULL COMMENT '流程实例发起人',
    start_user_name  varchar(100) NOT NULL COMMENT '流程发起人名称',
    start_time       datetime     DEFAULT NULL COMMENT '流程实例启动时间',
    end_time         datetime     DEFAULT NULL COMMENT '流程实例结束时间',
    create_user      varchar(32)  DEFAULT NULL COMMENT '创建人',
    create_time      datetime     DEFAULT NULL COMMENT '创建时间',
    update_user      varchar(32)  DEFAULT NULL COMMENT '修改人',
    update_time      datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
);
alter table agile_process_instance comment '流程实例表';



/*==============================================================*/
/* table: agile_process_task 流程任务表                         */
/*==============================================================*/
CREATE TABLE agile_process_task
(
    id                 varchar(100) NOT NULL COMMENT '流程任务主键ID(流程组件生成)',
    model_id           varchar(32)  NOT NULL COMMENT '流程模型主键ID',
    model_code         varchar(20)  NOT NULL COMMENT '流程模型编码',
    model_name         varchar(100) NOT NULL COMMENT '流程流程名称',
    form_name          varchar(100) NOT NULL COMMENT '表单名称',
    definition_id      varchar(100) NOT NULL COMMENT '流程定义ID',
    instance_id        varchar(100) NOT NULL COMMENT '流程实例ID',
    task_name          varchar(100) NOT NULL COMMENT '流程任务名称',
    assignee_user      varchar(32)  DEFAULT NULL COMMENT '流程任务执行人',
    assignee_user_name varchar(100) DEFAULT NULL COMMENT '流程任务执行人名称',
    task_status        varchar(2)   NOT NULL COMMENT '流程任务状态(0:已撤销 1:办理中 2：已完成)',
    start_user         varchar(32)  NOT NULL COMMENT '流程发起人',
    start_user_name    varchar(100) NOT NULL COMMENT '流程发起人名称',
    start_time         datetime     DEFAULT NULL COMMENT '流程任务启动时间',
    end_time           datetime     DEFAULT NULL COMMENT '流程任务结束时间',
    approve_message    varchar(500) DEFAULT NULL COMMENT '审批意见',
    create_user        varchar(32)  DEFAULT NULL COMMENT '创建人',
    create_time        datetime     DEFAULT NULL COMMENT '创建时间',
    update_user        varchar(32)  DEFAULT NULL COMMENT '修改人',
    update_time        datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
);
alter table agile_process_task comment '流程任务表';


/*==============================================================*/
/* table: agile_process_expression 流程表达式配置表              */
/*==============================================================*/
CREATE TABLE agile_process_expression
(
    id                varchar(32)  NOT NULL COMMENT '表达式主键ID',
    expression_code   varchar(20)  NOT NULL COMMENT '表达式编码',
    expression_name   varchar(50)  NOT NULL COMMENT '表达式名称',
    expression_value  varchar(100) NOT NULL COMMENT '表达式',
    expression_status varchar(1)   NOT NULL COMMENT '表达式状态（0:正常 1:停用）',
    remark            varchar(300) DEFAULT NULL COMMENT '备注',
    create_user       varchar(32)  DEFAULT NULL COMMENT '创建人',
    create_time       datetime     DEFAULT NULL COMMENT '创建时间',
    update_user       varchar(32)  DEFAULT NULL COMMENT '修改人',
    update_time       datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
);
alter table agile_process_expression comment '流程表达式配置表';

/*==============================================================*/
/* 字典配置                                                     */
/*==============================================================*/

INSERT INTO agile_sys_dict_type
VALUES ('12', '流程发布状态', 'process_deployment_status', '0', '0', '流程发布状态', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_dict_type
VALUES ('13', '流程表单类型', 'process_form_type', '0', '0', '流程表单类型', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_dict_type
VALUES ('14', '流程任务状态', 'process_task_status', '0', '0', '流程任务状态', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_dict_type
VALUES ('15', '流程实例状态', 'process_instance_status', '0', '0', '流程任务状态', NULL, NULL, NULL, NULL);


INSERT INTO agile_sys_dict_data
VALUES ('121', '0', 0, '已发布', '1', 'process_deployment_status', '0', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_dict_data
VALUES ('122', '0', 1, '未发布', '2', 'process_deployment_status', '0', '0', NULL, NULL, NULL, NULL, NULL);

INSERT INTO agile_sys_dict_data
VALUES ('131', '0', 0, '流程表单', '01', 'process_form_type', '0', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_dict_data
VALUES ('132', '0', 1, '业务表单', '02', 'process_form_type', '0', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_dict_data
VALUES ('133', '0', 1, '在线表单', '03', 'process_form_type', '0', '0', NULL, NULL, NULL, NULL, NULL);

INSERT INTO agile_sys_dict_data
VALUES ('141', '0', 0, '已撤销', '0', 'process_task_status', '0', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_dict_data
VALUES ('142', '0', 1, '审批中', '1', 'process_task_status', '0', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_dict_data
VALUES ('143', '0', 2, '审批通过', '2', 'process_task_status', '0', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_dict_data
VALUES ('144', '0', 3, '审批拒绝', '3', 'process_task_status', '0', '0', NULL, NULL, NULL, NULL, NULL);

INSERT INTO agile_sys_dict_data
VALUES ('151', '0', 0, '已撤销', '0', 'process_instance_status', '0', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_dict_data
VALUES ('152', '0', 1, '审批中', '1', 'process_instance_status', '0', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_dict_data
VALUES ('153', '0', 2, '审批完成', '2', 'process_instance_status', '0', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_dict_data
VALUES ('154', '0', 3, '审批拒绝', '3', 'process_instance_status', '0', '0', NULL, NULL, NULL, NULL, NULL);

/*==============================================================*/
/* 菜单配置                                                     */
/*==============================================================*/
INSERT INTO agile_sys_menu
VALUES ('6', '0', '流程管理', '6', '', 'process', 'process', 'M', '0', '0', '0', '', '', '', '', '', NULL, NULL, NULL,
        NULL);
INSERT INTO agile_sys_menu
VALUES ('601', '6', '流程表单', '0', 'process/form/index', 'form', 'form', 'C', '0', '0', '0', '01', '', '',
        'process:form:page', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60101', '601', '流程表单明细', 1, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:form:detail', '', NULL,
        NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60102', '601', '流程表单新增', 2, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:form:add', '', NULL,
        NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60103', '601', '流程表单修改', 3, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:form:update', '', NULL,
        NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60104', '601', '流程表单删除', 4, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:form:delete', '', NULL,
        NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60105', '601', '流程表单导入', 5, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:form:import', '', NULL,
        NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60106', '601', '流程表单导出', 6, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:form:export', '', NULL,
        NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60107', '601', '流程表单预览', 7, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:form:view', '', NULL,
        NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60108', '601', '流程表单设计', 8, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:form:designer', '',
        NULL, NULL, NULL, NULL);

INSERT INTO agile_sys_menu
VALUES ('602', '6', '流程模型', '1', 'process/model/index', 'model', 'example', 'C', '0', '0', '0', '01', '', '',
        'process:model:page,process:model:list', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60201', '602', '流程模型明细', 1, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:model:detail', '',
        NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60202', '602', '流程模型新增', 2, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:model:add', '', NULL,
        NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60203', '602', '流程模型修改', 3, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:model:update', '',
        NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60204', '602', '流程模型删除', 4, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:model:delete', '',
        NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60205', '602', '流程模型导入', 5, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:model:import', '',
        NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60206', '602', '流程模型导出', 6, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:model:export', '',
        NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60207', '602', '流程模型预览', 7, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:model:view', '', NULL,
        NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60208', '602', '流程模型设计', 8, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:model:designer', '',
        NULL, NULL, NULL, NULL);

INSERT INTO agile_sys_menu
VALUES ('603', '6', '流程表达式', '1', 'process/expression/index', 'expression', 'expression', 'C', '0', '0', '0', '01',
        '', '', 'process:expression:page,process:expression:list', '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60301', '603', '流程表达式明细', 1, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:expression:detail',
        '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60302', '603', '流程表达式新增', 2, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:expression:add', '',
        NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60303', '603', '流程表达式修改', 3, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:expression:update',
        '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60304', '603', '流程表达式删除', 4, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:expression:delete',
        '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60305', '603', '流程表达式导入', 5, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:expression:import',
        '', NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('60306', '603', '流程表达式导出', 6, '', '', '#', 'F', '0', '0', '0', '', '', '', 'process:expression:export',
        '', NULL, NULL, NULL, NULL);

INSERT INTO agile_sys_menu
VALUES ('7', '0', '我的事务', '0', '', 'process/task', 'education', 'M', '0', '0', '0', '', '', '', '', '', NULL, NULL,
        NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('701', '7', '流程发起', '1', 'process/task/start', 'start', 'edit', 'C', '0', '0', '0', '', '', '', '', '',
        NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('702', '7', '我的代办', '2', 'process/task/todo', 'todo', 'message', 'C', '0', '0', '0', '', '', '', '', '',
        NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('703', '7', '我的已办', '3', 'process/task/done', 'done', 'clipboard', 'C', '0', '0', '0', '', '', '', '', '',
        NULL, NULL, NULL, NULL);
INSERT INTO agile_sys_menu
VALUES ('704', '7', '我的发起', '4', 'process/task/apply', 'apply', 'button', 'C', '0', '0', '0', '', '', '', '', '',
        NULL, NULL, NULL, NULL);

