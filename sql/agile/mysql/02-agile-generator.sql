drop table if exists agile_generator_table_column;
drop table if exists agile_generator_table;

/*==============================================================*/
/* Table: agile_generator_table 代码生成业务表                   */
/*==============================================================*/
create table agile_generator_table
(
   id                   varchar(32) not null comment '生成表主键',
   table_name           varchar(100) not null comment '表名称',
   table_comment        varchar(200) not null comment '表描述',
   table_type           varchar(200) not null default 'crud' comment '表类（crud:单表 tree:树表）',
   project_name         varchar(100) not null comment '项目名称',
   package_name         varchar(100) not null comment '生成包路径',
   class_name           varchar(100) not null comment '实体类名称',
   module_name          varchar(30) not null comment '生成模块名',
   business_name        varchar(30) not null comment '生成业务名',
   function_name        varchar(50) not null comment '生成功能名',
   function_author      varchar(50) not null comment '生成功能作者',
   parent_menu_id       varchar(32) not null comment '上级菜单',
   tree_name            varchar(100) comment '树名称字段',
   tree_code            varchar(100) comment '树编码字段',
   tree_parent_code     varchar(32) comment '树父级编码字段',
   remark               varchar(300) comment '备注',
   create_user          varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_user          varchar(32) comment '修改人',
   update_time          datetime comment '修改时间',
   primary key (id)
);

alter table agile_generator_table comment '代码生成业务表';

/*==============================================================*/
/* Table: agile_generator_table_column 代码生成业务表字段信息表   */
/*==============================================================*/
create table agile_generator_table_column
(
   id                   varchar(32) not null comment '字段主键',
   table_id             varchar(32) not null comment '归属表主键',
   column_name          varchar(100) not null comment '列名称',
   column_comment       varchar(300) not null comment '列描述',
   column_type          varchar(50) not null comment '列类型',
   column_sort          int not null default 0 comment '排序',
   java_type            varchar(50) not null comment 'JAVA类型',
   java_field           varchar(100) not null comment 'JAVA字段名',
   pk_flag              char(1) not null default '0' comment '是否主键（1:是）',
   pk_type              char(1) not null default '2' comment '主键类型（1:数据库自增 2:UUID 3:自定义）',
   required_flag        char(1) not null default '0' comment '是否必填（1:是）',
   insert_flag          char(1) not null default '1' comment '是否为插入字段（1:是）',
   edit_flag            char(1) not null default '0' comment '是否编辑字段（1:是）',
   list_flag            char(1) not null default '0' comment '是否列表字段（1:是）',
   query_flag           char(1) not null default '0' comment '是否查询字段（1:是）',
   query_type           varchar(100) not null default 'EQ' comment '查询方式（等于、不等于、大于、小于、包含、范围）',
   html_type            varchar(100) not null comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
   dict_type            varchar(100) comment '字典类型',
   create_user          varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_user          varchar(32) comment '修改人',
   update_time          datetime comment '修改时间',
   primary key (id)
);

alter table agile_generator_table_column comment '代码生成业务表字段信息表';

alter table agile_generator_table_column add constraint fk_generator_table_column_ref_table foreign key (table_id)
      references agile_generator_table (id) on delete restrict on update restrict;

