/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.slim3.struts.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.validator.Form;
import org.apache.struts.Globals;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.validator.ValidatorPlugIn;
import org.slim3.commons.config.Configuration;
import org.slim3.commons.unit.CleanableTestCase;
import org.slim3.struts.annotation.Arg;
import org.slim3.struts.annotation.IntRange;
import org.slim3.struts.annotation.Mask;
import org.slim3.struts.annotation.Msg;
import org.slim3.struts.annotation.Required;
import org.slim3.struts.config.action.HogeAction;
import org.slim3.struts.form.ActionFormWrapper;
import org.slim3.struts.form.ActionFormWrapperClass;
import org.slim3.struts.unit.MockServletContext;
import org.slim3.struts.util.S3PropertyMessageResources;
import org.slim3.struts.util.S3PropertyMessageResourcesFactory;
import org.slim3.struts.util.ValidatorResourcesUtil;
import org.slim3.struts.validator.S3ValidatorResources;
import org.slim3.struts.web.WebLocator;

/**
 * @author higa
 * 
 */
public class S3ModuleConfigTest extends CleanableTestCase {

    private static final String CONFIG_PATH = "org/slim3/struts/config/slim3_configuration.properties";

    private S3ModuleConfig moduleConfig = new S3ModuleConfig("/");

    @SuppressWarnings("unused")
    @Required(msg = @Msg(key = "aaa", resource = true))
    private String aaa;

    @SuppressWarnings("unused")
    @Mask(mask = "hoge", args = @Arg(key = "aaa", resource = true, position = 1))
    private String bbb;

    @SuppressWarnings("unused")
    @Required(arg0 = @Arg(key = "aaa", resource = true, position = 0))
    private String ccc;

    @SuppressWarnings("unused")
    @Required(arg0 = @Arg(key = "", resource = true, position = 0))
    private String ddd;

    @SuppressWarnings("unused")
    @IntRange(min = 0, max = 3)
    private String eee;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Configuration.initialize(CONFIG_PATH);
        MockServletContext servletContext = new MockServletContext();
        WebLocator.setServletContext(servletContext);
        S3ValidatorResources validatorResources = new S3ValidatorResources();
        servletContext.setAttribute(ValidatorPlugIn.VALIDATOR_KEY,
                validatorResources);
        S3PropertyMessageResourcesFactory factory = new S3PropertyMessageResourcesFactory();
        S3PropertyMessageResources resources = new S3PropertyMessageResources(
                factory, "application");
        servletContext.setAttribute(Globals.MESSAGES_KEY, resources);
    }

    @Override
    protected void tearDown() throws Exception {
        WebLocator.setServletContext(null);
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void testToActionName() throws Exception {
        assertEquals("hogeAction", moduleConfig.toActionName("/hoge"));
        assertEquals("aaa.hogeAction", moduleConfig.toActionName("/aaa/hoge"));
        try {
            moduleConfig.toActionName("aaa");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testToActionClass() throws Exception {
        assertNotNull(moduleConfig.toActionClass("hogeAction"));
        assertNull(moduleConfig.toActionClass("xxxAction"));
    }

    /**
     * @throws Exception
     */
    public void testCreateActionConfigForPath() throws Exception {
        String path = "/hoge";
        ActionConfig actionConfig = moduleConfig.createActionConfig(path);
        assertEquals(path, actionConfig.getPath());
    }

    /**
     * @throws Exception
     */
    public void testCreateActionConfigForName() throws Exception {
        ActionConfig actionConfig = moduleConfig.createActionConfig("/hoge");
        assertEquals("hogeActionForm", actionConfig.getName());
    }

    /**
     * @throws Exception
     */
    public void testCreateActionConfigForActionClass() throws Exception {
        S3ActionMapping actionConfig = moduleConfig.createActionConfig("/hoge");
        assertEquals(HogeAction.class, actionConfig.getActionClass());
    }

    /**
     * @throws Exception
     */
    public void testCreateActionConfigForNoActionPath() throws Exception {
        assertNull(moduleConfig.createActionConfig("/xxx"));
    }

    /**
     * @throws Exception
     */
    public void testSetupExecuteMethodForValidate() throws Exception {
        S3ActionMapping actionConfig = moduleConfig.createActionConfig("/hoge");
        S3ExecuteConfig executeConfig = actionConfig.getExecuteConfig("index");
        assertFalse(executeConfig.isValidate());
    }

    /**
     * @throws Exception
     */
    public void testSetupExecuteMethodForInput() throws Exception {
        S3ActionMapping actionConfig = moduleConfig
                .createActionConfig("/hoge2");
        S3ExecuteConfig executeConfig = actionConfig.getExecuteConfig("submit");
        assertEquals("hoge.html", executeConfig.getInput());
    }

    /**
     * @throws Exception
     */
    public void testSetupExecuteMethodForRoles() throws Exception {
        S3ActionMapping actionConfig = moduleConfig
                .createActionConfig("/hoge2");
        S3ExecuteConfig executeConfig = actionConfig.getExecuteConfig("submit");
        String[] roles = executeConfig.getRoles();
        assertEquals(2, roles.length);
        assertEquals("aaa", roles[0]);
        assertEquals("bbb", roles[1]);
    }

    /**
     * @throws Exception
     */
    public void testSetupExecuteMethodForReset() throws Exception {
        S3ActionMapping actionConfig = moduleConfig
                .createActionConfig("/hoge2");
        S3ExecuteConfig executeConfig = actionConfig.getExecuteConfig("submit");
        Method resetMethod = executeConfig.getResetMethod();
        assertNotNull(resetMethod);
        assertEquals("resetForIndex", resetMethod.getName());
    }

    /**
     * @throws Exception
     */
    public void testSetupExecuteMethodForZeroExecuteMethod() throws Exception {
        try {
            moduleConfig.createActionConfig("/bad");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testSetupExecuteMethodForNotExistExecute() throws Exception {
        try {
            moduleConfig.createActionConfig("/bad2");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testSetupExecuteMethodForBadSignature() throws Exception {
        try {
            moduleConfig.createActionConfig("/bad3");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testSetupExecuteMethodForNoInput() throws Exception {
        try {
            moduleConfig.createActionConfig("/bad4");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testSetupExecuteMethodForNotExistResetMethod() throws Exception {
        try {
            moduleConfig.createActionConfig("/bad5");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testCreateFormBeanConfig() throws Exception {
        String name = "hoge2ActionForm";
        moduleConfig.findActionConfig("/hoge2");
        S3FormBeanConfig formBeanConfig = (S3FormBeanConfig) moduleConfig
                .findFormBeanConfig(name);
        assertNotNull(formBeanConfig);
        assertEquals(name, formBeanConfig.getName());
        assertEquals(ActionFormWrapper.class.getName(), formBeanConfig
                .getType());
        ActionFormWrapperClass dynaClass = (ActionFormWrapperClass) formBeanConfig
                .getDynaClass();
        assertNotNull(dynaClass);
        assertEquals(name, dynaClass.getName());
        assertNotNull(dynaClass.getDynaProperty("aaa"));
    }

    /**
     * @throws Exception
     */
    @Hoge(aaa = "111", bbb = false)
    public void testGetProperties() throws Exception {
        Method m = getClass().getDeclaredMethod("testGetProperties");
        Hoge hoge = m.getAnnotation(Hoge.class);
        Map<String, Object> properties = moduleConfig.getProperties(hoge);
        assertEquals(2, properties.size());
        assertEquals("111", properties.get("aaa"));
        assertEquals(false, properties.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testIsValidationTarget() throws Exception {
        assertTrue(moduleConfig.isValidationTarget("aaa", new String[0]));
        assertTrue(moduleConfig.isValidationTarget("aaa", new String[] { "aaa",
                "bbb" }));
        assertFalse(moduleConfig.isValidationTarget("ccc", new String[] {
                "aaa", "bbb" }));
    }

    /**
     * @throws Exception
     */
    public void testResolveKey() throws Exception {
        assertEquals("aaa", moduleConfig.resolveKey("aaa", true, null));
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("aaa", "111");
        assertEquals("111", moduleConfig.resolveKey("${var:aaa}", false,
                properties));
        assertEquals("aaa", moduleConfig.resolveKey("aaa", false, properties));
    }

    /**
     * @throws Exception
     */
    public void testCreateValidatorField() throws Exception {
        org.apache.commons.validator.Field field = moduleConfig
                .createValidatorField("aaa", "required",
                        new HashMap<String, Object>());
        assertEquals("aaa", field.getProperty());
        assertEquals("required", field.getDepends());
    }

    /**
     * @throws Exception
     */
    public void testCreateValidatorFieldForMsg() throws Exception {
        Field f = getClass().getDeclaredField("aaa");
        Required anno = f.getAnnotation(Required.class);
        Map<String, Object> properties = moduleConfig.getProperties(anno);
        org.apache.commons.validator.Field field = moduleConfig
                .createValidatorField("aaa", "required", properties);
        org.apache.commons.validator.Msg msg = field.getMessage("required");
        assertNotNull(msg);
        assertEquals("required", msg.getName());
        assertEquals("aaa", msg.getKey());
        assertTrue(msg.isResource());
    }

    /**
     * @throws Exception
     */
    public void testCreateValidatorFieldForArgs() throws Exception {
        Field f = getClass().getDeclaredField("bbb");
        Mask anno = f.getAnnotation(Mask.class);
        Map<String, Object> properties = moduleConfig.getProperties(anno);
        org.apache.commons.validator.Field field = moduleConfig
                .createValidatorField("bbb", "mask", properties);
        org.apache.commons.validator.Arg a = field.getArg(1);
        assertEquals("aaa", a.getKey());
        assertEquals(1, a.getPosition());
        assertTrue(a.isResource());
    }

    /**
     * @throws Exception
     */
    public void testCreateValidatorFieldForArg() throws Exception {
        Field f = getClass().getDeclaredField("ccc");
        Required anno = f.getAnnotation(Required.class);
        Map<String, Object> properties = moduleConfig.getProperties(anno);
        org.apache.commons.validator.Field field = moduleConfig
                .createValidatorField("ccc", "required", properties);
        org.apache.commons.validator.Arg a = field.getArg(0);
        assertEquals("aaa", a.getKey());
        assertEquals(0, a.getPosition());
        assertTrue(a.isResource());
    }

    /**
     * @throws Exception
     */
    public void testCreateValidatorFieldForArgLabel() throws Exception {
        Field f = getClass().getDeclaredField("ddd");
        Required anno = f.getAnnotation(Required.class);
        Map<String, Object> properties = moduleConfig.getProperties(anno);
        org.apache.commons.validator.Field field = moduleConfig
                .createValidatorField("ddd", "required", properties);
        org.apache.commons.validator.Arg a = field.getArg(0);
        assertEquals("labels.ddd", a.getKey());
        assertEquals(0, a.getPosition());
        assertTrue(a.isResource());
    }

    /**
     * @throws Exception
     */
    public void testCreateValidatorFieldForArgNoLabel() throws Exception {
        Field f = getClass().getDeclaredField("ddd");
        Required anno = f.getAnnotation(Required.class);
        Map<String, Object> properties = moduleConfig.getProperties(anno);
        org.apache.commons.validator.Field field = moduleConfig
                .createValidatorField("aaa", "required", properties);
        org.apache.commons.validator.Arg a = field.getArg(0);
        assertEquals("aaa", a.getKey());
        assertEquals(0, a.getPosition());
        assertFalse(a.isResource());
    }

    /**
     * @throws Exception
     */
    public void testCreateValidatorFieldForVar() throws Exception {
        Field f = getClass().getDeclaredField("eee");
        IntRange anno = f.getAnnotation(IntRange.class);
        Map<String, Object> properties = moduleConfig.getProperties(anno);
        org.apache.commons.validator.Field field = moduleConfig
                .createValidatorField("eee", "required", properties);
        org.apache.commons.validator.Var v = field.getVar("min");
        assertNotNull(v);
        assertEquals("0", v.getValue());
        org.apache.commons.validator.Var v2 = field.getVar("max");
        assertNotNull(v2);
        assertEquals("3", v2.getValue());
    }

    /**
     * @throws Exception
     */
    public void testRegisterValidatorField() throws Exception {
        Field f = getClass().getDeclaredField("aaa");
        Required anno = f.getAnnotation(Required.class);
        Map<String, Object> properties = moduleConfig.getProperties(anno);
        Map<String, Form> forms = new HashMap<String, Form>();
        Form form = new Form();
        forms.put("index", form);
        moduleConfig.registerValidatorField("aaa", "required", properties,
                forms);
        org.apache.commons.validator.Field field = form.getField("aaa");
        assertNotNull(field);
    }

    /**
     * @throws Exception
     */
    public void testProcessValidatorAnnotation() throws Exception {
        Field f = getClass().getDeclaredField("aaa");
        Required anno = f.getAnnotation(Required.class);
        Map<String, Form> forms = new HashMap<String, Form>();
        Form form = new Form();
        forms.put("index", form);
        moduleConfig.processValidatorAnnotation("aaa", anno, forms);
        org.apache.commons.validator.Field field = form.getField("aaa");
        assertNotNull(field);
    }

    /**
     * @throws Exception
     */
    public void testSetupValidator() throws Exception {
        S3ActionMapping actionMapping = moduleConfig
                .createActionConfig("/hoge2");
        moduleConfig.setupValidator(actionMapping);
        assertNotNull(ValidatorResourcesUtil.getValidatorResources().getForm(
                null, "hoge2ActionForm_submit2"));
    }

    /**
     * @throws Exception
     */
    public void testFindActionConfig() throws Exception {
        assertNotNull(moduleConfig.findActionConfig("/hoge2"));
        assertNotNull(moduleConfig.findFormBeanConfig("hoge2ActionForm"));
        assertNotNull(ValidatorResourcesUtil.getValidatorResources().getForm(
                null, "hoge2ActionForm_submit2"));
    }

    /**
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Documented
    public static @interface Hoge {

        /**
         *
         */
        String aaa();

        /**
         *
         */
        boolean bbb();
    }
}