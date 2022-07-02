drop table if exists agile_sys_tenant;

/*==============================================================*/
/* table: agile_sys_tenant租户信息表                            */
/*==============================================================*/
create table agile_sys_tenant
(
  id                   varchar(32) not null comment '租户主键',
  tenant_code          varchar(20) not null comment '租户编码',
  tenant_name          varchar(100) not null comment '租户名称',
  enable_status        char(1) not null comment '租户启用状态（0:正常 1:停用）',
  audit_status         char(1)  null comment '租户审核状态（0：审核中 1:审核通过 2:审核拒绝）',
  tenant_type          char(1)  null comment '租户类型（0:本地 1:远程）',
  expiration_date      datetime  null comment '到期时间',
  remark               varchar(300) default null comment '备注',
  tenant_id            varchar(32) not null comment '租户ID',
  create_user          varchar(32) default null comment '创建人',
  create_time          datetime default null comment '创建时间',
  update_user          varchar(32) default null comment '修改人',
  update_time         datetime default null comment '修改时间',
  primary key (id)
);

alter table agile_sys_tenant comment '租户信息表';

INSERT INTO agile_sys_tenant VALUES ('jeeagile', 'jeeagile', '默认租户', '0', '1', '0', null, '', 'jeeagile', NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('100','1','租户管理',1,'system/tenant/index','tenant','tenant','C','0','0','1','system:tenant:page','租户管理菜单',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10001','100','租户明细',1,'','','#','F','0','0','1','system:tenant:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10002','100','租户新增',2,'','','#','F','0','0','1','system:tenant:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10003','100','租户修改',3,'','','#','F','0','0','1','system:tenant:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10004','100','租户删除',4,'','','#','F','0','0','1','system:tenant:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10005','100','租户导入',5,'','','#','F','0','0','1','system:tenant:import','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10006','100','租户导出',6,'','','#','F','0','0','1','system:tenant:export','',NULL,NULL,NULL,NULL);


ALTER TABLE `agile`.`agile_sys_config`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `remark`;

update `agile`.`agile_sys_config` set tenant_id='jeeagile';

ALTER TABLE `agile`.`agile_sys_dept`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `remark`;

update `agile`.`agile_sys_dept` set tenant_id='jeeagile';

ALTER TABLE `agile`.`agile_sys_dict_type`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `remark`;

update `agile`.`agile_sys_dict_type` set tenant_id='jeeagile';

ALTER TABLE `agile`.`agile_sys_dict_data`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `remark`;

update `agile`.`agile_sys_dict_data` set tenant_id='jeeagile';

ALTER TABLE `agile`.`agile_sys_menu`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `remark`;

update `agile`.`agile_sys_menu` set tenant_id='jeeagile';

ALTER TABLE `agile`.`agile_sys_post`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `remark`;

update `agile`.`agile_sys_post` set tenant_id='jeeagile';

ALTER TABLE `agile`.`agile_sys_role`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `remark`;

update `agile`.`agile_sys_role` set tenant_id='jeeagile';

ALTER TABLE `agile`.`agile_sys_role_dept`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `dept_id`;

update `agile`.`agile_sys_role_dept` set tenant_id='jeeagile';

ALTER TABLE `agile`.`agile_sys_role_menu`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `menu_id`;

update `agile`.`agile_sys_role_menu` set tenant_id='jeeagile';

ALTER TABLE `agile`.`agile_sys_user`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `remark`;

update `agile`.`agile_sys_user` set tenant_id='jeeagile';

ALTER TABLE `agile`.`agile_sys_user_post`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `post_id`;

update `agile`.`agile_sys_user_post` set tenant_id='jeeagile';

ALTER TABLE `agile`.`agile_sys_user_role`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `role_id`;

update `agile`.`agile_sys_user_role` set tenant_id='jeeagile';

ALTER TABLE `agile`.`agile_sys_login`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `message`;

update `agile`.`agile_sys_login` set tenant_id='jeeagile';

ALTER TABLE `agile`.`agile_sys_logger`
ADD COLUMN `tenant_id` VARCHAR(32) not null COMMENT '租户ID' AFTER `message`;

update `agile`.`agile_sys_logger` set tenant_id='jeeagile';