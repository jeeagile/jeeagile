package com.jeeagile.drools.util;

import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.drools.constants.AgileDroolsConstants;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @创建人 JeeAgile
 * @创建日期 2025-01-05
 * @描述 规则引擎 工具类
 */
public class AgileDroolsUtil {
    private static Logger logger = LoggerFactory.getLogger(AgileDroolsUtil.class);

    /**
     * 获取KieServices
     */
    public static KieServices kieServices() {
        return KieServices.Factory.get();
    }

    /**
     * 获取KieContainer
     */
    public static KieContainer kieContainer() {
        return kieServices().getKieClasspathContainer();
    }

    /**
     * 获取KieContainer
     *
     * @param containerId 容器ID
     */
    public static KieContainer kieContainer(String containerId) {
        return kieServices().getKieClasspathContainer(containerId);
    }

    /**
     * 获取默认容器默认KieSession
     */
    public static KieSession kieSession() {
        return kieContainer().newKieSession();
    }

    /**
     * 获取默认容器指定KieSession
     */
    public static KieSession kieSession(String kieSessionName) {
        return kieContainer().newKieSession(kieSessionName);
    }

    /**
     * 获取默认容器指定KieSession
     */
    public static KieSession kieSession(String containerId, String kieSessionName) {
        return kieContainer(containerId).newKieSession(kieSessionName);
    }


    /**
     * 加载规则文件
     */
    public static List<File> loadRuleFile(List<String> paths) {
        List<File> ruleFileList = new ArrayList<>();
        for (String path : paths) {
            ruleFileList.addAll(loadRuleFile(path));
        }
        return ruleFileList;
    }

    /**
     * 加载规则文件
     */
    public static List<File> loadRuleFile(String path) {
        List<File> ruleFileList = new ArrayList<>();
        if (path.startsWith(ResourceLoader.CLASSPATH_URL_PREFIX)) {
            ruleFileList.addAll(loadInnerFileList(path));
        } else {
            ruleFileList.addAll(loadOuterFileList(path));
        }
        return ruleFileList;
    }

    /**
     * 加载内部资源文件
     *
     * @param path 资源文件路径
     */
    public static List<File> loadInnerFileList(String path) {
        List<File> ruleFileList = new ArrayList<>();
        try {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources(path);
            for (Resource resource : resources) {
                File file = resource.getFile();
                if (isRuleFile(file.getName())) {
                    ruleFileList.add(file);
                }
            }
        } catch (IOException ex) {
            logger.error("读取规则文件出错！", ex);
        }
        return ruleFileList;
    }

    /**
     * 加载外部规则文件
     *
     * @param path 文件路径
     */
    public static List<File> loadOuterFileList(String path) {
        List<File> ruleFileList = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile() && isRuleFile(file.getName())) {
                ruleFileList.add(file);
            } else {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File nextFile : files) {
                        ruleFileList.addAll(loadOuterFileList(nextFile.getPath()));
                    }
                }
            }
        }
        return ruleFileList;
    }

    /**
     * 判断是否为规则文件
     *
     * @param fileName 文件名
     */
    private static boolean isRuleFile(String fileName) {
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (fileType.equals(AgileDroolsConstants.SUFFIX_DRL)) {
            return true;
        }
        if (fileType.equals(AgileDroolsConstants.SUFFIX_EXCEL)) {
            return true;
        }
        return fileType.equals(AgileDroolsConstants.SUFFIX_EXCEL_2007);
    }

    /**
     * 读取规则文件内容
     */
    public static String loadFileContent(File file, String charset) {
        try {
            if (file.getPath().toLowerCase().endsWith(AgileDroolsConstants.SUFFIX_EXCEL)
                    || file.getPath().toLowerCase().endsWith(AgileDroolsConstants.SUFFIX_EXCEL_2007)) {
                return new SpreadsheetCompiler().compile(Files.newInputStream(file.toPath()), InputType.XLS);
            }
            if (file.getPath().endsWith(AgileDroolsConstants.SUFFIX_CSV)) {
                return new SpreadsheetCompiler().compile(Files.newInputStream(file.toPath()), InputType.CSV);
            }
        } catch (Exception ex) {
            logger.error("读取规则文件内容出错！");
        }
        if (AgileStringUtil.isEmpty(charset)) {
            charset = "utf-8";
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            boolean flag = true;
            while ((line = bufferedReader.readLine()) != null) {
                if (flag) {
                    stringBuilder.append(line);
                    flag = false;
                } else {
                    stringBuilder.append("\r\n").append(line);
                }
            }
        } catch (Exception ex) {
            logger.error("读取规则文件内容出错！", ex);
        }
        return stringBuilder.toString();
    }
}
