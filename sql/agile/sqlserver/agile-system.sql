if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('agile_sys_role_dept') and o.name = 'fk_sys_user_dept_ref_dept')
alter table agile_sys_role_dept
   drop constraint fk_sys_user_dept_ref_dept
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('agile_sys_role_dept') and o.name = 'fk_sys_user_dept_ref_role')
alter table agile_sys_role_dept
   drop constraint fk_sys_user_dept_ref_role
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('agile_sys_role_menu') and o.name = 'fk_sys_user_menu_ref_menu')
alter table agile_sys_role_menu
   drop constraint fk_sys_user_menu_ref_menu
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('agile_sys_role_menu') and o.name = 'fk_sys_user_menu_ref_role')
alter table agile_sys_role_menu
   drop constraint fk_sys_user_menu_ref_role
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('agile_sys_user_post') and o.name = 'fk_sys_user_post_ref_post')
alter table agile_sys_user_post
   drop constraint fk_sys_user_post_ref_post
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('agile_sys_user_post') and o.name = 'fk_sys_user_post_ref_user')
alter table agile_sys_user_post
   drop constraint fk_sys_user_post_ref_user
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('agile_sys_user_role') and o.name = 'fk_sys_user_role_ref_role')
alter table agile_sys_user_role
   drop constraint fk_sys_user_role_ref_role
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('agile_sys_user_role') and o.name = 'fk_sys_user_role_ref_user')
alter table agile_sys_user_role
   drop constraint fk_sys_user_role_ref_user
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_config')
            and   type = 'U')
   drop table agile_sys_config
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_dept')
            and   type = 'U')
   drop table agile_sys_dept
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_dict_data')
            and   type = 'U')
   drop table agile_sys_dict_data
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_dict_type')
            and   type = 'U')
   drop table agile_sys_dict_type
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_menu')
            and   type = 'U')
   drop table agile_sys_menu
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_post')
            and   type = 'U')
   drop table agile_sys_post
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_role')
            and   type = 'U')
   drop table agile_sys_role
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_role_dept')
            and   type = 'U')
   drop table agile_sys_role_dept
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_role_menu')
            and   type = 'U')
   drop table agile_sys_role_menu
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_user')
            and   type = 'U')
   drop table agile_sys_user
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_user_post')
            and   type = 'U')
   drop table agile_sys_user_post
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_user_role')
            and   type = 'U')
   drop table agile_sys_user_role
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_login')
            and   type = 'U')
   drop table agile_sys_login
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_sys_logger')
            and   type = 'U')
   drop table agile_sys_logger
go

/*==============================================================*/
/* Table: agile_sys_config ???????????????                            */
/*==============================================================*/
create table agile_sys_config (
   id                   varchar(32)          not null,
   config_name          varchar(100)         not null,
   config_key           varchar(100)         not null,
   config_value         varchar(300)         not null,
   system_flag          char(1)              not null default '0',
   remark               varchar(300)         null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_sys_config primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????',
   'user', @CurrentUser, 'table', 'agile_sys_config'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_config', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_config', 'column', 'config_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_config', 'column', 'config_key'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_config', 'column', 'config_value'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????0:??? 1:??????',
   'user', @CurrentUser, 'table', 'agile_sys_config', 'column', 'system_flag'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????',
   'user', @CurrentUser, 'table', 'agile_sys_config', 'column', 'remark'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_config', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_config', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_config', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_config', 'column', 'update_time'
go

INSERT INTO agile_sys_config VALUES ('1','????????????-??????????????????','sys.user.pwd','123456','1','??????????????? 123456',NULL,NULL,NULL,NULL);

/*==============================================================*/
/* Table: agile_sys_dept ?????????                                  */
/*==============================================================*/
create table agile_sys_dept (
   id                   varchar(32)          not null,
   parent_id            varchar(32)          not null default '0',
   dept_name            varchar(100)         not null,
   dept_code            varchar(20)          not null,
   dept_sort            int                  not null default 0,
   dept_status          char(1)              not null default '0',
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_sys_dept primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dept'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dept', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dept', 'column', 'parent_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dept', 'column', 'dept_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dept', 'column', 'dept_code'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dept', 'column', 'dept_sort'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????0:?????? 1:?????????',
   'user', @CurrentUser, 'table', 'agile_sys_dept', 'column', 'dept_status'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_dept', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dept', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_dept', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dept', 'column', 'update_time'
go

INSERT INTO agile_sys_dept VALUES ('1','0','????????????','jeeagile',0,'0',NULL,NULL,NULL,NULL);


/*==============================================================*/
/* Table: agile_sys_dict_type ???????????????                         */
/*==============================================================*/
create table agile_sys_dict_type (
   id                   varchar(32)          not null,
   dict_name            varchar(100)         not null,
   dict_type            varchar(100)         not null,
   dict_status          char(1)              not null default '0',
   system_flag          char(1)              null default '0',
   remark               varchar(300)         null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_sys_dict_type primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_type'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_type', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_type', 'column', 'dict_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_type', 'column', 'dict_type'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????0:?????? 1:?????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_type', 'column', 'dict_status'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????0:??? 1:??????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_type', 'column', 'system_flag'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_type', 'column', 'remark'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_type', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_type', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_type', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_type', 'column', 'update_time'
go

INSERT INTO agile_sys_dict_type VALUES ('1','????????????','sys_user_sex','0','0','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('2','????????????','sys_show_visible','0','0','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('3','????????????','sys_normal_disable','0','0','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('4','????????????','sys_job_status','0','0','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('5','????????????','sys_yes_no','0','0','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('6','????????????','sys_data_scope','0','0','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('7','????????????','sys_common_status','0','0','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('8','????????????','sys_logger_status','0','0','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_type VALUES ('9','????????????','sys_logger_type','0','0','??????????????????',NULL,NULL,NULL,NULL);

/*==============================================================*/
/* Table: agile_sys_dict_data ???????????????                         */
/*==============================================================*/
create table agile_sys_dict_data (
   id                   varchar(32)          not null,
   parent_id            varchar(32)          not null default '0',
   dict_sort            int                  not null default 0,
   dict_label           varchar(100)         not null,
   dict_value           varchar(100)         not null,
   dict_type            varchar(100)         not null,
   dict_status          char(1)              not null default '0',
   system_flag          char(1)              not null default '0',
   remark               varchar(300)         null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_sys_dict_data primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data', 'column', 'parent_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data', 'column', 'dict_sort'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data', 'column', 'dict_label'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data', 'column', 'dict_value'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data', 'column', 'dict_type'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????0:?????? 1:?????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data', 'column', 'dict_status'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????0:??? 1:??????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data', 'column', 'system_flag'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data', 'column', 'remark'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_dict_data', 'column', 'update_time'
go


INSERT INTO agile_sys_dict_data VALUES ('11','0',1,'???','0','sys_user_sex','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('12','0',2,'???','1','sys_user_sex','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('13','0',3,'??????','2','sys_user_sex','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('21','0',1,'??????','0','sys_show_visible','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('22','0',2,'??????','1','sys_show_visible','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('31','0',1,'??????','0','sys_normal_disable','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('32','0',2,'??????','1','sys_normal_disable','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('41','0',1,'??????','0','sys_job_status','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('42','0',2,'??????','1','sys_job_status','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('51','0',1,'???','1','sys_yes_no','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('52','0',2,'???','0','sys_yes_no','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('61','0',0,'??????????????????','01','sys_data_scope','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('62','0',1,'?????????????????????','02','sys_data_scope','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('63','0',3,'??????????????????????????????','03','sys_data_scope','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('64','0',4,'?????????????????????','04','sys_data_scope','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('65','0',5,'???????????????????????????','05','sys_data_scope','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('71','0',1,'??????','0','sys_common_status','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('72','0',2,'??????','1','sys_common_status','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('81','0',0,'??????','0','sys_logger_status','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('82','0',1,'??????','1','sys_logger_status','0','0',NULL,NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_dict_data VALUES ('91','0',1,'????????????','SELECT','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('92','0',2,'????????????','DETAIL','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('93','0',3,'????????????','ADD','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('94','0',4,'????????????','UPDATE','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('95','0',5,'????????????','DELETE','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('96','0',6,'????????????','GRANT','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('97','0',7,'????????????','EXPORT','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('98','0',8,'????????????','IMPORT','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('99','0',9,'????????????','CLEAR','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('910','0',10,'????????????','FORCE','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('911','0',11,'????????????','GENERATOR','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_dict_data VALUES ('912','0',12,'????????????','OTHER','sys_logger_type','0','0',NULL,NULL,NULL,NULL,NULL);

/*==============================================================*/
/* Table: agile_sys_menu ???????????????                              */
/*==============================================================*/
create table agile_sys_menu (
   id                   varchar(32)          not null,
   parent_id            varchar(32)          not null default '0',
   menu_name            varchar(100)         not null,
   menu_sort            int                  not null default 0,
   menu_comp            varchar(200)         null,
   menu_path            varchar(200)         null,
   menu_icon            varchar(100)         null,
   menu_type            char(1)              not null,
   menu_visible         char(1)              not null default '0',
   menu_status          char(1)              not null default '0',
   menu_frame           char(1)              not null default '1',
   menu_perm            varchar(100)         null,
   remark               varchar(300)         null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_sys_menu primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'parent_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'menu_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'menu_sort'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'menu_comp'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'menu_path'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'menu_icon'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????M:?????? C:?????? F:?????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'menu_type'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????????????????0:?????? 1:?????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'menu_visible'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????0:?????? 1:?????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'menu_status'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????0:??? 1:??????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'menu_frame'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'menu_perm'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'remark'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_menu', 'column', 'update_time'
go

INSERT INTO agile_sys_menu VALUES ('1','0','????????????',1,'','system','system','M','0','0','1','','??????????????????',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('101','1','????????????',1,'system/user/index','user','user','C','0','0','1','system:user:page','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10101','101','????????????',1,'','','#','F','0','0','1','system:user:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10102','101','????????????',2,'','','#','F','0','0','1','system:user:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10103','101','????????????',3,'','','#','F','0','0','1','system:user:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10104','101','????????????',4,'','','#','F','0','0','1','system:user:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10105','101','????????????',5,'','','#','F','0','0','1','system:user:password','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10106','101','????????????',6,'','','#','F','0','0','1','system:user:import','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10107','101','????????????',7,'','','#','F','0','0','1','system:user:export','',NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('102','1','????????????',2,'system/role/index','role','role','C','0','0','1','system:role:page','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10201','102','????????????',1,'','','#','F','0','0','1','system:role:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10202','102','????????????',2,'','','#','F','0','0','1','system:role:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10203','102','????????????',3,'','','#','F','0','0','1','system:role:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10204','102','????????????',4,'','','#','F','0','0','1','system:role:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10205','102','????????????',5,'','','#','F','0','0','1','system:role:import','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10206','102','????????????',6,'','','#','F','0','0','1','system:role:export','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('103','1','????????????',3,'system/menu/index','menu','menu','C','0','0','1','system:menu:list','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10301','103','????????????',1,'','','#','F','0','0','1','system:menu:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10302','103','????????????',2,'','','#','F','0','0','1','system:menu:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10303','103','????????????',3,'','','#','F','0','0','1','system:menu:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10304','103','????????????',4,'','','#','F','0','0','1','system:menu:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10305','103','????????????',5,'','','#','F','0','0','1','system:menu:import','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10306','103','????????????',6,'','','#','F','0','0','1','system:menu:export','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10307','103','????????????',7,'','','#','F','0','0','1','system:menu:sort','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('104','1','????????????',4,'system/dept/index','dept','dept','C','0','0','1','system:dept:list','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10401','104','????????????',1,'','','#','F','0','0','1','system:dept:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10402','104','????????????',2,'','','#','F','0','0','1','system:dept:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10403','104','????????????',3,'','','#','F','0','0','1','system:dept:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10404','104','????????????',4,'','','#','F','0','0','1','system:dept:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10405','104','????????????',5,'','','#','F','0','0','1','system:dept:import','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10406','104','????????????',6,'','','#','F','0','0','1','system:dept:export','',NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('105','1','????????????',5,'system/post/index','post','post','C','0','0','1','system:post:page','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10501','105','????????????',1,'','','#','F','0','0','1','system:post:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10502','105','????????????',2,'','','#','F','0','0','1','system:post:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10503','105','????????????',3,'','','#','F','0','0','1','system:post:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10504','105','????????????',4,'','','#','F','0','0','1','system:post:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10505','105','????????????',5,'','','#','F','0','0','1','system:post:import','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10506','105','????????????',6,'','','#','F','0','0','1','system:post:export','',NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('106','1','????????????',6,'system/dict/index','dict','dict','C','0','0','1','system:dict:type:page','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10601','106','????????????',1,'','','#','F','0','0','1','system:dict:type:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10602','106','????????????',2,'','','#','F','0','0','1','system:dict:type:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10603','106','????????????',3,'','','#','F','0','0','1','system:dict:type:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10604','106','????????????',4,'','','#','F','0','0','1','system:dict:type:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10605','106','????????????',5,'','','#','F','0','0','1','system:dict:type:import','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10606','106','????????????',6,'','','#','F','0','0','1','system:dict:type:export','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10607','106','??????????????????',7,'','','#','F','0','0','1','system:dict:data:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10608','106','??????????????????',8,'','','#','F','0','0','1','system:dict:data:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10609','106','??????????????????',9,'','','#','F','0','0','1','system:dict:data:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10610','106','??????????????????',10,'','','#','F','0','0','1','system:dict:data:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10611','106','??????????????????',11,'','','#','F','0','0','1','system:dict:data:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10612','106','??????????????????',12,'','','#','F','0','0','1','system:dict:data:import','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10613','106','??????????????????',13,'','','#','F','0','0','1','system:dict:data:export','',NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('107','1','????????????',7,'system/config/index','config','config','C','0','0','1','system:config:page','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10701','107','????????????',1,'','','#','F','0','0','1','system:config:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10702','107','????????????',2,'','','#','F','0','0','1','system:config:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10703','107','????????????',3,'','','#','F','0','0','1','system:config:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10704','107','????????????',4,'','','#','F','0','0','1','system:config:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10705','107','????????????',5,'','','#','F','0','0','1','system:config:import','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('10706','107','????????????',6,'','','#','F','0','0','1','system:config:export','',NULL,NULL,NULL,NULL);




INSERT INTO agile_sys_menu VALUES ('2','0','????????????',2,NULL,'monitor','monitor','M','0','0','1','','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('201','2','????????????',1,'monitor/online/index','online','online','C','0','0','1','monitor:online:list','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('20101','201','????????????',1,'','#','#','F','0','0','1','monitor:online:batchLogout','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('20102','201','????????????',2,'','#','#','F','0','0','1','monitor:online:forceLogout','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('202','2','????????????',2,'monitor/druid/index','druid','druid','C','0','0','1','monitor:druid:list','??????????????????',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('203','2','????????????',3,'monitor/server/index','server','server','C','0','0','1','monitor:server:info','??????????????????',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('3','0','????????????',3,NULL,'tool','tool','M','0','0','1','','??????????????????',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('301','3','????????????',1,'tool/form/index','form','form','C','0','0','1','tool:form:list','??????????????????',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('302','3','????????????',2,'tool/generator/index','generator','generator','C','0','0','1','tool:generator:page','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30201','302','????????????',1,'','#','#','F','0','0','1','tool:generator:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30202','302','????????????',2,'','#','#','F','0','0','1','tool:generator:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30203','302','????????????',3,'','#','#','F','0','0','1','tool:generator:import','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30204','302','????????????',4,'','#','#','F','0','0','1','tool:generator:sync','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30205','302','????????????',5,'','#','#','F','0','0','1','tool:generator:preview','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('30206','302','????????????',6,'','#','#','F','0','0','1','tool:generator:code','',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('303','3','????????????',3,'tool/swagger/index','swagger','swagger','C','0','0','1','tool:swagger:view','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('304','3','????????????',4,'tool/process/index','process','process','C','0','0','1','tool:process:view','??????????????????',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('4','0','????????????',4,'','quartz','quartz','M','0','0','1','','??????????????????',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('401','4','????????????',1,'quartz/job/index','job','job','C','0','0','1','quartz:job:page','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40101','401','????????????',1,'','','#','F','0','0','1','quartz:job:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40102','401','????????????',2,'','','#','F','0','0','1','quartz:job:add','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40103','401','????????????',3,'','','#','F','0','0','1','quartz:job:update','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40104','401','????????????',4,'','','#','F','0','0','1','quartz:job:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40105','401','????????????',5,'','','#','F','0','0','1','quartz:job:import','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40106','401','????????????',6,'','','#','F','0','0','1','quartz:job:export','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40107','401','????????????',7,'','','#','F','0','0','1','quartz:job:execute','',NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('402','4','????????????',1,'quartz/logger/index','quartzLogger','logger','C','0','0','1','job:logger:page','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40201','402','??????',1,'','','','F','0','0','1','quartz:logger:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40202','402','??????',2,'','#','#','F','0','0','1','quartz:logger:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('40203','402','??????',3,'','','','F','0','0','1','quartz:logger:clear','',NULL,NULL,NULL,NULL);



INSERT INTO agile_sys_menu VALUES ('5','0','????????????',5,'','logger','logger','M','0','0','1','','??????????????????',NULL,NULL,NULL,NULL);

INSERT INTO agile_sys_menu VALUES ('501','5','????????????',1,'logger/operate/index','operate','operate','C','0','0','1','logger:operate:list','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50101','501','??????',1,'','','','F','0','0','1','logger:operate:detail','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50102','501','??????',2,'','#','#','F','0','0','1','logger:operate:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50103','501','??????',3,'','','','F','0','0','1','logger:operate:clear','',NULL,NULL,NULL,NULL);


INSERT INTO agile_sys_menu VALUES ('502','5','????????????',2,'logger/login/index','login','login','C','0','0','1','logger:login:list','??????????????????',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50201','502','??????',1,'','#','#','F','0','0','1','logger:login:delete','',NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_menu VALUES ('50202','502','??????',2,'','','','F','0','0','1','logger:login:clear','',NULL,NULL,NULL,NULL);

/*==============================================================*/
/* Table: agile_sys_post ???????????????                              */
/*==============================================================*/
create table agile_sys_post (
   id                   varchar(32)          not null,
   post_code            varchar(20)          not null,
   post_name            varchar(100)         not null,
   post_sort            int                  not null,
   post_status          char(1)              not null,
   remark               varchar(300)         null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_sys_post primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????',
   'user', @CurrentUser, 'table', 'agile_sys_post'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_post', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_post', 'column', 'post_code'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_post', 'column', 'post_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_post', 'column', 'post_sort'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????0:?????? 1:?????????',
   'user', @CurrentUser, 'table', 'agile_sys_post', 'column', 'post_status'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????',
   'user', @CurrentUser, 'table', 'agile_sys_post', 'column', 'remark'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_post', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_post', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_post', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_post', 'column', 'update_time'
go

INSERT INTO agile_sys_post VALUES ('1','jeeagile','JeeAgile',0,'0',NULL,NULL,NULL,NULL,NULL);

/*==============================================================*/
/* Table: agile_sys_role ???????????????                              */
/*==============================================================*/
create table agile_sys_role (
   id                   varchar(32)          not null,
   role_name            varchar(30)          not null,
   role_code            varchar(100)         not null,
   role_sort            int                  not null,
   role_status          char(1)              not null,
   data_scope           char(2)              not null default '01',
   remark               varchar(300)         null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_sys_role primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role', 'column', 'role_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role', 'column', 'role_code'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role', 'column', 'role_sort'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????0:?????? 1:?????????',
   'user', @CurrentUser, 'table', 'agile_sys_role', 'column', 'role_status'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????????????????sys_data_scope???',
   'user', @CurrentUser, 'table', 'agile_sys_role', 'column', 'data_scope'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????',
   'user', @CurrentUser, 'table', 'agile_sys_role', 'column', 'remark'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_role', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_role', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role', 'column', 'update_time'
go

INSERT INTO agile_sys_role VALUES ('1','jeeagile','JeeAgile',0,'0','02',NULL,NULL,NULL,NULL,NULL);

/*==============================================================*/
/* Table: agile_sys_role_dept ?????????????????????                     */
/*==============================================================*/
create table agile_sys_role_dept (
   id                   varchar(32)          not null,
   role_id              varchar(32)          not null,
   dept_id              varchar(32)          not null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_sys_role_dept primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_dept'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_dept', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_dept', 'column', 'role_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_dept', 'column', 'dept_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_dept', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_dept', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_dept', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_dept', 'column', 'update_time'
go

/*==============================================================*/
/* Table: agile_sys_role_menu ?????????????????????                     */
/*==============================================================*/
create table agile_sys_role_menu (
   id                   varchar(32)          not null,
   role_id              varchar(32)          not null,
   menu_id              varchar(32)          not null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_sys_role_menu primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_menu'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_menu', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_menu', 'column', 'role_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_menu', 'column', 'menu_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_menu', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_menu', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_menu', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_role_menu', 'column', 'update_time'
go


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

/*==============================================================*/
/* Table: agile_sys_user ???????????????                              */
/*==============================================================*/
create table agile_sys_user (
   id                   varchar(32)          not null,
   user_name            varchar(30)          not null,
   nick_name            varchar(100)         null,
   user_sex             char(1)              null,
   user_sort            int                  not null,
   user_phone           varchar(20)          null,
   user_mobile          varchar(12)          null,
   user_address         varchar(200)         null,
   user_email           varchar(50)          null,
   user_avatar          varchar(100)         null,
   user_status          char(1)              not null default '0',
   user_pwd             varchar(100)         not null,
   dept_id              varchar(32)          null,
   remark               varchar(300)         null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_sys_user primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'user_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'nick_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'user_sex'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'user_sort'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'user_phone'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'user_mobile'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'user_address'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'user_email'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'user_avatar'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????0:?????? 2:?????? 3:?????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'user_status'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'user_pwd'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'dept_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'remark'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user', 'column', 'update_time'
go

INSERT INTO agile_sys_user VALUES ('0','admin','?????????','1',0,'18600000000','18600000000',NULL,'admin@163.com',NULL,'0','e10adc3949ba59abbe56e057f20f883e','',NULL,NULL,NULL,NULL,NULL);
INSERT INTO agile_sys_user VALUES ('1','jeeagile','JeeAgile','1',1,'18600000000','18600000000',NULL,'jeeagile@163.com','','0','e10adc3949ba59abbe56e057f20f883e','1',NULL,NULL,NULL,NULL,NULL);

/*==============================================================*/
/* Table: agile_sys_user_post ?????????????????????                     */
/*==============================================================*/
create table agile_sys_user_post (
   id                   varchar(32)          not null,
   user_id              varchar(32)          not null,
   post_id              varchar(32)          not null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_sys_user_post primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_post'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_post', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_post', 'column', 'user_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_post', 'column', 'post_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_post', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_post', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_post', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_post', 'column', 'update_time'
go

INSERT INTO agile_sys_user_post VALUES ('1','1','1',NULL,NULL,NULL,NULL);

/*==============================================================*/
/* Table: agile_sys_user_role ?????????????????????                     */
/*==============================================================*/
create table agile_sys_user_role (
   id                   varchar(32)          not null,
   user_id              varchar(32)          not null,
   role_id              varchar(32)          not null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_sys_user_role primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_role'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_role', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_role', 'column', 'user_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_role', 'column', 'role_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_role', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_role', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_role', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_user_role', 'column', 'update_time'
go
INSERT INTO agile_sys_user_role VALUES ('1','1','1',NULL,NULL,NULL,NULL);

/*==============================================================*/
/* Table: agile_sys_login ?????????????????????                      */
/*==============================================================*/
create table agile_sys_login (
   id                   varchar(32)          not null,
   login_module         varchar(150)         not null,
   login_name           varchar(150)         not null,
   login_time           datetime             not null,
   login_ip             varchar(50)          not null,
   login_address        varchar(150)         null,
   login_device         varchar(100)         not null,
   login_os             varchar(100)         not null,
   login_browser        varchar(100)         not null,
   server_address       varchar(50)          not null,
   status               varchar(1)           not null,
   message              text                 null,
   constraint PK_agile_sys_login primary key nonclustered (id)
)
go

if exists (select 1 from  sys.extended_properties
           where major_id = object_id('agile_sys_login') and minor_id = 0)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_login'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_login'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'id')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'id'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'id'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_module')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_module'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_module'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_name')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_name'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_name'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_time')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_time'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_time'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_ip')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_ip'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????IP',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_ip'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_address')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_address'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_address'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_device')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_device'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_device'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_os')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_os'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????????????????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_os'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'login_browser')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_browser'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'login_browser'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'server_address')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'server_address'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '???????????????',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'server_address'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'status')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'status'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'status'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_login')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'message')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'message'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_login', 'column', 'message'
go

/*==============================================================*/
/* Table: agile_sys_logger ?????????????????????                    */
/*==============================================================*/
create table agile_sys_logger (
   id                   varchar(32)          not null,
   operate_module       varchar(100)         not null,
   operate_notes        varchar(100)         not null,
   operate_type         varchar(10)          not null,
   operate_user         varchar(10)          not null,
   request_uri          varchar(150)         not null,
   request_method       varchar(30)          not null,
   request_param        text                 null,
   response_param       text                 null,
   execute_method       varchar(150)         null,
   operate_ip           varchar(50)          not null,
   operate_address      varchar(150)         null,
   operate_device       varchar(100)         not null,
   operate_os           varchar(100)         not null,
   operate_browser      varchar(100)         not null,
   server_address       varchar(50)          not null,
   execute_time         numeric(10,0)        not null,
   status               varchar(1)           not null,
   message              text                 null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_sys_logger primary key nonclustered (id)
)
go

if exists (select 1 from  sys.extended_properties
           where major_id = object_id('agile_sys_logger') and minor_id = 0)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'id')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'id'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'id'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_module')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_module'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_module'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_notes')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_notes'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_notes'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_type')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_type'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_type'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_user')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_user'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_user'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'request_uri')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'request_uri'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????uri',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'request_uri'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'request_method')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'request_method'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'request_method'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'request_param')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'request_param'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'request_param'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'response_param')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'response_param'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'response_param'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'execute_method')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'execute_method'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'execute_method'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_ip')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_ip'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????ip??????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_ip'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_address')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_address'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_address'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_device')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_device'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_device'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_os')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_os'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '??????????????????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_os'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'operate_browser')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_browser'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'operate_browser'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'server_address')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'server_address'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????ip??????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'server_address'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'execute_time')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'execute_time'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????(??????)',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'execute_time'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'status')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'status'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'status'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'message')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'message'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'message'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'create_user')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'create_user'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'create_user'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'create_time')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'create_time'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'create_time'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'update_user')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'update_user'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '?????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'update_user'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('agile_sys_logger')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'update_time')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_dropextendedproperty 'MS_Description',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'update_time'

end


select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '????????????',
   'user', @CurrentUser, 'table', 'agile_sys_logger', 'column', 'update_time'
go

alter table agile_gen_table_column
   add constraint fk_gen_table_column_ref_table foreign key (table_id)
      references agile_gen_table (id)
go

alter table agile_sys_role_dept
   add constraint fk_sys_user_dept_ref_dept foreign key (dept_id)
      references agile_sys_dept (id)
go

alter table agile_sys_role_dept
   add constraint fk_sys_user_dept_ref_role foreign key (role_id)
      references agile_sys_role (id)
go

alter table agile_sys_role_menu
   add constraint fk_sys_user_menu_ref_menu foreign key (menu_id)
      references agile_sys_menu (id)
go

alter table agile_sys_role_menu
   add constraint fk_sys_user_menu_ref_role foreign key (role_id)
      references agile_sys_role (id)
go

alter table agile_sys_user_post
   add constraint fk_sys_user_post_ref_post foreign key (post_id)
      references agile_sys_post (id)
go

alter table agile_sys_user_post
   add constraint fk_sys_user_post_ref_user foreign key (user_id)
      references agile_sys_user (id)
go

alter table agile_sys_user_role
   add constraint fk_sys_user_role_ref_role foreign key (role_id)
      references agile_sys_role (id)
go

alter table agile_sys_user_role
   add constraint fk_sys_user_role_ref_user foreign key (user_id)
      references agile_sys_user (id)
go

