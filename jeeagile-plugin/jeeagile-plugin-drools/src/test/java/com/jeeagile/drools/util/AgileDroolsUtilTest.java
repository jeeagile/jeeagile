package com.jeeagile.drools.util;

import com.alibaba.fastjson.JSONObject;
import com.jeeagile.drools.kie.AgileKieBase;
import com.jeeagile.drools.kie.AgileKieRule;
import com.jeeagile.drools.kie.AgileKieTemplate;
import com.jeeagile.drools.properties.AgileKieBaseProperties;
import com.jeeagile.drools.test.One;
import com.jeeagile.drools.test.Two;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AgileDroolsUtilTest  {

    @Test
    public void test() {
        AgileDroolsUtil.kieSession().fireAllRules();
    }

    @Test
    public void testOne() throws Exception {
        Map<String, AgileKieBaseProperties> map = new LinkedHashMap<>();
        AgileKieBase agileKieBase = new AgileKieBase();
        agileKieBase.setName("one");
        List<String> packagesOne = new ArrayList<>();
        packagesOne.add("com.jeeagile.drools.test");
        packagesOne.add("com.jeeagile.drools.test1");
        packagesOne.add("com.jeeagile.drools.test2");
        agileKieBase.setPackages(packagesOne);
        agileKieBase.setDefault(true);
        List<AgileKieRule> kieRuleList = new ArrayList<>();
        List<File> ruleFileList = AgileDroolsUtil.loadRuleFile("F:\\jeeagile\\jeeagile-plugin\\jeeagile-plugin-drools\\src\\test\\resources\\rules\\one");
        for (File file : ruleFileList) {
            AgileKieRule agileKieRule = new AgileKieRule();
            agileKieRule.setContent(AgileDroolsUtil.loadFileContent(file, "utf-8"));
            agileKieRule.setName(file.getName());
            agileKieRule.setPath(file.getPath());
            kieRuleList.add(agileKieRule);
        }
        agileKieBase.setKieRuleList(kieRuleList);

        List<AgileKieBase> agileKieBaseList = new ArrayList<>();
        agileKieBaseList.add(agileKieBase);

        AgileKieTemplate agileKieTemplate = new AgileKieTemplate();
        agileKieTemplate.setAgileKieBaseList(agileKieBaseList);
        agileKieTemplate.afterPropertiesSet();


        One one = new One();
        one.setName("测试");
        Two two = new Two();
        two.setName("测试2");

        agileKieTemplate.fireAllRules("one", one, two);
        FactType factType = agileKieTemplate.getKieBase("one").getFactType("com.jeeagile.drools.test", "Person");
        Object o = factType.newInstance();
        factType.set(o, "name", "00");
        agileKieTemplate.fireAllRules("one", o);


        AgileKieRule agileKieRule = new AgileKieRule();
        agileKieRule.setName("test.drl");
        agileKieRule.setPath("F:\\jeeagile\\jeeagile-plugin\\jeeagile-plugin-drools\\src\\test\\resources\\rules\\one\\test.drl");
        agileKieRule.setContent("package com.jeeagile.drools.test1\n" +
                "rule \"测试1\"\n" +
                "    when\n" +
                "       \n" +
                "    then\n" +
                "        System.out.println(\"测试12222222!!!!\");\n" +
                "end\n");
        agileKieTemplate.updateKieRule(agileKieRule);
        agileKieTemplate.fireAllRules("one", one, two);

        agileKieTemplate.verify("package com.jeeagile.drools.test1\n" +
                "rule \"测试12\"\n" +
                "    when\n" +
                "       \n" +
                "    then\n" +
                "        System.out.println(\"测试122!!!!\");\n" +
                "end\n");
        agileKieTemplate.fireAllRules("one", one, two);
    }


    @Test
    public void testTwo() throws IllegalAccessException, InstantiationException {
        KieHelper kieHelper = new KieHelper();

        kieHelper.addContent("package com.jeeagile.drools.test;\n" +
                "\n" +
                "import lombok.Data;\n" +
                "@Data\n" +
                "public class OneTest {\n" +
                "    private String name;\n" +
                "}", ResourceType.JAVA);


        kieHelper.addContent("package com.jeeagile.drools;\n" +
                "declare Person\n" +
                "    name : String\n" +
                "end", "Person.drl");


        kieHelper.addContent("package com.jeeagile.drools.test1;\n" +
                "import com.jeeagile.drools.test.One;" +
                "rule \"测试1\"\n" +
                "    when\n" +
                "       $one:One(getName()==\"测试\") \n" +
                "    then\n" +
                "        System.out.println(\"测试122!!!!\");\n" +
                "end\n", "ond.drl");


        KieBase kieBase = kieHelper.build();
        FactType factType = kieBase.getFactType("com.jeeagile.drools", "Person");
        try {
            Object o = JSONObject.parseObject("{name:\"测试\"}", factType.getFactClass());
//            Object o = JSONObject.parseObject("{name:\"测试\"}",  Class.forName("OneTest"));

            System.out.println(o);
            KieSession kieSession = kieBase.newKieSession();
            kieSession.insert(o);
            kieSession.fireAllRules();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}