alter table agile_gen_table_column drop constraint fk_gen_table_column_ref_table;
drop table agile_gen_table cascade constraints;
drop table agile_gen_table_column cascade constraints;

/*==============================================================*/
/* Table: agile_gen_table 代码生成业务表                         */
/*==============================================================*/
create table agile_gen_table 
(
   id                 VARCHAR2(32)         not null,
   table_name         VARCHAR2(100)        not null,
   table_comment      VARCHAR2(200)        not null,
   table_type         VARCHAR2(200)        default 'crud' not null,
   project_name       VARCHAR2(100)        not null,
   template_type      VARCHAR2(100)        not null,
   package_name       VARCHAR2(100)        not null,
   class_name         VARCHAR2(100)        not null,
   module_name        VARCHAR2(30)         not null,
   business_name      VARCHAR2(30)         not null,
   function_name      VARCHAR2(50)         not null,
   function_author    VARCHAR2(50)         not null,
   parent_menu_id     VARCHAR2(32)         not null,
   tree_name          VARCHAR2(100),
   tree_code          VARCHAR2(100),
   tree_parent_code   VARCHAR2(32),
   remark             VARCHAR2(300),
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_gen_table primary key (id)
);

comment on table agile_gen_table is '代码生成业务表';

comment on column agile_gen_table.id is '生成表主键ID';
comment on column agile_gen_table.table_name is '表名称';
comment on column agile_gen_table.table_comment is '表描述';
comment on column agile_gen_table.table_type is '表类（crud单表 tree树表）';
comment on column agile_gen_table.project_name is '项目名称';
comment on column agile_gen_table.template_type is '模版类型';
comment on column agile_gen_table.package_name is '生成包路径';
comment on column agile_gen_table.class_name is '实体类名称';
comment on column agile_gen_table.module_name is '生成模块名';
comment on column agile_gen_table.business_name is '生成业务名';
comment on column agile_gen_table.function_name is '生成功能名';
comment on column agile_gen_table.function_author is '生成功能作者';
comment on column agile_gen_table.parent_menu_id is '上级菜单ID';
comment on column agile_gen_table.tree_name is '树名称字段';
comment on column agile_gen_table.tree_code is '树编码字段';
comment on column agile_gen_table.tree_parent_code is '树父级编码字段';
comment on column agile_gen_table.remark is '备注';
comment on column agile_gen_table.create_user is '创建人';
comment on column agile_gen_table.create_time is '创建时间';
comment on column agile_gen_table.update_user is '修改人';
comment on column agile_gen_table.update_time is '修改时间';

/*==============================================================*/
/* Table: agile_gen_table_column 代码生成业务表字段信息表         */
/*==============================================================*/
create table agile_gen_table_column 
(
   id                 VARCHAR2(32)         not null,
   table_id           VARCHAR2(32)         not null,
   column_name        VARCHAR2(100)        not null,
   column_comment     VARCHAR2(300)        not null,
   column_type        VARCHAR2(50)         not null,
   column_sort        INTEGER              default 0 not null,
   java_type          VARCHAR2(50)         not null,
   java_field         VARCHAR2(100)        not null,
   pk_flag            CHAR(1)              default '0' not null,
   pk_type            CHAR(1)              default '2' not null,
   required_flag      CHAR(1)              default '0' not null,
   insert_flag        CHAR(1)              default '1' not null,
   edit_flag          CHAR(1)              default '0' not null,
   list_flag          CHAR(1)              default '0' not null,
   query_flag         CHAR(1)              default '0' not null,
   query_type         VARCHAR2(100)        default 'EQ' not null,
   html_type          VARCHAR2(100)        not null,
   dict_type          VARCHAR2(100),
   create_user        VARCHAR2(32),
   create_time        DATE,
   update_user        VARCHAR2(32),
   update_time        DATE,
   constraint PK_agile_gen_table_column primary key (id)
);

comment on table agile_gen_table_column is '代码生成业务表字段信息表';

comment on column agile_gen_table_column.id is '主键ID';
comment on column agile_gen_table_column.table_id is '归属表主键';
comment on column agile_gen_table_column.column_name is '列名称';
comment on column agile_gen_table_column.column_comment is '列描述';
comment on column agile_gen_table_column.column_type is '列类型';
comment on column agile_gen_table_column.column_sort is '排序';
comment on column agile_gen_table_column.java_type is 'JAVA类型';
comment on column agile_gen_table_column.java_field is 'JAVA字段名';
comment on column agile_gen_table_column.pk_flag is '是否主键（1是）';
comment on column agile_gen_table_column.pk_type is '主键类型（1：数据库自增  2：UUID 3：自定义）';
comment on column agile_gen_table_column.required_flag is '是否必填（1是）';
comment on column agile_gen_table_column.insert_flag is '是否为插入字段（1是）';
comment on column agile_gen_table_column.edit_flag is '是否编辑字段（1是）';
comment on column agile_gen_table_column.list_flag is '是否列表字段（1是）';
comment on column agile_gen_table_column.query_flag is '是否查询字段（1是）';
comment on column agile_gen_table_column.query_type is '查询方式（等于、不等于、大于、小于、范围）';
comment on column agile_gen_table_column.html_type is '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）';
comment on column agile_gen_table_column.dict_type is '字典类型';
comment on column agile_gen_table_column.create_user is '创建人';
comment on column agile_gen_table_column.create_time is '创建时间';
comment on column agile_gen_table_column.update_user is '修改人';
comment on column agile_gen_table_column.update_time is '修改时间';



alter table agile_gen_table_column
   add constraint fk_gen_table_column_ref_table foreign key (table_id)
      references agile_gen_table (id);