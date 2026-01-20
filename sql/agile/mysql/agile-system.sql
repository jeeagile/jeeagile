drop table if exists agile_sys_config;
drop table if exists agile_sys_dict_type;
drop table if exists agile_sys_dict_data;
drop table if exists agile_sys_role_dept;
drop table if exists agile_sys_role_menu;
drop table if exists agile_sys_user_post;
drop table if exists agile_sys_user_role;
drop table if exists agile_sys_user;
drop table if exists agile_sys_post;
drop table if exists agile_sys_role;
drop table if exists agile_sys_menu;
drop table if exists agile_sys_dept;
drop table if exists agile_sys_login;
drop table if exists agile_sys_logger;
drop table if exists agile_online_dict;
drop table if exists agile_online_form;
drop table if exists agile_online_table;
drop table if exists agile_online_column;
drop table if exists agile_online_rule;
drop table if exists agile_online_column_rule;
drop table if exists agile_online_page;

/*==============================================================*/
/* table: agile_sys_config 参数配置表                            */
/*==============================================================*/
create table agile_sys_config
(
    id                varchar(32) not null comment '参数主键',
    config_name       varchar(100) not null comment '参数名称',
    config_key        varchar(100) not null comment '参数键名',
    config_value      varchar(300) not null comment '参数键值',
    system_flag       char(1) not null default '0' comment '系统内置（0:否 1:是）',
    remark            varchar(300) default null comment '备注',
    create_user       varchar(32) default null comment '创建人',
    create_time       datetime default null comment '创建时间',
    update_user       varchar(32) default null comment '修改人',
    update_time       datetime default null comment '修改时间',
    primary key (id)
);
alter table agile_sys_config comment '参数配置表';

INSERT INTO agile_sys_config VALUES ('1', '用户管理-账号初始密码', 'sys.user.pwd', '123456', '1', '初始化密码 123456',NULL,NULL,NULL,NULL);

/*==============================================================*/
/* table: agile_sys_dept 部门表                                 */
/*==============================================================*/
create table agile_sys_dept
(
    id                varchar(32) not null comment '部门主键',
    parent_id         varchar(32) not null default '0' comment '上级部门',
    dept_name         varchar(100) not null comment '部门名称',
    dept_code         varchar(20) not null comment '部门编码',
    dept_sort         int not null default '0' comment '显示顺序',
    dept_status       char(1) not null default '0' comment '部门状态（0:正常 1:停用）',
    remark            varchar(300) default null comment '备注',
    create_user       varchar(32) default null comment '创建人',
    create_time       datetime default null comment '创建时间',
    update_user       varchar(32) default null comment '修改人',
    update_time       datetime default null comment '修改时间',
    primary key (id)
);

alter table agile_sys_dept comment '部门管理表';

INSERT INTO agile_sys_dept VALUES ('1', '0', '敏捷开发', 'jeeagile',0,'0',NULL,NULL,NULL,NULL,NULL);


/*==============================================================*/
/* table: agile_sys_dict_type 字典类型表                        */
/*==============================================================*/
create table agile_sys_dict_type
(
    id                varchar(32) not null comment '字典类型主键',
    dict_name         varchar(100) not null comment '字典名称',
    dict_type         varchar(100) not null comment '字典类型',
    dict_status       char(1) not null default '0' comment '状态（0:正常 1:停用）',
    system_flag       char(1) default '0' comment '系统内置（0:否 1:是）',
    remark            varchar(300) default null comment '备注',
    create_user       varchar(32) default null comment '创建人',
    create_time       datetime default null comment '创建时间',
    update_user       varchar(32) default null comment '修改人',
    update_time       datetime default null comment '修改时间',
    primary key (id)
);

alter table agile_sys_dict_type comment '字典类型表';

INSERT INTO agile_sys_dict_type VALUES ('1', '用户性别', 'sys_user_sex', '0', '0', '用户性别',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('2', '显示隐藏', 'sys_show_hide', '0', '0', '显示隐藏',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('3', '正常停用', 'sys_normal_disable', '0', '0', '正常停用',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('4', '系统是否', 'sys_yes_no', '0', '0', '系统是否',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('5', '数据范围', 'sys_data_scope', '0', '0', '数据范围列表',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('6', '成功失败', 'sys_success_fail', '0', '0', '成功失败',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('7', '操作类型', 'sys_operate_type', '0', '0', '操作类型',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('9', '开关状态', 'sys_switch_status', '0', '0', '开关状态',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('10', '审核状态', 'sys_audit_status', '0', '0', '审核状态',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('11', '发布状态', 'sys_publish_status', '0', '0', '发布状态',NULL,NULL,NULL,NULL);


/*==============================================================*/
/* table: agile_sys_dict_data 字典数据表                         */
/*==============================================================*/
create table agile_sys_dict_data
(
    id                varchar(32) not null comment '字典数据主键',
    parent_id         varchar(32) not null default '0' comment '上级字典',
    dict_sort         int not null default '0' comment '字典排序',
    dict_label        varchar(100) not null comment '字典标签',
    dict_value        varchar(100) not null comment '字典键值',
    dict_type         varchar(100) not null comment '字典类型',
    dict_status       char(1) not null default '0' comment '字典状态（0:正常 1:停用）',
    system_flag       char(1) not null default '0' comment '系统内置（0:否 1:是）',
    remark            varchar(300) default null comment '备注',
    create_user       varchar(32) default null comment '创建人',
    create_time       datetime default null comment '创建时间',
    update_user       varchar(32) default null comment '修改人',
    update_time       datetime default null comment '修改时间',
    primary key (id)
);

alter table agile_sys_dict_data comment '字典数据表';

INSERT INTO agile_sys_dict_data VALUES ('11', '0',1,'男', '0', 'sys_user_sex', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('12', '0',2,'女', '1', 'sys_user_sex', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('13', '0',3,'未知', '2', 'sys_user_sex', '0', '0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('21', '0',1,'显示', '0', 'sys_show_hide', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('22', '0',2,'隐藏', '1', 'sys_show_hide', '0', '0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('31', '0',1,'正常', '0', 'sys_normal_disable', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('32', '0',2,'停用', '1', 'sys_normal_disable', '0', '0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('41', '0',1,'是', '1', 'sys_yes_no', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('42', '0',2,'否', '0', 'sys_yes_no', '0', '0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('51', '0',0,'全部数据权限', '01', 'sys_data_scope', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('52', '0',1,'本部门数据权限', '02', 'sys_data_scope', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('53', '0',3,'本部门及以下数据权限', '03', 'sys_data_scope', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('54', '0',4,'仅本人数据权限', '04', 'sys_data_scope', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('55', '0',5,'自定义部门数据权限', '05', 'sys_data_scope', '0', '0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('61', '0',1,'成功', '0', 'sys_success_fail', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('62', '0',2,'失败', '1', 'sys_success_fail', '0', '0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('71', '0',1,'查询数据', 'SELECT', 'sys_operate_type', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('72', '0',2,'查看明细', 'DETAIL', 'sys_operate_type', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('73', '0',3,'新增数据', 'ADD', 'sys_operate_type', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('74', '0',4,'修改数据', 'UPDATE', 'sys_operate_type', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('75', '0',5,'删除数据', 'DELETE', 'sys_operate_type', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('76', '0',6,'用户授权', 'GRANT', 'sys_operate_type', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('77', '0',7,'导出数据', 'EXPORT', 'sys_operate_type', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('78', '0',8,'导入数据', 'IMPORT', 'sys_operate_type', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('79', '0',9,'清空数据', 'CLEAR', 'sys_operate_type', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('710', '0',10,'用户强退', 'FORCE', 'sys_operate_type', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('711', '0',11,'代码生成', 'GENERATOR', 'sys_operate_type', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('712', '0',12,'其他操作', 'OTHER', 'sys_operate_type', '0', '0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('91', '0',0,'启用', '0', 'sys_switch_status', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('92', '0',1,'停用', '1', 'sys_switch_status', '0', '0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('101', '0',0,'审核中', '0', 'sys_audit_status', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('102', '0',1,'审核通过', '1', 'sys_audit_status', '0', '0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('103', '0',2,'审核拒绝', '2', 'sys_audit_status', '0', '0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('111', '0', 0, '已发布', '01', 'sys_publish_status', '0', '0', NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('112', '0', 1, '未发布', '02', 'sys_publish_status', '0', '0', NULL,NULL,NULL,NULL,NULL);

/*==============================================================*/
/* table: agile_sys_menu 菜单权限表                              */
/*==============================================================*/
create table agile_sys_menu
(
    id                varchar(32) not null comment '菜单主键',
    parent_id         varchar(32) not null default '0' comment '上级菜单',
    menu_name         varchar(100) not null comment '菜单名称',
    menu_sort         int not null default '0' comment '显示顺序',
    menu_comp         varchar(200) default null comment '组件路径',
    menu_path         varchar(200) default null comment '路由地址',
    menu_icon         varchar(100) default null comment '菜单图标',
    menu_type         char(1) not null comment '菜单类型（M:目录 C:菜单 F:按钮）',
    menu_visible      char(1) not null default '0' comment '菜单显示状态（0:显示 1:隐藏）',
    menu_status       char(1) not null default '0' comment '菜单状态（0:正常 1:停用）',
    menu_frame        char(1) not null default '1' comment '外链标识（0:是 1:否）',
    menu_kind         VARCHAR(2)  default null comment '菜单分类（01：路由菜单 02：在线表单 03：工单列表）',
    page_id           VARCHAR(32)  default null comment '在线表单页面ID',
    menu_perm         varchar(100) default null comment '权限标识',
    remark            varchar(300) default null comment '备注',
    create_user       varchar(32) default null comment '创建人',
    create_time       datetime default null comment '创建时间',
    update_user       varchar(32) default null comment '修改人',
    update_time       datetime default null comment '修改时间',
    primary key (id)
);

alter table agile_sys_menu comment '菜单权限表';

INSERT INTO agile_sys_menu VALUES ('1', '0', '系统管理',1,'', 'system', 'system', 'M', '0', '0', '1', '', '', '', '系统管理目录',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('101', '1', '用户管理',1,'system/user/index', 'user', 'user', 'C', '0', '0', '1', '01', '', 'system:user:page', '用户管理菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10101', '101', '用户明细',1,'', '', '#', 'F', '0', '0', '1', '', '', 'system:user:detail', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10102', '101', '用户新增',2,'', '', '#', 'F', '0', '0', '1', '', '', 'system:user:add', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10103', '101', '用户修改',3,'', '', '#', 'F', '0', '0', '1', '', '', 'system:user:update', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10104', '101', '用户删除',4,'', '', '#', 'F', '0', '0', '1', '', '', 'system:user:delete', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10105', '101', '重置密码',5,'', '', '#', 'F', '0', '0', '1', '', '', 'system:user:password', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10106', '101', '用户导入',6,'', '', '#', 'F', '0', '0', '1', '', '', 'system:user:import', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10107', '101', '用户导出',7,'', '', '#', 'F', '0', '0', '1', '', '', 'system:user:export', '',NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('102', '1', '角色管理',2,'system/role/index', 'role', 'role', 'C', '0', '0', '1', '01', '', 'system:role:page', '角色管理菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10201', '102', '角色明细',1,'', '', '#', 'F', '0', '0', '1', '', '', 'system:role:detail', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10202', '102', '角色新增',2,'', '', '#', 'F', '0', '0', '1', '', '', 'system:role:add', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10203', '102', '角色修改',3,'', '', '#', 'F', '0', '0', '1', '', '', 'system:role:update', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10204', '102', '角色删除',4,'', '', '#', 'F', '0', '0', '1', '', '', 'system:role:delete', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10205', '102', '数据权限',5,'', '', '#', 'F', '0', '0', '1', '', '', 'system:role:scope', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10206', '102', '角色导入',6,'', '', '#', 'F', '0', '0', '1', '', '', 'system:role:import', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10207', '102', '角色导出',7,'', '', '#', 'F', '0', '0', '1', '', '', 'system:role:export', '',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('103', '1', '菜单管理',3,'system/menu/index', 'menu', 'menu', 'C', '0', '0', '1', '01', '', 'system:menu:list', '菜单管理菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10301', '103', '菜单明细',1,'', '', '#', 'F', '0', '0', '1', '', '', 'system:menu:detail', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10302', '103', '菜单新增',2,'', '', '#', 'F', '0', '0', '1', '', '', 'system:menu:add', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10303', '103', '菜单修改',3,'', '', '#', 'F', '0', '0', '1', '', '', 'system:menu:update', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10304', '103', '菜单删除',4,'', '', '#', 'F', '0', '0', '1', '', '', 'system:menu:delete', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10305', '103', '菜单导入',5,'', '', '#', 'F', '0', '0', '1', '', '', 'system:menu:import', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10306', '103', '菜单导出',6,'', '', '#', 'F', '0', '0', '1', '', '', 'system:menu:export', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10307', '103', '菜单排序',7,'', '', '#', 'F', '0', '0', '1', '', '', 'system:menu:sort', '',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('104', '1', '部门管理',4,'system/dept/index', 'dept', 'dept', 'C', '0', '0', '1', '01', '', 'system:dept:list', '部门管理菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10401', '104', '部门明细',1,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dept:detail', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10402', '104', '部门新增',2,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dept:add', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10403', '104', '部门修改',3,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dept:update', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10404', '104', '部门删除',4,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dept:delete', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10405', '104', '部门导入',5,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dept:import', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10406', '104', '部门导出',6,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dept:export', '',NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('105', '1', '岗位管理',5,'system/post/index', 'post', 'post', 'C', '0', '0', '1', '01', '', 'system:post:page', '岗位管理菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10501', '105', '岗位明细',1,'', '', '#', 'F', '0', '0', '1', '', '', 'system:post:detail', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10502', '105', '岗位新增',2,'', '', '#', 'F', '0', '0', '1', '', '', 'system:post:add', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10503', '105', '岗位修改',3,'', '', '#', 'F', '0', '0', '1', '', '', 'system:post:update', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10504', '105', '岗位删除',4,'', '', '#', 'F', '0', '0', '1', '', '', 'system:post:delete', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10505', '105', '岗位导入',5,'', '', '#', 'F', '0', '0', '1', '', '', 'system:post:import', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10506', '105', '岗位导出',6,'', '', '#', 'F', '0', '0', '1', '', '', 'system:post:export', '',NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('106', '1', '字典管理',6,'system/dict/index', 'dict', 'dict', 'C', '0', '0', '1', '01', '', 'system:dict:type:page', '字典管理菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10601', '106', '字典明细',1,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:type:detail', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10602', '106', '字典新增',2,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:type:add', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10603', '106', '字典修改',3,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:type:update', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10604', '106', '字典删除',4,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:type:delete', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10605', '106', '字典导入',5,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:type:import', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10606', '106', '字典导出',6,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:type:export', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10607', '106', '字典数据分页',7,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:data:page', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10608', '106', '字典数据列表',8,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:data:list', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10609', '106', '字典数据明细',9,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:data:detail', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10610', '106', '字典数据新增',10,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:data:add', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10611', '106', '字典数据修改',11,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:data:update', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10612', '106', '字典数据删除',12,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:data:delete', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10613', '106', '字典数据导入',13,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:data:import', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10614', '106', '字典数据导出',14,'', '', '#', 'F', '0', '0', '1', '', '', 'system:dict:data:export', '',NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('107', '1', '参数设置',7,'system/config/index', 'config', 'config', 'C', '0', '0', '1', '01', '', 'system:config:page', '参数设置菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10701', '107', '参数明细',1,'', '', '#', 'F', '0', '0', '1', '', '', 'system:config:detail', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10702', '107', '参数新增',2,'', '', '#', 'F', '0', '0', '1', '', '', 'system:config:add', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10703', '107', '参数修改',3,'', '', '#', 'F', '0', '0', '1', '', '', 'system:config:update', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10704', '107', '参数删除',4,'', '', '#', 'F', '0', '0', '1', '', '', 'system:config:delete', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10705', '107', '参数导入',5,'', '', '#', 'F', '0', '0', '1', '', '', 'system:config:import', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10706', '107', '参数导出',6,'', '', '#', 'F', '0', '0', '1', '', '', 'system:config:export', '',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('108', '1', '操作日志',8,'system/logger/operate', 'operate', 'operate', 'C', '0', '0', '1', '01', '', 'logger:operate:page', '操作日志菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10801', '108', '查看',1,'', '', '', 'F', '0', '0', '1', '', '', 'logger:operate:detail', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10802', '108', '删除',2,'', '#', '#', 'F', '0', '0', '1', '', '', 'logger:operate:delete', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10803', '108', '清空',3,'', '', '', 'F', '0', '0', '1', '', '', 'logger:operate:clear', '',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('109', '1', '登录日志',9,'system/logger/login', 'login', 'login', 'C', '0', '0', '1', '01', '', 'logger:login:page', '登录日志菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10901', '109', '删除',1,'', '#', '#', 'F', '0', '0', '1', '', '', 'logger:login:delete', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10902', '109', '清空',2,'', '', '', 'F', '0', '0', '1', '', '', 'logger:login:clear', '',NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('2', '0', '系统监控',2,NULL,'monitor', 'monitor', 'M', '0', '0', '1', '', '', '', '系统监控目录',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('201', '2', '在线用户',1,'monitor/online/index', 'online', 'online', 'C', '0', '0', '1', '01', '', 'monitor:online:list', '在线用户菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('20101', '201', '批量强退',1,'', '#', '#', 'F', '0', '0', '1', '', '', 'monitor:online:batchLogout', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('20102', '201', '单户强退',2,'', '#', '#', 'F', '0', '0', '1', '', '', 'monitor:online:forceLogout', '',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('202', '2', '数据监控',2,'monitor/druid/index', 'druid', 'druid', 'C', '0', '0', '1', '01', '', 'monitor:druid:list', '数据监控菜单',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('203', '2', '服务监控',3,'monitor/server/index', 'server', 'server', 'C', '0', '0', '1', '01', '', 'monitor:server:info', '服务监控菜单',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('3', '0', '系统工具',3,NULL,'tool', 'tool', 'M', '0', '0', '1', '', '', '', '系统工具目录',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('301', '3', '表单构建',1,'tool/form/index', 'form', 'form', 'C', '0', '0', '1', '01', '', 'tool:form:list', '表单构建菜单',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('302', '3', '代码生成',2,'tool/generator/index', 'generator', 'generator', 'C', '0', '0', '1', '01', '', 'tool:generator:page', '代码生成菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30201', '302', '生成修改',1,'', '#', '#', 'F', '0', '0', '1', '', '', 'tool:generator:update', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30202', '302', '生成删除',2,'', '#', '#', 'F', '0', '0', '1', '', '', 'tool:generator:delete', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30203', '302', '导入代码',3,'', '#', '#', 'F', '0', '0', '1', '', '', 'tool:generator:import', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30204', '302', '同步信息',4,'', '#', '#', 'F', '0', '0', '1', '', '', 'tool:generator:sync', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30205', '302', '预览代码',5,'', '#', '#', 'F', '0', '0', '1', '', '', 'tool:generator:preview', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30206', '302', '生成代码',6,'', '#', '#', 'F', '0', '0', '1', '', '', 'tool:generator:code', '',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('303', '3', '系统接口',3,'tool/swagger/index', 'swagger', 'swagger', 'C', '0', '0', '1', '01', '', 'tool:swagger:view', '系统接口菜单',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('304', '3', '流程设计',4,'tool/process/index', 'process', 'process', 'C', '0', '0', '1', '01', '', 'tool:process:view', '系统接口菜单',NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('4', '0', '在线表单', '4', '', 'online', 'online', 'M', '0', '0', '1', '', '', '', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('401', '4', '字典管理',1,'online/dict/index', 'dict', 'dict', 'C', '0', '0', '1', '01', '', 'online:dict:page', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40101', '401', '字典明细',1,'', '', '#', 'F', '0', '0', '1', '', '', 'online:dict:detail', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40102', '401', '新增字典',2,'', '', '#', 'F', '0', '0', '1', '', '', 'online:dict:add', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40103', '401', '修改字典',3,'', '', '#', 'F', '0', '0', '1', '', '', 'online:dict:update', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40104', '401', '删除字典',4,'', '', '#', 'F', '0', '0', '1', '', '', 'online:dict:delete', '',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('402', '4', '表单管理',1,'online/form/index', 'form', 'form', 'C', '0', '0', '1', '01', '', 'online:form:page', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40201', '402', '表单明细',1,'', '', '#', 'F', '0', '0', '1', '', '', 'online:form:detail', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40202', '402', '新增表单',2,'', '', '#', 'F', '0', '0', '1', '', '', 'online:form:add', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40203', '402', '修改表单',3,'', '', '#', 'F', '0', '0', '1', '', '', 'online:form:update', '',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40204', '402', '删除表单',4,'', '', '#', 'F', '0', '0', '1', '', '', 'online:form:delete', '',NULL,NULL,NULL,NULL);


/*==============================================================*/
/* table: agile_sys_post 岗位信息表                              */
/*==============================================================*/
create table agile_sys_post
(
    id                varchar(32) not null comment '岗位主键',
    post_code         varchar(20) not null comment '岗位编码',
    post_name         varchar(100) not null comment '岗位名称',
    post_sort         int not null comment '显示顺序',
    post_status       char(1) not null comment '状态（0:正常 1:停用）',
    remark            varchar(300) default null comment '备注',
    create_user       varchar(32) default null comment '创建人',
    create_time       datetime default null comment '创建时间',
    update_user       varchar(32) default null comment '修改人',
    update_time       datetime default null comment '修改时间',
    primary key (id)
);

alter table agile_sys_post comment '岗位信息表';

INSERT INTO agile_sys_post VALUES ('1', 'jeeagile', 'JeeAgile',0,'0',NULL,NULL,NULL,NULL,NULL);

/*==============================================================*/
/* table: agile_sys_role   */
/*==============================================================*/
create table agile_sys_role
(
    id                varchar(32) not null comment '角色主键',
    role_name         varchar(30) not null comment '角色名称',
    role_code         varchar(100) not null comment '角色编码',
    role_sort         int not null comment '显示顺序',
    role_status       char(1) not null comment '角色状态（0:正常 1:停用）',
    data_scope        char(2) not null default '01' comment '数据范围（参见字典sys_data_scope）',
    remark            varchar(300) default null comment '备注',
    create_user       varchar(32) default null comment '创建人',
    create_time       datetime default null comment '创建时间',
    update_user       varchar(32) default null comment '修改人',
    update_time       datetime default null comment '修改时间',
    primary key (id)
);

alter table agile_sys_role comment '角色信息表';

INSERT INTO agile_sys_role VALUES ('1', 'jeeagile', 'JeeAgile',0,'0', '02',NULL,NULL,NULL,NULL,NULL);

/*==============================================================*/
/* table: agile_sys_role_dept 角色部门关联表                     */
/*==============================================================*/
create table agile_sys_role_dept
(
    id                varchar(32) not null comment '角色部门主键',
    role_id           varchar(32) not null comment '角色主键',
    dept_id           varchar(32) not null comment '部门主键',
    create_user       varchar(32) default null comment '创建人',
    create_time       datetime default null comment '创建时间',
    update_user       varchar(32) default null comment '修改人',
    update_time       datetime default null comment '修改时间',
    primary key (id)
);

alter table agile_sys_role_dept comment '角色部门关联表';

/*==============================================================*/
/* table: agile_sys_role_menu 角色菜单关联表                     */
/*==============================================================*/
create table agile_sys_role_menu
(
    id                varchar(32) not null comment '角色菜单主键',
    role_id           varchar(32) not null comment '角色主键',
    menu_id           varchar(32) not null comment '菜单主键',
    create_user       varchar(32) default null comment '创建人',
    create_time       datetime default null comment '创建时间',
    update_user       varchar(32) default null comment '修改人',
    update_time       datetime default null comment '修改时间',
    primary key (id)
);

alter table agile_sys_role_menu comment '角色菜单关联表';

INSERT INTO agile_sys_role_menu VALUES ('1', '1', '1',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('2', '1', '101',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('3', '1', '10101',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('4', '1', '10102',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('5', '1', '10103',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('6', '1', '10104',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('7', '1', '102',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('8', '1', '10201',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('9', '1', '10202',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('10', '1', '10203',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('11', '1', '103',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('12', '1', '10301',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('13', '1', '10302',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('14', '1', '10303',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('15', '1', '10304',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('16', '1', '104',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('17', '1', '10401',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('18', '1', '10402',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('19', '1', '10403',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('20', '1', '105',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('21', '1', '10501',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('22', '1', '10502',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('23', '1', '10503',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('24', '1', '106',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('25', '1', '10601',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('26', '1', '10602',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('27', '1', '10603',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('28', '1', '107',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('29', '1', '10701',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('30', '1', '10702',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('31', '1', '10703',NULL,NULL,NULL,NULL);


/*==============================================================*/
/* table: agile_sys_user 用户信息表                              */
/*==============================================================*/
create table agile_sys_user
(
    id                varchar(32) not null comment '用户主键',
    user_name         varchar(30) not null comment '用户登录名',
    nick_name         varchar(100) default null comment '用户昵称',
    user_sex          char(1) default null comment '用户性别',
    user_sort         int not null comment '显示顺序',
    user_phone        varchar(20) default null comment '联系电话',
    user_mobile       varchar(12) default null comment '联系手机',
    user_address      varchar(200) default null comment '联系地址',
    user_email        varchar(50) default null comment '邮箱地址',
    user_avatar       varchar(100) default null comment '头像地址',
    user_status       char(1) not null default '0' comment '用户状态（0:正常 2:停用 3:冻结）',
    user_pwd          varchar(100) not null comment '用户密码',
    dept_id           varchar(32) default null comment '部门主键',
    remark            varchar(300) default null comment '备注',
    create_user       varchar(32) default null comment '创建人',
    create_time       datetime default null comment '创建时间',
    update_user       varchar(32) default null comment '修改人',
    update_time       datetime default null comment '修改时间',
    primary key (id)
);

alter table agile_sys_user comment '用户信息表';

INSERT INTO agile_sys_user VALUES ('0', 'admin', '管理员', '1',0,'18600000000', '18600000000',NULL,'admin@163.com',NULL,'0', 'e10adc3949ba59abbe56e057f20f883e', '',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_user VALUES ('1', 'jeeagile', 'JeeAgile', '1',1,'18600000000', '18600000000',NULL,'jeeagile@163.com', '', '0', 'e10adc3949ba59abbe56e057f20f883e', '1',NULL,NULL,NULL,NULL,NULL);


/*==============================================================*/
/* table: agile_sys_user_post 用户岗位关联表                     */
/*==============================================================*/
create table agile_sys_user_post
(
    id                varchar(32) not null comment '用户岗位主键',
    user_id           varchar(32) not null comment '用户主键',
    post_id           varchar(32) not null comment '岗位主键',
    create_user       varchar(32) default null comment '创建人',
    create_time       datetime default null comment '创建时间',
    update_user       varchar(32) default null comment '修改人',
    update_time       datetime default null comment '修改时间',
    primary key (id)
);

alter table agile_sys_user_post comment '用户岗位关联表';

INSERT INTO agile_sys_user_post VALUES ('1', '1', '1',NULL,NULL,NULL,NULL);

/*==============================================================*/
/* table: agile_sys_user_role 用户角色关联表                     */
/*==============================================================*/
create table agile_sys_user_role
(
    id                varchar(32) not null comment '用户角色主键',
    user_id           varchar(32) not null comment '用户主键',
    role_id           varchar(32) not null comment '角色主键',
    create_user       varchar(32) default null comment '创建人',
    create_time       datetime default null comment '创建时间',
    update_user       varchar(32) default null comment '修改人',
    update_time       datetime default null comment '修改时间',
    primary key (id)
);

alter table agile_sys_user_role comment '用户角色关联表';

INSERT INTO agile_sys_user_role VALUES ('1', '1', '1',NULL,NULL,NULL,NULL);

/*==============================================================*/
/* table: agile_sys_login 用户登录日志表                      */
/*==============================================================*/
create table agile_sys_login
(
    id                     varchar(32) not null comment '登录日志主键',
    login_module           varchar(150) not null comment '登录类型',
    login_name             varchar(150) not null comment '登录用户名',
    login_time             datetime not null comment '登录时间',
    login_ip               varchar(50) not null comment '登录IP',
    login_address          varchar(150) default null comment '登录地址',
    login_device           varchar(100) not null comment '登录设备名称',
    login_os               varchar(100) not null comment '登录设备操作系统名称',
    login_browser          varchar(100) not null comment '登录浏览器名称',
    server_address         varchar(50) not null comment '服务器地址',
    status                 varchar(1) not null comment '登录状态（0：成功 1：失败）',
    message                longtext comment '登录信息',
    primary key (id)
);

alter table agile_sys_login comment '用户登录日志表';

/*==============================================================*/
/* table: agile_sys_logger 用户操作日志表                    */
/*==============================================================*/
create table agile_sys_logger
(
    id                    varchar(32) not null comment '操作日志主键',
    operate_module        varchar(100) not null comment '操作模块',
    operate_notes         varchar(100) default null comment '操作详细描述',
    operate_type          varchar(10) not null comment '操作类型',
    operate_user          varchar(10) not null comment '操作人员名称',
    request_uri           varchar(150) not null comment '请求uri',
    request_method        varchar(30) not null comment '请求方式',
    request_param         longtext comment '请求参数',
    response_param        longtext comment '返回参数',
    execute_method        varchar(150) default null comment '执行方法',
    operate_ip            varchar(50) not null comment '操作IP',
    operate_address       varchar(150) default null comment '操作地址',
    operate_device        varchar(100) not null comment '操作设备名称',
    operate_os            varchar(100) not null comment '操作设备操作系统名称',
    operate_browser       varchar(100) not null comment '操作浏览器名称',
    server_address        varchar(50) not null comment '服务器地址',
    execute_time          decimal(10,0) not null comment '执行时间(毫秒)',
    status                varchar(1) not null comment '操作状态（0：成功 1：失败）',
    message               longtext comment '操作信息',
    create_user           varchar(32) default null comment '创建人',
    create_time           datetime default null comment '创建时间',
    update_user           varchar(32) default null comment '修改人',
    update_time           datetime default null comment '修改时间',
    primary key (id)
);

alter table agile_sys_logger comment '用户操作日志表';


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
/* table: agile_online_rule 在线表单 规则配置                    */
/*==============================================================*/
CREATE TABLE agile_online_rule (
     id                            varchar(32) NOT NULL COMMENT '主键Id',
     rule_name                     varchar(64) NOT NULL COMMENT '规则名称',
     rule_type                     varchar(2) NOT NULL COMMENT '规则类型',
     pattern                       varchar(500) DEFAULT NULL COMMENT '规则表达式',
     create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
     create_time                   datetime DEFAULT NULL COMMENT '创建时间',
     update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
     update_time                   datetime DEFAULT NULL COMMENT '修改时间',
     PRIMARY KEY (id) USING BTREE
) ;
alter table agile_online_rule comment '在线表单 规则配置';

INSERT INTO agile_online_rule VALUES ('1', '只允许整数', '01', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_online_rule VALUES ('2', '只允许数字', '02', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_online_rule VALUES ('3', '只允许英文字符', '03', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_online_rule VALUES ('4', '范围验证', '04', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_online_rule VALUES ('5', '邮箱格式验证', '05', NULL, NULL, NULL, NULL, NULL);
INSERT INTO agile_online_rule VALUES ('6', '手机格式验证', '06', NULL, NULL, NULL, NULL, NULL);


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
      primary_column_id             varchar(32) NOT NULL COMMENT '表主键字段ID',
      primary_column_name           varchar(50) NOT NULL COMMENT '表主键字段名称',
      master_table_id               varchar(32) DEFAULT NULL COMMENT '主表ID',
      master_column_id              varchar(32) DEFAULT NULL COMMENT '主表字段ID',
      master_column_name            varchar(50) DEFAULT NULL COMMENT '主表字段名称',
      slave_column_id               varchar(32) DEFAULT NULL COMMENT '从表字段ID',
      slave_column_name             varchar(50) DEFAULT NULL COMMENT '从表字段称',
      left_join                     char(1) DEFAULT NULL COMMENT '是否左关联',
      cascade_delete                char(1) DEFAULT NULL COMMENT '是否级联删除',
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
       column_scale                  int DEFAULT NULL COMMENT '字段范围',
       column_default                varchar(50) DEFAULT NULL COMMENT '字段默认值',
       column_extra                  varchar(200) DEFAULT NULL COMMENT '字段扩展',
       column_type                   varchar(50) NOT NULL COMMENT '字段类型',
       data_type                     varchar(20) NOT NULL COMMENT '数据类型',
       primary_flag                  char(1) NOT NULL COMMENT '主键标识（0:否 1:是）',
       primary_type                  varchar(20) DEFAULT NULL COMMENT '主键类型',
       field_name                    varchar(100) NOT NULL COMMENT '数据对象数据名称',
       field_type                    varchar(50) NOT NULL COMMENT '数据对象数据类型',
       field_label                   varchar(100) NOT NULL COMMENT '数据对象显示标签',
       field_kind                    varchar(2) DEFAULT NULL COMMENT '数据对象字段分类',
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
/* table: agile_online_column_rule 在线表单 表字段规则配置       */
/*==============================================================*/
CREATE TABLE agile_online_column_rule (
            id                            varchar(32) NOT NULL COMMENT '主键ID',
            form_id                       varchar(32) NOT NULL COMMENT '在线表单主键ID',
            table_id                      varchar(32) NOT NULL COMMENT '数据表主键ID',
            column_id                     varchar(32) NOT NULL COMMENT '数据表字段ID',
            rule_id                       varchar(32) NOT NULL COMMENT '规则类型ID',
            rule_config                   varchar(50) NOT NULL COMMENT '规则配置',
            create_user                   varchar(32) DEFAULT NULL COMMENT '创建人',
            create_time                   datetime DEFAULT NULL COMMENT '创建时间',
            update_user                   varchar(32) DEFAULT NULL COMMENT '修改人',
            update_time                   datetime DEFAULT NULL COMMENT '修改时间',
            PRIMARY KEY (id)
);
alter table agile_online_column_rule comment '在线表单 表字段规则配置';

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



alter table agile_sys_role_menu add constraint fk_sys_user_menu_ref_menu foreign key (menu_id)
    references agile_sys_menu (id) on delete restrict on update restrict;

alter table agile_sys_role_menu add constraint fk_sys_user_menu_ref_role foreign key (role_id)
    references agile_sys_role (id) on delete restrict on update restrict;

alter table agile_sys_role_dept add constraint fk_sys_user_dept_ref_dept foreign key (dept_id)
    references agile_sys_dept (id) on delete restrict on update restrict;

alter table agile_sys_role_dept add constraint fk_sys_user_dept_ref_role foreign key (role_id)
    references agile_sys_role (id) on delete restrict on update restrict;

alter table agile_sys_user_post add constraint fk_sys_user_post_ref_post foreign key (post_id)
    references agile_sys_post (id) on delete restrict on update restrict;

alter table agile_sys_user_post add constraint fk_sys_user_post_ref_user foreign key (user_id)
    references agile_sys_user (id) on delete restrict on update restrict;

alter table agile_sys_user_role add constraint fk_sys_user_role_ref_role foreign key (role_id)
    references agile_sys_role (id) on delete restrict on update restrict;

alter table agile_sys_user_role add constraint fk_sys_user_role_ref_user foreign key (user_id)
    references agile_sys_user (id) on delete restrict on update restrict;

