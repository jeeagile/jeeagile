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
  create_user          varchar(32) default null comment '创建人',
  create_time          datetime default null comment '创建时间',
  update_user          varchar(32) default null comment '修改人',
  update_time         datetime default null comment '修改时间',
  primary key (id)
);

alter table agile_sys_tenant comment '租户信息表';