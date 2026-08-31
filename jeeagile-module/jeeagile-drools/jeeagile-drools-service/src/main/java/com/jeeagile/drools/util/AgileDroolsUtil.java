package com.jeeagile.drools.util;

import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.constants.AgileYesNo;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.drools.constants.DroolsFieldType;
import com.jeeagile.drools.entity.AgileDroolsModel;
import com.jeeagile.drools.vo.AgileDroolsModelFieldInfo;
import com.jeeagile.drools.vo.AgileDroolsModelInfo;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.kie.api.KieBase;
import org.kie.api.definition.type.FactField;
import org.kie.api.definition.type.FactType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.*;

/**
 * @创建人 wangcy
 * @创建日期 2024-02-22
 * @描述
 */
public class AgileDroolsUtil {
    private static Logger logger = LoggerFactory.getLogger(AgileDroolsUtil.class);
    private static final String TEMPLATE_PATH = "template/";

    public static String getModelSourceCode(AgileDroolsModel agileDroolsModel, List<AgileDroolsModelFieldInfo> droolsModelFieldList) {
        AgileVelocityUtil.initVelocity();
        VelocityContext velocityContext = prepareVelocityContext(agileDroolsModel);
        velocityContext.put("importList", getImportList(droolsModelFieldList));
        velocityContext.put("fieldList", droolsModelFieldList);
        StringWriter stringWriter = new StringWriter();
        Template template = Velocity.getTemplate(TEMPLATE_PATH + "model." + agileDroolsModel.getModelType() + ".vm", AgileConstants.UTF8);
        template.merge(velocityContext, stringWriter);
        return stringWriter.toString();
    }

    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    public static VelocityContext prepareVelocityContext(AgileDroolsModel agileDroolsModel) {
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("ClassName", agileDroolsModel.getModelName());
        velocityContext.put("packageName", agileDroolsModel.getModelPackage());
        return velocityContext;
    }


    /**
     * 根据列类型获取导入包
     *
     * @param droolsModelFieldInfoList 列集合
     * @return 返回需要导入的包列表
     */
    public static Set<String> getImportList(List<AgileDroolsModelFieldInfo> droolsModelFieldInfoList) {
        HashSet<String> importList = new HashSet<>();
        for (AgileDroolsModelFieldInfo droolsModelFieldInfo : droolsModelFieldInfoList) {
            switch (droolsModelFieldInfo.getFieldType()) {
                case DroolsFieldType.Date:
                    importList.add("java.util.Date");
                    break;
                case DroolsFieldType.BigDecimal:
                    importList.add("java.math.BigDecimal");
                    break;
                case DroolsFieldType.BigInteger:
                    importList.add("java.math.BigInteger");
                    break;
                case DroolsFieldType.Map:
                    importList.add("java.util.Map");
                    break;
                case DroolsFieldType.Object:
                    AgileDroolsModelInfo droolsModelInfo = droolsModelFieldInfo.getDroolsModelInfo();
                    importList.add(droolsModelInfo.getModelPackage() + "." + droolsModelInfo.getModelName());
                    importList.addAll(getImportList(droolsModelInfo.getDroolsModelFieldList()));
                    break;
                default:
                    break;
            }
            if (AgileYesNo.YES.equals(droolsModelFieldInfo.getListFlag())) {
                importList.add("java.util.List");
            }
        }
        return importList;
    }

    /**
     * 处理数据对象参数值
     *
     * @param kieBase
     * @param factType
     * @param object
     * @param data
     * @param droolsModelFieldInfo
     * @throws AgileBaseException
     */
    public static void handlerParamData(KieBase kieBase, FactType factType, Object object, Map data, AgileDroolsModelFieldInfo droolsModelFieldInfo) throws AgileBaseException {
        FactField factField = factType.getField(droolsModelFieldInfo.getFieldName());
        if (factField != null && AgileStringUtil.isNotEmpty(data)) {
            if (DroolsFieldType.Object.equals(droolsModelFieldInfo.getFieldType())) {
                try {
                    AgileDroolsModelInfo droolsModelInfo = droolsModelFieldInfo.getDroolsModelInfo();
                    List<AgileDroolsModelFieldInfo> modelFieldInfoList = droolsModelInfo.getDroolsModelFieldList();
                    FactType fieldFactType = kieBase.getFactType(droolsModelInfo.getModelPackage(), droolsModelInfo.getModelName());
                    Object fieldObject = fieldFactType.newInstance();
                    if (AgileStringUtil.isNotEmpty(data.get(droolsModelFieldInfo.getFieldName()))) {
                        if (AgileYesNo.YES.equals(droolsModelFieldInfo.getListFlag())) {
                            List<Map> fieldDataList = (List) data.get(droolsModelFieldInfo.getFieldName());
                            List fieldObjectList = new ArrayList();
                            for (Map fieldData : fieldDataList) {
                                fieldObject = fieldFactType.newInstance();
                                for (AgileDroolsModelFieldInfo modelFieldInfo : modelFieldInfoList) {
                                    if (AgileYesNo.YES.equals(modelFieldInfo.getInputFlag())) {
                                        handlerParamData(kieBase, fieldFactType, fieldObject, fieldData, modelFieldInfo);
                                    }
                                }
                                fieldObjectList.add(fieldObject);
                            }
                            factType.set(object, factField.getName(), fieldObjectList);
                        } else {
                            Map fieldData = (Map) data.get(droolsModelFieldInfo.getFieldName());
                            for (AgileDroolsModelFieldInfo modelFieldInfo : modelFieldInfoList) {
                                if (AgileYesNo.YES.equals(modelFieldInfo.getInputFlag())) {
                                    handlerParamData(kieBase, fieldFactType, fieldObject, fieldData, modelFieldInfo);
                                }
                            }
                            factType.set(object, factField.getName(), fieldObject);
                        }
                    }
                } catch (Exception ex) {
                    logger.error("fieldObject参数数据处理异常！", ex);
                }
            } else {
                if (AgileYesNo.YES.equals(droolsModelFieldInfo.getInputFlag())) {
                    factType.set(object, factField.getName(), AgileDroolsUtil.handlerParamValue(factField, data));
                }
            }
        }
    }

    /**
     * 处理参数类型
     *
     * @param factField
     * @param data
     * @return
     */
    public static Object handlerParamValue(FactField factField, Map data) {
        try {
            Class<?> parameterType = factField.getType();
            Object value = data.get(factField.getName());
            if (AgileStringUtil.isNotEmpty(value)) {
                if (parameterType == BigDecimal.class) {
                    return BigDecimal.valueOf(Double.parseDouble(value.toString()));
                }
                if (parameterType == Integer.class) {
                    return Integer.parseInt(value.toString());
                }
                if (parameterType == Short.class) {
                    return Short.parseShort(value.toString());
                }
                if (parameterType == Long.class) {
                    return Long.parseLong(value.toString());
                }
                if (parameterType == Float.class) {
                    return Float.parseFloat(value.toString());
                }
                if (parameterType == Double.class) {
                    return Double.parseDouble(value.toString());
                }
            }
            return value;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
