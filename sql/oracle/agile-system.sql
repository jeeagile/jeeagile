alter table agile_sys_role_dept drop constraint fk_sys_user_dept_ref_dept;
alter table agile_sys_role_dept drop constraint fk_sys_user_dept_ref_role;
alter table agile_sys_role_menu drop constraint fk_sys_user_menu_ref_menu;
alter table agile_sys_role_menu drop constraint fk_sys_user_menu_ref_role;
alter table agile_sys_user_post drop constraint fk_sys_user_post_ref_post;
alter table agile_sys_user_post drop constraint fk_sys_user_post_ref_user;
alter table agile_sys_user_role drop constraint fk_sys_user_role_ref_role;
alter table agile_sys_user_role drop constraint fk_sys_user_role_ref_user;

drop table agile_sys_config cascade constraints;
drop table agile_sys_dept cascade constraints;
drop table agile_sys_dict_data cascade constraints;
drop table agile_sys_dict_type cascade constraints;
drop table agile_sys_menu cascade constraints;
drop table agile_sys_post cascade constraints;
drop table agile_sys_role cascade constraints;
drop table agile_sys_role_dept cascade constraints;
drop table agile_sys_role_menu cascade constraints;
drop table agile_sys_user cascade constraints;
drop table agile_sys_user_post cascade constraints;
drop table agile_sys_user_role cascade constraints;


/*==============================================================*/
/* Table: agile_sys_config 参数配置表                            */
/*==============================================================*/
create table agile_sys_config
(
   id                 VARCHAR2(32)         not null,
   config_name        VARCHAR2(100)        not null,
   config_key         VARCHAR2(100)        not null,
   config_value       VARCHAR2(300)        not null,
   system_flag        CHAR(1)              default '0' not null,
   remark             VARCHAR2(300),
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_sys_config primary key (id)
);

comment on table agile_sys_config is '参数配置表';

comment on column agile_sys_config.id is '参数主键id';
comment on column agile_sys_config.config_name is '参数名称';
comment on column agile_sys_config.config_key is '参数键名';
comment on column agile_sys_config.config_value is '参数键值';
comment on column agile_sys_config.system_flag is '系统内置标识（1：是 0：否）';
comment on column agile_sys_config.remark is '备注';
comment on column agile_sys_config.create_user is '创建人';
comment on column agile_sys_config.create_time is '创建时间';
comment on column agile_sys_config.update_user is '修改人';
comment on column agile_sys_config.update_time is '修改时间';

INSERT INTO agile_sys_config VALUES ('1','用户管理-账号初始密码','sys.user.pwd','123456','1','初始化密码 123456',NULL,NULL,NULL,NULL);
commit;

/*==============================================================*/
/* Table: agile_sys_dept 部门表                                 */
/*==============================================================*/
create table agile_sys_dept
(
   id                 VARCHAR2(32)         not null,
   parent_id          VARCHAR2(32)         default '0' not null,
   dept_name          VARCHAR2(100)        not null,
   dept_code          VARCHAR2(20)         not null,
   dept_sort          INTEGER              default 0 not null,
   dept_status        CHAR(1)              default '0' not null,
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_sys_dept primary key (id)
);

comment on table agile_sys_dept is '部门表';

comment on column agile_sys_dept.id is '部门id';
comment on column agile_sys_dept.parent_id is '上级部门id';
comment on column agile_sys_dept.dept_name is '部门名称';
comment on column agile_sys_dept.dept_code is '部门编码';
comment on column agile_sys_dept.dept_sort is '显示顺序';
comment on column agile_sys_dept.dept_status is '部门状态（0正常 1停用）';
comment on column agile_sys_dept.create_user is '创建人';
comment on column agile_sys_dept.create_time is '创建时间';
comment on column agile_sys_dept.update_user is '修改人';
comment on column agile_sys_dept.update_time is '修改时间';

INSERT INTO agile_sys_dept VALUES ('1','0','敏捷开发','jeeagile',0,'0',NULL,NULL,NULL,NULL);
commit;

/*==============================================================*/
/* Table: agile_sys_dict_type 字典类型表                        */
/*==============================================================*/
create table agile_sys_dict_type
(
   id                 VARCHAR2(32)         not null,
   dict_name          VARCHAR2(100)        not null,
   dict_type          VARCHAR2(100)        not null,
   dict_status        CHAR(1)              default '0' not null,
   system_flag        CHAR(1)              default '0',
   remark             VARCHAR2(300),
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_sys_dict_type primary key (id)
);

comment on table agile_sys_dict_type is '字典类型表';

comment on column agile_sys_dict_type.id is '字典主键id';
comment on column agile_sys_dict_type.dict_name is '字典名称';
comment on column agile_sys_dict_type.dict_type is '字典类型';
comment on column agile_sys_dict_type.dict_status is '状态（0正常 1停用）';
comment on column agile_sys_dict_type.system_flag is '系统内置标识（1：是 0：否）';
comment on column agile_sys_dict_type.remark is '备注';
comment on column agile_sys_dict_type.create_user is '创建人';
comment on column agile_sys_dict_type.create_time is '创建时间';
comment on column agile_sys_dict_type.update_user is '修改人';
comment on column agile_sys_dict_type.update_time is '修改时间';

INSERT INTO agile_sys_dict_type VALUES ('1','用户性别','sys_user_sex','0','0','用户性别列表',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('2','菜单状态','sys_show_visible','0','0','菜单状态列表',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('3','系统开关','sys_normal_disable','0','0','系统开关列表',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('4','任务状态','sys_job_status','0','0','任务状态列表',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('5','系统是否','sys_yes_no','0','0','系统是否列表',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('6','数据范围','sys_data_scope','0','0','数据范围列表',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('7','系统状态','sys_common_status','0','0','登录状态列表',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('8','日志状态','sys_logger_status','0','0','日志状态列表',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('9','日志类型','sys_logger_type','0','0','日志类型列表',NULL,NULL,NULL,NULL);
commit;

/*==============================================================*/
/* Table: agile_sys_dict_data 字典数据表                         */
/*==============================================================*/
create table agile_sys_dict_data
(
   id                 VARCHAR2(32)         not null,
   parent_id          VARCHAR2(32)         default '0' not null,
   dict_sort          INTEGER              default 0 not null,
   dict_label         VARCHAR2(100)        not null,
   dict_value         VARCHAR2(100)        not null,
   dict_type          VARCHAR2(100)        not null,
   dict_status        CHAR(1)              default '0' not null,
   system_flag        CHAR(1)              default '0' not null,
   remark             VARCHAR2(300),
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_sys_dict_data primary key (id)
);

comment on table agile_sys_dict_data is '字典数据表';

comment on column agile_sys_dict_data.id is '字典id';
comment on column agile_sys_dict_data.parent_id is '上级字典id';
comment on column agile_sys_dict_data.dict_sort is '字典排序';
comment on column agile_sys_dict_data.dict_label is '字典标签';
comment on column agile_sys_dict_data.dict_value is '字典键值';
comment on column agile_sys_dict_data.dict_type is '字典类型';
comment on column agile_sys_dict_data.dict_status is '字典状态（0正常 1停用）';
comment on column agile_sys_dict_data.system_flag is '系统内置标识（1：是 0：否）';
comment on column agile_sys_dict_data.remark is '备注';
comment on column agile_sys_dict_data.create_user is '创建人';
comment on column agile_sys_dict_data.create_time is '创建时间';
comment on column agile_sys_dict_data.update_user is '修改人';
comment on column agile_sys_dict_data.update_time is '修改时间';

INSERT INTO agile_sys_dict_data VALUES ('11','0',1,'男','0','sys_user_sex','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('12','0',2,'女','1','sys_user_sex','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('13','0',3,'未知','2','sys_user_sex','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('21','0',1,'显示','0','sys_show_visible','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('22','0',2,'隐藏','1','sys_show_visible','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('31','0',1,'正常','0','sys_normal_disable','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('32','0',2,'停用','1','sys_normal_disable','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('41','0',1,'启用','0','sys_job_status','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('42','0',2,'暂停','1','sys_job_status','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('51','0',1,'是','1','sys_yes_no','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('52','0',2,'否','0','sys_yes_no','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('61','0',0,'全部数据权限','01','sys_data_scope','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('62','0',1,'本部门数据权限','02','sys_data_scope','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('63','0',3,'本部门及以下数据权限','03','sys_data_scope','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('64','0',4,'仅本人数据权限','04','sys_data_scope','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('65','0',5,'自定义部门数据权限','05','sys_data_scope','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('71','0',1,'成功','0','sys_common_status','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('72','0',2,'失败','1','sys_common_status','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('81','0',0,'成功','0','sys_logger_status','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('82','0',1,'失败','1','sys_logger_status','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('91','0',1,'查询','SELECT','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('92','0',2,'查看','DETAIL','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('93','0',3,'新增','ADD','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('94','0',4,'修改','UPDATE','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('95','0',5,'删除','DELETE','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('96','0',6,'授权','GRANT','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('97','0',7,'导出','EXPORT','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('98','0',8,'导入','IMPORT','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('99','0',9,'清空','CLEAR','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('910','0',10,'强退','FORCE','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('911','0',11,'生成代码','GEN','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('912','0',12,'其他','OTHER','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
commit;

/*==============================================================*/
/* Table: agile_sys_menu 菜单权限表                             */
/*==============================================================*/
create table agile_sys_menu
(
   id                 VARCHAR2(32)         not null,
   parent_id          VARCHAR2(32)         default '0' not null,
   menu_name          VARCHAR2(100)        not null,
   menu_sort          INTEGER              default 0 not null,
   menu_comp          VARCHAR2(200),
   menu_path          VARCHAR2(200),
   menu_icon          VARCHAR2(100),
   menu_type          CHAR(1)              not null,
   menu_visible       CHAR(1)              default '0' not null,
   menu_status        CHAR(1)              default '0' not null,
   menu_frame         INTEGER              default 1 not null,
   menu_perm          VARCHAR2(100),
   remark             VARCHAR2(300),
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_sys_menu primary key (id)
);

comment on table agile_sys_menu is '菜单权限表';

comment on column agile_sys_menu.id is '菜单主键id';
comment on column agile_sys_menu.parent_id is '父级菜单id';
comment on column agile_sys_menu.menu_name is '菜单名称';
comment on column agile_sys_menu.menu_sort is '显示顺序';
comment on column agile_sys_menu.menu_comp is '组件路径';
comment on column agile_sys_menu.menu_path is '路由地址';
comment on column agile_sys_menu.menu_icon is '菜单图标';
comment on column agile_sys_menu.menu_type is '菜单类型（m目录 c菜单 f按钮）';
comment on column agile_sys_menu.menu_visible is '菜单显示状态（0显示 1隐藏）';
comment on column agile_sys_menu.menu_status is '菜单状态（0正常 1停用）';
comment on column agile_sys_menu.menu_frame is '外链标识（0是 1否）';
comment on column agile_sys_menu.menu_perm is '权限标识';
comment on column agile_sys_menu.remark is '备注';
comment on column agile_sys_menu.create_user is '创建人';
comment on column agile_sys_menu.create_time is '创建时间';
comment on column agile_sys_menu.update_user is '修改人';
comment on column agile_sys_menu.update_time is '修改时间';

INSERT INTO agile_sys_menu VALUES ('1','0','系统管理',1,'','system','system','M','0','0',1,'','系统管理目录',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('101','1','用户管理',1,'system/user/index','user','user','C','0','0',1,'system:user:list','用户管理菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10101','101','用户新增',1,'','','#','F','0','0',1,'system:user:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10102','101','用户修改',2,'','','#','F','0','0',1,'system:user:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10103','101','用户删除',3,'','','#','F','0','0',1,'system:user:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10104','101','重置密码',4,'','','#','F','0','0',1,'system:user:resetPwd','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('102','1','角色管理',2,'system/role/index','role','role','C','0','0',1,'system:role:list','角色管理菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10201','102','角色新增',1,'','','#','F','0','0',1,'system:role:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10202','102','角色修改',2,'','','#','F','0','0',1,'system:role:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10203','102','角色删除',3,'','','#','F','0','0',1,'system:role:delete','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('103','1','菜单管理',3,'system/menu/index','menu','menu','C','0','0',1,'system:menu:list','菜单管理菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10301','103','菜单新增',1,'','','#','F','0','0',1,'system:menu:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10302','103','菜单修改',2,'','','#','F','0','0',1,'system:menu:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10303','103','菜单删除',3,'','','#','F','0','0',1,'system:menu:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10304','103','菜单排序',4,'','',NULL,'F','0','0',1,'system:menu:sort','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('104','1','部门管理',4,'system/dept/index','dept','dept','C','0','0',1,'system:dept:list','部门管理菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10401','104','部门新增',1,'','','#','F','0','0',1,'system:dept:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10402','104','部门修改',2,'','','#','F','0','0',1,'system:dept:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10403','104','部门删除',3,'','','#','F','0','0',1,'system:dept:delete','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('105','1','岗位管理',5,'system/post/index','post','post','C','0','0',1,'system:post:list','岗位管理菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10501','105','岗位新增',1,'','','#','F','0','0',1,'system:post:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10502','105','岗位修改',2,'','','#','F','0','0',1,'system:post:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10503','105','岗位删除',3,'','','#','F','0','0',1,'system:post:delete','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('106','1','字典管理',6,'system/dict/index','dict','dict','C','0','0',1,'system:dict:list','字典管理菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10601','106','字典新增',1,'','#','#','F','0','0',1,'system:dict:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10602','106','字典修改',2,'','#','#','F','0','0',1,'system:dict:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10603','106','字典删除',3,'','#','#','F','0','0',1,'system:dict:delete','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('107','1','参数设置',7,'system/config/index','config','config','C','0','0',1,'system:config:list','参数设置菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10701','107','参数新增',1,'','#','#','F','0','0',1,'system:config:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10702','107','参数修改',2,'','#','#','F','0','0',1,'system:config:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10703','107','参数删除',3,'','#','#','F','0','0',1,'system:config:delete','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('2','0','系统监控',2,NULL,'monitor','monitor','M','0','0',1,'','系统监控目录',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('201','2','在线用户',1,'monitor/online/index','online','online','C','0','0',1,'monitor:online:list','在线用户菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('20101','201','批量强退',1,'','#','#','F','0','0',1,'monitor:online:batchLogout','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('20102','201','单户强退',2,'','#','#','F','0','0',1,'monitor:online:forceLogout','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('202','2','数据监控',2,'monitor/druid/index','druid','druid','C','0','0',1,'monitor:druid:list','数据监控菜单',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('203','2','服务监控',3,'monitor/server/index','server','server','C','0','0',1,'monitor:server:info','服务监控菜单',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('3','0','系统工具',3,NULL,'tool','tool','M','0','0',1,'','系统工具目录',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('301','3','表单构建',1,'tool/form/index','form','form','C','0','0',1,'tool:form:list','表单构建菜单',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('302','3','代码生成',2,'tool/generator/index','generator','generator','C','0','0',1,'tool:gen:list','代码生成菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30201','302','生成查询',1,'','#','#','F','0','0',1,'tool:generator:query','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30202','302','生成修改',2,'','#','#','F','0','0',1,'tool:generator:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30203','302','生成删除',3,'','#','#','F','0','0',1,'tool:generator:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30204','302','导入代码',2,'','#','#','F','0','0',1,'tool:generator:import','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30205','302','预览代码',4,'','#','#','F','0','0',1,'tool:generator:preview','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30206','302','生成代码',5,'','#','#','F','0','0',1,'tool:generator:code','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('303','3','系统接口',3,'tool/swagger/index','swagger','swagger','C','0','0',1,'tool:swagger:list','系统接口菜单',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('4','0','任务管理',4,'','quartz','job','M','0','0',1,'','任务管理目录',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('401','4','定时任务',1,'quartz/job/index','job','job','C','0','0',1,'quartz:job:list','定时任务菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40101','401','任务新增',1,'','#','#','F','0','0',1,'quartz:job:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40102','401','任务修改',2,'','#','#','F','0','0',1,'quartz:job:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40103','401','任务删除',3,'','#','#','F','0','0',1,'quartz:job:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40104','401','状态修改',4,'','#','#','F','0','0',1,'quartz:job:status','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('5','0','日志管理',5,'','logger','log','M','0','0',1,'','日志管理目录',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('501','5','操作日志',1,'logger/operate/index','operate','operate','C','0','0',1,'logger:operate:list','操作日志菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50101','501','查看',1,'','','','F','0','0',1,'logger:operate:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50102','501','清空',2,'','','','F','0','0',1,'logger:operate:clear','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50103','501','删除',3,'','#','#','F','0','0',1,'logger:operate:delete','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('502','5','登录日志',2,'logger/login/index','login','login','C','0','0',1,'logger:login:list','登录日志菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50201','502','清空',1,'','','','F','0','0',1,'logger:login:clear','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50202','502','删除',2,'','#','#','F','0','0',1,'logger:login:delete','',NULL,NULL,NULL,NULL);
commit;

/*==============================================================*/
/* Table: agile_sys_post 岗位信息表                              */
/*==============================================================*/
create table agile_sys_post
(
   id                 VARCHAR2(32)         not null,
   post_code          VARCHAR2(20)         not null,
   post_name          VARCHAR2(100)        not null,
   post_sort          INTEGER              not null,
   post_status        CHAR(1)              not null,
   remark             VARCHAR2(300),
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_sys_post primary key (id)
);

comment on table agile_sys_post is '岗位信息表';

comment on column agile_sys_post.id is '岗位主键id';
comment on column agile_sys_post.post_code is '岗位编码';
comment on column agile_sys_post.post_name is '岗位名称';
comment on column agile_sys_post.post_sort is '显示顺序';
comment on column agile_sys_post.post_status is '状态（0正常 1停用）';
comment on column agile_sys_post.remark is '备注';
comment on column agile_sys_post.create_user is '创建人';
comment on column agile_sys_post.create_time is '创建时间';
comment on column agile_sys_post.update_user is '修改人';
comment on column agile_sys_post.update_time is '修改时间';

INSERT INTO agile_sys_post VALUES ('1','jeeagile','JeeAgile',0,'0',NULL,NULL,NULL,NULL,NULL);
commit;

/*==============================================================*/
/* Table: agile_sys_role 角色信息表                              */
/*==============================================================*/
create table agile_sys_role
(
   id                 VARCHAR2(32)         not null,
   role_name          VARCHAR2(30)         not null,
   role_code          VARCHAR2(100)        not null,
   role_sort          INTEGER              not null,
   role_status        CHAR(1)              not null,
   data_scope         CHAR(2)              default '01' not null,
   remark             VARCHAR2(300),
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_sys_role primary key (id)
);

comment on table agile_sys_role is '角色信息表';

comment on column agile_sys_role.id is '角色主键id';
comment on column agile_sys_role.role_name is '角色名称';
comment on column agile_sys_role.role_code is '角色编码';
comment on column agile_sys_role.role_sort is '显示顺序';
comment on column agile_sys_role.role_status is '角色状态（0正常 1停用）';
comment on column agile_sys_role.data_scope is '数据范围 值参见字典sys_data_scope';
comment on column agile_sys_role.remark is '备注';
comment on column agile_sys_role.create_user is '创建人';
comment on column agile_sys_role.create_time is '创建时间';
comment on column agile_sys_role.update_user is '修改人';
comment on column agile_sys_role.update_time is '修改时间';

INSERT INTO agile_sys_role VALUES ('1','jeeagile','JeeAgile',0,'0','02',NULL,NULL,NULL,NULL,NULL);
commit;

/*==============================================================*/
/* Table: agile_sys_role_dept 角色部门关联表                    */
/*==============================================================*/
create table agile_sys_role_dept
(
   id                 VARCHAR2(32)         not null,
   role_id            VARCHAR2(32)         not null,
   dept_id            VARCHAR2(32)         not null,
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_sys_role_dept primary key (id)
);

comment on table agile_sys_role_dept is '角色部门关联表';

comment on column agile_sys_role_dept.id is '角色部门主键id';
comment on column agile_sys_role_dept.role_id is '角色id';
comment on column agile_sys_role_dept.dept_id is '部门id';
comment on column agile_sys_role_dept.create_user is '创建人';
comment on column agile_sys_role_dept.create_time is '创建时间';
comment on column agile_sys_role_dept.update_user is '修改人';
comment on column agile_sys_role_dept.update_time is '修改时间';

/*==============================================================*/
/* Table: agile_sys_role_menu 角色菜单关联表                     */
/*==============================================================*/
create table agile_sys_role_menu
(
   id                 VARCHAR2(32)         not null,
   role_id            VARCHAR2(32)         not null,
   menu_id            VARCHAR2(32)         not null,
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_sys_role_menu primary key (id)
);

comment on table agile_sys_role_menu is '角色菜单关联表';

comment on column agile_sys_role_menu.id is '角色菜单主键id';
comment on column agile_sys_role_menu.role_id is '角色id';
comment on column agile_sys_role_menu.menu_id is '菜单id';
comment on column agile_sys_role_menu.create_user is '创建人';
comment on column agile_sys_role_menu.create_time is '创建时间';
comment on column agile_sys_role_menu.update_user is '修改人';
comment on column agile_sys_role_menu.update_time is '修改时间';

INSERT INTO agile_sys_role_menu VALUES ('1','1','1',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('2','1','101',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('3','1','10101',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('4','1','10102',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('5','1','10103',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('6','1','10104',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('7','1','102',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('8','1','10201',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('9','1','10202',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('10','1','10203',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('11','1','103',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('12','1','10301',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('13','1','10302',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('14','1','10303',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('15','1','10304',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('16','1','104',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('17','1','10401',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('18','1','10402',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('19','1','10403',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('20','1','105',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('21','1','10501',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('22','1','10502',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('23','1','10503',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('24','1','106',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('25','1','10601',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('26','1','10602',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('27','1','10603',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('28','1','107',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('29','1','10701',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('30','1','10702',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('31','1','10703',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('32','1','2',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('33','1','201',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('34','1','20101',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('35','1','20102',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('36','1','202',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('37','1','203',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('38','1','5',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('39','1','501',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('40','1','50101',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('41','1','50102',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('42','1','50103',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('43','1','502',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('44','1','50201',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_role_menu VALUES ('45','1','50202',NULL,NULL,NULL,NULL);
commit;

/*==============================================================*/
/* Table: agile_sys_user 用户信息表                              */
/*==============================================================*/
create table agile_sys_user
(
   id                 VARCHAR2(32)         not null,
   user_name          VARCHAR2(30)         not null,
   nick_name          VARCHAR2(100),
   user_sex           CHAR(1),
   user_sort          INTEGER              not null,
   user_phone         VARCHAR2(20),
   user_mobile        VARCHAR2(12),
   user_address       VARCHAR2(200),
   user_email         VARCHAR2(50),
   user_avatar        VARCHAR2(100),
   user_status        CHAR(1)              default '0' not null,
   user_pwd           VARCHAR2(100)        not null,
   dept_id            VARCHAR2(32),
   remark             VARCHAR2(300),
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_sys_user primary key (id)
);

comment on table agile_sys_user is '用户信息表';

comment on column agile_sys_user.id is '用户主键id';
comment on column agile_sys_user.user_name is '用户登录名';
comment on column agile_sys_user.nick_name is '用户昵称';
comment on column agile_sys_user.user_sex is '用户性别';
comment on column agile_sys_user.user_sort is '显示顺序';
comment on column agile_sys_user.user_phone is '联系电话';
comment on column agile_sys_user.user_mobile is '联系手机';
comment on column agile_sys_user.user_address is '联系地址';
comment on column agile_sys_user.user_email is '邮箱地址';
comment on column agile_sys_user.user_avatar is '头像地址';
comment on column agile_sys_user.user_status is '用户状态（0:正常 2:停用 3:冻结）';
comment on column agile_sys_user.user_pwd is '用户密码';
comment on column agile_sys_user.dept_id is '部门主键id';
comment on column agile_sys_user.remark is '备注';
comment on column agile_sys_user.create_user is '创建人';
comment on column agile_sys_user.create_time is '创建时间';
comment on column agile_sys_user.update_user is '修改人';
comment on column agile_sys_user.update_time is '修改时间';

INSERT INTO agile_sys_user VALUES ('0','admin','管理员','1',0,'18600000000','18600000000',NULL,'admin@163.com',NULL,'0','e10adc3949ba59abbe56e057f20f883e','',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_user VALUES ('1','jeeagile','JeeAgile','1',1,'18600000000','18600000000',NULL,'jeeagile@163.com','','0','e10adc3949ba59abbe56e057f20f883e','1',NULL,NULL,NULL,NULL,NULL);
commit;

/*==============================================================*/
/* Table: agile_sys_user_post 用户岗位关联表                     */
/*==============================================================*/
create table agile_sys_user_post
(
   id                 VARCHAR2(32)         not null,
   user_id            VARCHAR2(32)         not null,
   post_id            VARCHAR2(32)         not null,
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_sys_user_post primary key (id)
);

comment on table agile_sys_user_post is '用户岗位关联表';

comment on column agile_sys_user_post.id is '用户岗位主键id';
comment on column agile_sys_user_post.user_id is '用户id';
comment on column agile_sys_user_post.post_id is '岗位id';
comment on column agile_sys_user_post.create_user is '创建人';
comment on column agile_sys_user_post.create_time is '创建时间';
comment on column agile_sys_user_post.update_user is '修改人';
comment on column agile_sys_user_post.update_time is '修改时间';

INSERT INTO agile_sys_user_post VALUES ('1','1','1',NULL,NULL,NULL,NULL);
commit;

/*==============================================================*/
/* Table: agile_sys_user_role 用户角色关联表                     */
/*==============================================================*/
create table agile_sys_user_role
(
   id                 VARCHAR2(32)         not null,
   user_id            VARCHAR2(32)         not null,
   role_id            VARCHAR2(32)         not null,
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_sys_user_role primary key (id)
);

comment on table agile_sys_user_role is '用户角色关联表';

comment on column agile_sys_user_role.id is '用户角色主键id';
comment on column agile_sys_user_role.user_id is '用户id';
comment on column agile_sys_user_role.role_id is '角色id';
comment on column agile_sys_user_role.create_user is '创建人';
comment on column agile_sys_user_role.create_time is '创建时间';
comment on column agile_sys_user_role.update_user is '修改人';
comment on column agile_sys_user_role.update_time is '修改时间';

INSERT INTO agile_sys_user_role VALUES ('1','1','1',NULL,NULL,NULL,NULL);
commit;

alter table agile_sys_role_dept
   add constraint fk_sys_user_dept_ref_dept foreign key (dept_id)
      references agile_sys_dept (id);

alter table agile_sys_role_dept
   add constraint fk_sys_user_dept_ref_role foreign key (role_id)
      references agile_sys_role (id);

alter table agile_sys_role_menu
   add constraint fk_sys_user_menu_ref_menu foreign key (menu_id)
      references agile_sys_menu (id);

alter table agile_sys_role_menu
   add constraint fk_sys_user_menu_ref_role foreign key (role_id)
      references agile_sys_role (id);

alter table agile_sys_user_post
   add constraint fk_sys_user_post_ref_post foreign key (post_id)
      references agile_sys_post (id);

alter table agile_sys_user_post
   add constraint fk_sys_user_post_ref_user foreign key (user_id)
      references agile_sys_user (id);

alter table agile_sys_user_role
   add constraint fk_sys_user_role_ref_role foreign key (role_id)
      references agile_sys_role (id);

alter table agile_sys_user_role
   add constraint fk_sys_user_role_ref_user foreign key (user_id)
      references agile_sys_user (id);


