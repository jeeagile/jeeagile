if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('agile_generator_table_column') and o.name = 'fk_generator_table_column_ref_table')
alter table agile_generator_table_column
   drop constraint fk_generator_table_column_ref_table
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_generator_table')
            and   type = 'U')
   drop table agile_generator_table
go

if exists (select 1
            from  sysobjects
           where  id = object_id('agile_generator_table_column')
            and   type = 'U')
   drop table agile_generator_table_column
go


/*==============================================================*/
/* Table: agile_generator_table 代码生成业务表                   */
/*==============================================================*/
create table agile_generator_table (
   id                   varchar(32)          not null,
   table_name           varchar(100)         not null,
   table_comment        varchar(200)         not null,
   table_type           varchar(200)         not null default 'crud',
   project_name         varchar(100)         not null,
   package_name         varchar(100)         not null,
   class_name           varchar(100)         not null,
   module_name          varchar(30)          not null,
   business_name        varchar(30)          not null,
   function_name        varchar(50)          not null,
   function_author      varchar(50)          not null,
   parent_menu_id       varchar(32)          not null,
   tree_name            varchar(100)         null,
   tree_code            varchar(100)         null,
   tree_parent_code     varchar(32)          null,
   remark               varchar(300)         null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_generator_table primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '代码生成业务表',
   'user', @CurrentUser, 'table', 'agile_generator_table'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '生成表主键',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '表名称',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'table_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '表描述',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'table_comment'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '表类（crud:单表 tree:树表）',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'table_type'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '项目名称',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'project_name'
go


declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '生成包路径',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'package_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '实体类名称',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'class_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '生成模块名',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'module_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '生成业务名',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'business_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '生成功能名',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'function_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '生成功能作者',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'function_author'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '上级菜单',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'parent_menu_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '树名称字段',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'tree_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '树编码字段',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'tree_code'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '树父级编码字段',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'tree_parent_code'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '备注',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'remark'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '创建人',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '创建时间',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '修改人',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '修改时间',
   'user', @CurrentUser, 'table', 'agile_generator_table', 'column', 'update_time'
go

/*==============================================================*/
/* Table: agile_generator_table_column 代码生成业务表字段信息表   */
/*==============================================================*/
create table agile_generator_table_column (
   id                   varchar(32)          not null,
   table_id             varchar(32)          not null,
   column_name          varchar(100)         not null,
   column_comment       varchar(300)         not null,
   column_type          varchar(50)          not null,
   column_sort          int                  not null default 0,
   java_type            varchar(50)          not null,
   java_field           varchar(100)         not null,
   pk_flag              char(1)              not null default '0',
   pk_type              char(1)              not null default '2',
   required_flag        char(1)              not null default '0',
   insert_flag          char(1)              not null default '1',
   edit_flag            char(1)              not null default '0',
   list_flag            char(1)              not null default '0',
   query_flag           char(1)              not null default '0',
   query_type           varchar(100)         not null default 'EQ',
   html_type            varchar(100)         not null,
   dict_type            varchar(100)         null,
   create_user          varchar(32)          null,
   create_time          datetime             null,
   update_user          varchar(32)          null,
   update_time          datetime             null,
   constraint PK_agile_generator_table_column primary key nonclustered (id)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '代码生成业务表字段信息表',
   'user', @CurrentUser, 'table', 'agile_generator_table_column'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '字段主键',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '归属表主键',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'table_id'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '列名称',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'column_name'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '列描述',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'column_comment'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '列类型',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'column_type'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '排序',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'column_sort'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   'JAVA类型',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'java_type'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   'JAVA字段名',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'java_field'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '是否主键（1:是）',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'pk_flag'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '主键类型（1:数据库自增 2:UUID 3:自定义）',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'pk_type'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '是否必填（1:是）',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'required_flag'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '是否为插入字段（1:是）',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'insert_flag'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '是否编辑字段（1:是）',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'edit_flag'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '是否列表字段（1:是）',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'list_flag'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '是否查询字段（1:是）',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'query_flag'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '查询方式（等于、不等于、大于、小于、包含、范围）',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'query_type'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'html_type'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '字典类型',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'dict_type'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '创建人',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'create_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '创建时间',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'create_time'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '修改人',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'update_user'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description',
   '修改时间',
   'user', @CurrentUser, 'table', 'agile_generator_table_column', 'column', 'update_time'
go