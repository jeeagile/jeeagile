package com.jeeagile.generator.util;

import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.enums.AgileFlagEnum;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.util.ArrayUtil;
import com.jeeagile.core.util.CharUtil;
import com.jeeagile.core.util.DateUtil;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.generator.constants.AgileGeneratorConstants;
import com.jeeagile.generator.entity.AgileGeneratorTable;
import com.jeeagile.generator.entity.AgileGeneratorTableColumn;
import com.jeeagile.generator.vo.AgileGeneratorTableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author JeeAgile
 * @date 2021-06-21
 * @description
 */
@Slf4j
public class AgileGeneratorUtil {
    /**
     * 初始化表信息
     */
    public static void initGeneratorTable(AgileGeneratorTable agileGeneratorTable) {
        agileGeneratorTable.setProjectName("jeeagile-demo");
        agileGeneratorTable.setClassName(getClassName(agileGeneratorTable.getTableName()));
        agileGeneratorTable.setPackageName(getPackageName());
        agileGeneratorTable.setModuleName(getModuleName());
        agileGeneratorTable.setBusinessName(getBusinessName(agileGeneratorTable.getTableName()));
        agileGeneratorTable.setFunctionName(getFunctionName(agileGeneratorTable.getTableComment()));
        agileGeneratorTable.setParentMenuId("0");
        agileGeneratorTable.setFunctionAuthor("JeeAgile");
    }

    /**
     * 初始化列属性字段
     */
    public static void initColumnField(AgileGeneratorTable agileGeneratorTable, AgileGeneratorTableColumn agileGeneratorTableColumn) {
        agileGeneratorTableColumn.setTableId(agileGeneratorTable.getId());
        String columnName = agileGeneratorTableColumn.getColumnName();
        // 设置java字段名
        agileGeneratorTableColumn.setJavaField(StringUtil.toCamelCase(columnName));
        String dataType = getDbColumnType(agileGeneratorTableColumn.getColumnType());
        if (ArrayUtil.contains(AgileGeneratorConstants.DB_COLUMN_TYPE_STR, dataType)) {
            agileGeneratorTableColumn.setJavaType(AgileGeneratorConstants.JAVA_TYPE_STRING);
            // 字符串长度超过500设置为文本域
            Integer columnLength = getColumnLength(agileGeneratorTableColumn.getColumnType());
            String htmlType = columnLength >= 200 ? AgileGeneratorConstants.HTML_TYPE_TEXTAREA : AgileGeneratorConstants.HTML_TYPE_INPUT;
            agileGeneratorTableColumn.setHtmlType(htmlType);
        } else if (ArrayUtil.contains(AgileGeneratorConstants.DB_COLUMN_TYPE_TIME, dataType)) {
            agileGeneratorTableColumn.setJavaType(AgileGeneratorConstants.JAVA_TYPE_DATE);
            agileGeneratorTableColumn.setHtmlType(AgileGeneratorConstants.HTML_TYPE_DATETIME);
        } else if (ArrayUtil.contains(AgileGeneratorConstants.DB_COLUMN_TYPE_NUMBER, dataType)) {
            agileGeneratorTableColumn.setHtmlType(AgileGeneratorConstants.HTML_TYPE_INPUT);
            // 如果是浮点型 统一用BigDecimal
            String[] str = StringUtils.split(StringUtils.substringBetween(agileGeneratorTableColumn.getColumnType(), "(" , ")"), ",");
            if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0) {
                agileGeneratorTableColumn.setJavaType(AgileGeneratorConstants.JAVA_TYPE_BIGDECIMAL);
            }
            // 如果是整形
            else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10) {
                agileGeneratorTableColumn.setJavaType(AgileGeneratorConstants.JAVA_TYPE_INTEGER);
                agileGeneratorTableColumn.setHtmlType(AgileGeneratorConstants.HTML_TYPE_NUMBER);
            }
            // 长整形
            else {
                agileGeneratorTableColumn.setJavaType(AgileGeneratorConstants.JAVA_TYPE_LONG);
                agileGeneratorTableColumn.setHtmlType(AgileGeneratorConstants.HTML_TYPE_NUMBER);
            }
        }

        // 插入字段（默认所有字段都需要插入）
        agileGeneratorTableColumn.setInsertFlag(AgileFlagEnum.YES.getCode());

        if (!agileGeneratorTableColumn.getPkFlag().equals(AgileFlagEnum.YES.getCode())) {
            // 编辑字段
            if (!ArrayUtil.contains(AgileGeneratorConstants.COLUMN_NAME_NOT_EDIT, columnName)) {
                agileGeneratorTableColumn.setEditFlag(AgileFlagEnum.YES.getCode());
            }
            // 列表字段
            if (!ArrayUtil.contains(AgileGeneratorConstants.COLUMN_NAME_NOT_LIST, columnName)) {
                agileGeneratorTableColumn.setListFlag(AgileFlagEnum.YES.getCode());
                if (StringUtils.startsWithIgnoreCase(columnName, "parent")) {
                    agileGeneratorTableColumn.setListFlag(AgileFlagEnum.NO.getCode());
                }
            }
            // 查询字段
            if (!ArrayUtil.contains(AgileGeneratorConstants.COLUMN_NAME_NOT_QUERY, columnName)) {
                agileGeneratorTableColumn.setQueryFlag(AgileFlagEnum.YES.getCode());
                if (StringUtils.endsWithIgnoreCase(columnName, "sort")) {
                    agileGeneratorTableColumn.setQueryFlag(AgileFlagEnum.NO.getCode());
                }
            }
        }
        // 查询字段类型
        if (StringUtils.endsWithIgnoreCase(columnName, "name")) {
            agileGeneratorTableColumn.setQueryType(AgileGeneratorConstants.QUERY_TYPE_LIKE);
        }
        // 状态字段设置单选框
        if (StringUtils.endsWithIgnoreCase(columnName, "status")) {
            agileGeneratorTableColumn.setHtmlType(AgileGeneratorConstants.HTML_TYPE_RADIO);
            agileGeneratorTableColumn.setDictType(AgileGeneratorConstants.DICT_TYPE_STATUS);
        }
        // 性别字段设置下拉框
        else if (StringUtils.endsWithIgnoreCase(columnName, "sex")) {
            agileGeneratorTableColumn.setHtmlType(AgileGeneratorConstants.HTML_TYPE_SELECT);
            agileGeneratorTableColumn.setDictType(AgileGeneratorConstants.DICT_TYPE_SEX);
        }
        // 类型字段设置下拉框
        else if (StringUtils.endsWithIgnoreCase(columnName, "type")) {
            agileGeneratorTableColumn.setHtmlType(AgileGeneratorConstants.HTML_TYPE_SELECT);
        }
        // 内容字段设置富文本控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "content")) {
            agileGeneratorTableColumn.setHtmlType(AgileGeneratorConstants.HTML_TYPE_EDITOR);
        }
        //是否标识
        else if (StringUtils.endsWithIgnoreCase(columnName, "flag")) {
            agileGeneratorTableColumn.setHtmlType(AgileGeneratorConstants.HTML_TYPE_RADIO);
            agileGeneratorTableColumn.setDictType(AgileGeneratorConstants.DICT_TYPE_FLAG);
        }
        //排序
        else if (StringUtils.endsWithIgnoreCase(columnName, "sort")) {
            agileGeneratorTableColumn.setHtmlType(AgileGeneratorConstants.HTML_TYPE_NUMBER);
        }

    }

    /**
     * 获取数据库类型字段
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static String getDbColumnType(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            return StringUtils.substringBefore(columnType, "(");
        } else {
            return columnType;
        }
    }

    /**
     * 获取字段长度
     */
    public static Integer getColumnLength(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            String length = StringUtils.substringBetween(columnType, "(" , ")");
            return Integer.valueOf(length);
        } else {
            return 0;
        }
    }


    /**
     * 表名转换成Java类名
     *
     * @param tableName 表名称
     * @return 类名
     */
    public static String getClassName(String tableName) {
        String className = StringUtil.toCamelCase(tableName);
        return StringUtil.upperFirst(className.replace("agile" , ""));
    }

    @SuppressWarnings("all")
    public static String getPackageName() {
        return "com.jeeagile.demo";
    }

    public static String getModuleName() {
        String packageName = getPackageName();
        int lastIndex = packageName.lastIndexOf(CharUtil.DOT);
        int nameLength = packageName.length();
        return StringUtils.substring(packageName, lastIndex + 1, nameLength);
    }

    /**
     * 获取业务名
     *
     * @param tableName 表名
     * @return 业务名
     */
    public static String getBusinessName(String tableName) {
        int lastIndex = tableName.lastIndexOf('_');
        int nameLength = tableName.length();
        return StringUtils.substring(tableName, lastIndex + 1, nameLength);
    }

    public static String getFunctionName(String tableComment) {
        return RegExUtils.replaceAll(tableComment, "(?:表|agile)" , "");
    }


    private static final String VELOCITY_INIT_CLASS_KEY = "file.resource.loader.class";
    private static final String VELOCITY_INIT_CLASS_NAME = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    /**
     * 初始化Velocity
     */
    private static void initVelocity() {
        try {
            Properties properties = new Properties();
            // 加载classpath目录下的vm文件
            properties.setProperty(VELOCITY_INIT_CLASS_KEY, VELOCITY_INIT_CLASS_NAME);
            // 定义字符集
            properties.setProperty(RuntimeConstants.ENCODING_DEFAULT, AgileConstants.UTF8);
            properties.setProperty(RuntimeConstants.OUTPUT_ENCODING, AgileConstants.UTF8);
            // 初始化Velocity引擎，指定配置Properties
            Velocity.init(properties);
        } catch (Exception ex) {
            throw new AgileFrameException("Velocity初始化异常！" , ex);
        }
    }

    /**
     * 生成代码
     */
    public static Map<String, String> generatorCode(AgileGeneratorTableInfo agileGeneratorTableInfo) {
        Map<String, String> generatorCodeDataMap = new LinkedHashMap<>();
        AgileGeneratorUtil.initVelocity();
        VelocityContext velocityContext = prepareVelocityContext(agileGeneratorTableInfo);
        // 获取模板列表
        List<String> templateList = getTemplateList(agileGeneratorTableInfo.getTableType());
        for (String templateName : templateList) {
            StringWriter stringWriter = new StringWriter();
            Template template = Velocity.getTemplate(templateName, AgileConstants.UTF8);
            template.merge(velocityContext, stringWriter);
            generatorCodeDataMap.put(templateName, stringWriter.toString());
        }
        return generatorCodeDataMap;
    }

    public static void generatorCode(AgileGeneratorTableInfo agileGeneratorTableInfo, ZipOutputStream zipOutputStream) {
        try {
            AgileGeneratorUtil.initVelocity();
            VelocityContext velocityContext = prepareVelocityContext(agileGeneratorTableInfo);
            // 获取模板列表
            List<String> templateList = getTemplateList(agileGeneratorTableInfo.getTableType());
            for (String templateName : templateList) {
                // 渲染模板
                StringWriter stringWriter = new StringWriter();
                Template template = Velocity.getTemplate(templateName, AgileConstants.UTF8);
                template.merge(velocityContext, stringWriter);
                // 添加到zip
                zipOutputStream.putNextEntry(new ZipEntry(getFileName(templateName, agileGeneratorTableInfo)));
                IOUtils.write(stringWriter.toString(), zipOutputStream, StandardCharsets.UTF_8);
                IOUtils.close(stringWriter);
                zipOutputStream.flush();
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            log.error("渲染模板失败，表名：{}" , agileGeneratorTableInfo.getTableName(), e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, AgileGeneratorTableInfo agileGeneratorTableInfo) {
        // 文件名称
        String fileName = "";
        //父项目名称
        String projectName = agileGeneratorTableInfo.getProjectName();
        // 包路径
        String packageName = agileGeneratorTableInfo.getPackageName();
        // 模块名
        String moduleName = agileGeneratorTableInfo.getModuleName();
        // 大写类名
        String className = agileGeneratorTableInfo.getClassName();
        // 业务名称
        String businessName = agileGeneratorTableInfo.getBusinessName();
        String vuePath = projectName + "-ui/src";
        String packagePath = org.apache.commons.lang.StringUtils.replace(packageName, "." , "/");
        if (template.contains("entity.java.vm")) {
            String javaPath = projectName + "-model/src/main/java/" + packagePath;
            fileName = StringUtil.format("{}/entity/{}/{}.java" , javaPath, moduleName, className);
        } else if (template.contains("service.java.vm")) {
            String javaPath = projectName + "-api/src/main/java/" + packagePath;
            fileName = StringUtil.format("{}/service/{}/I{}Service.java" , javaPath, moduleName, className);
        } else if (template.contains("mapper.java.vm")) {
            String javaPath = projectName + "-service/src/main/java/" + packagePath;
            fileName = StringUtil.format("{}/mapper/{}/{}Mapper.java" , javaPath, moduleName, className);
        } else if (template.contains("serviceImpl.java.vm")) {
            String javaPath = projectName + "-service/src/main/java/" + packagePath;
            fileName = StringUtil.format("{}/service/{}/{}ServiceImpl.java" , javaPath, moduleName, className);
        } else if (template.contains("controller.java.vm")) {
            String javaPath = projectName + "-web/src/main/java/" + packagePath;
            fileName = StringUtil.format("{}/controller/{}/{}Controller.java" , javaPath, moduleName, className);
        } else if (template.contains("mapper.xml.vm")) {
            String mapperXmlPath = projectName + "-service/src/main/resources/mapper/" + moduleName;
            fileName = StringUtil.format("{}/{}Mapper.xml" , mapperXmlPath, className);
        } else if (template.contains("sql.vm")) {
            fileName = businessName + "Menu.sql";
        } else if (template.contains("api.js.vm")) {
            fileName = StringUtil.format("{}/api/{}/{}.js" , vuePath, moduleName, businessName);
        } else if (template.contains("index.vue.vm")) {
            fileName = StringUtil.format("{}/views/{}/{}/index.vue" , vuePath, moduleName, businessName);
        } else if (template.contains("index-tree.vue.vm")) {
            fileName = StringUtil.format("{}/views/{}/{}/index.vue" , vuePath, moduleName, businessName);
        }
        return fileName;
    }

    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static List<String> getTemplateList(String tableType) {
        List<String> templates = new ArrayList<>();
        templates.add( "template/java/mapper.java.vm");
        templates.add("template/java/entity.java.vm");
        templates.add("template/java/service.java.vm");
        templates.add("template/java/serviceImpl.java.vm");
        templates.add("template/java/controller.java.vm");
        templates.add("template/xml/mapper.xml.vm");
        templates.add("template/sql/sql.vm");
        templates.add("template/js/api.js.vm");
        if (AgileGeneratorConstants.TABLE_TYPE_CRUD.equals(tableType)) {
            templates.add("template/vue/index.vue.vm");
        } else if (AgileGeneratorConstants.TABLE_TYPE_TREE.equals(tableType)) {
            templates.add("template/vue/index-tree.vue.vm");
        }
        return templates;
    }

    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    public static VelocityContext prepareVelocityContext(AgileGeneratorTableInfo agileGeneratorTableInfo) {
        String moduleName = agileGeneratorTableInfo.getModuleName();
        String businessName = agileGeneratorTableInfo.getBusinessName();
        String packageName = agileGeneratorTableInfo.getPackageName();
        String tableType = agileGeneratorTableInfo.getTableType();
        String functionName = agileGeneratorTableInfo.getFunctionName();
        AgileGeneratorTableColumn tablePkColumn = getTablePkColumn(agileGeneratorTableInfo.getAgileGeneratorTableColumnList());
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tableType" , tableType);
        velocityContext.put("tableName" , agileGeneratorTableInfo.getTableName());
        velocityContext.put("functionName" , StringUtils.isNotEmpty(functionName) ? functionName : "【请填写功能名称】");
        velocityContext.put("ClassName" , agileGeneratorTableInfo.getClassName());
        velocityContext.put("className" , StringUtils.uncapitalize(agileGeneratorTableInfo.getClassName()));
        velocityContext.put("moduleName" , agileGeneratorTableInfo.getModuleName());
        velocityContext.put("BusinessName" , StringUtils.capitalize(agileGeneratorTableInfo.getBusinessName()));
        velocityContext.put("businessName" , agileGeneratorTableInfo.getBusinessName());
        velocityContext.put("basePackage" , getPackagePrefix(packageName));
        velocityContext.put("packageName" , packageName);
        velocityContext.put("author" , agileGeneratorTableInfo.getFunctionAuthor());
        velocityContext.put("datetime" , DateUtil.getDateNow());
        velocityContext.put("pkColumn" , tablePkColumn);
        velocityContext.put("importList" , getJavaImportList(agileGeneratorTableInfo.getAgileGeneratorTableColumnList()));
        velocityContext.put("permissionPrefix" , getPermissionPrefix(moduleName, businessName));
        velocityContext.put("columnList" , agileGeneratorTableInfo.getAgileGeneratorTableColumnList());
        velocityContext.put("table" , agileGeneratorTableInfo);
        velocityContext.put("StringUtil" , StringUtil.class);
        setMenuVelocityContext(velocityContext, agileGeneratorTableInfo);
        if (AgileGeneratorConstants.TABLE_TYPE_TREE.equals(tableType)) {
            setTreeVelocityContext(velocityContext, agileGeneratorTableInfo);
        }
        return velocityContext;
    }

    private static AgileGeneratorTableColumn getTablePkColumn(List<AgileGeneratorTableColumn> agileGeneratorTableColumnList) {
        if (agileGeneratorTableColumnList != null && !agileGeneratorTableColumnList.isEmpty()) {
            for (AgileGeneratorTableColumn agileGeneratorTableColumn : agileGeneratorTableColumnList) {
                if (AgileFlagEnum.YES.getCode().equals(agileGeneratorTableColumn.getPkFlag())) {
                    return agileGeneratorTableColumn;
                }
            }
            return agileGeneratorTableColumnList.get(0);
        }
        return null;
    }

    /**
     * 获取包前缀
     */
    public static String getPackagePrefix(String packageName) {
        int lastIndex = packageName.lastIndexOf('.');
        return StringUtils.substring(packageName, 0, lastIndex);
    }

    /**
     * 根据列类型获取需要导入的JAVA包
     */
    public static Set<String> getJavaImportList(List<AgileGeneratorTableColumn> agileGeneratorTableColumnList) {
        HashSet<String> importJavaList = new HashSet<>();
        for (AgileGeneratorTableColumn agileGeneratorTableColumn : agileGeneratorTableColumnList) {
            if (ArrayUtil.contains(AgileGeneratorConstants.BASE_COLUMN_NAME, agileGeneratorTableColumn.getColumnName())) {
                continue;
            }
            if (AgileGeneratorConstants.JAVA_TYPE_DATE.equals(agileGeneratorTableColumn.getJavaType())) {
                importJavaList.add("java.util.Date");
            } else if (AgileGeneratorConstants.JAVA_TYPE_BIGDECIMAL.equals(agileGeneratorTableColumn.getJavaType())) {
                importJavaList.add("java.math.BigDecimal");
            }
        }
        return importJavaList;
    }

    /**
     * 获取权限前缀
     *
     * @param moduleName   模块名称
     * @param businessName 业务名称
     * @return 返回权限前缀
     */
    public static String getPermissionPrefix(String moduleName, String businessName) {
        return StringUtil.format("{}:{}" , moduleName, businessName);
    }

    /**
     * 设置菜单
     */
    public static void setMenuVelocityContext(VelocityContext velocityContext, AgileGeneratorTableInfo agileGeneratorTableInfo) {
        String parentMenuId = agileGeneratorTableInfo.getParentMenuId();
        String menuId = StringUtil.getUuid();
        velocityContext.put("menuId" , menuId);
        velocityContext.put("parentMenuId" , parentMenuId);
    }

    /**
     * 设置树形结构表
     */
    public static void setTreeVelocityContext(VelocityContext velocityContext, AgileGeneratorTableInfo agileGeneratorTableInfo) {
        velocityContext.put("treeCode" , StringUtil.toCamelCase(agileGeneratorTableInfo.getTreeCode()));
        velocityContext.put("treeParentCode" , StringUtil.toCamelCase(agileGeneratorTableInfo.getTreeParentCode()));
        velocityContext.put("treeName" , StringUtil.toCamelCase(agileGeneratorTableInfo.getTreeName()));
    }


}
