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
package org.slim3.struts.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.slim3.commons.bean.BeanUtil;
import org.slim3.commons.config.Configuration;
import org.slim3.commons.unit.CleanableTestCase;
import org.slim3.struts.action.S3RequestProcessor.IndexParsedResult;
import org.slim3.struts.action.controller.HogeController;
import org.slim3.struts.config.S3ActionMapping;
import org.slim3.struts.config.S3ExecuteConfig;
import org.slim3.struts.config.S3FormBeanConfig;
import org.slim3.struts.config.S3ModuleConfig;
import org.slim3.struts.form.ActionFormWrapper;
import org.slim3.struts.form.ActionFormWrapperClass;
import org.slim3.struts.unit.MockHttpServletRequest;
import org.slim3.struts.unit.MockHttpServletResponse;
import org.slim3.struts.unit.MockServletContext;
import org.slim3.struts.util.ActionUtil;
import org.slim3.struts.util.S3ExecuteConfigUtil;
import org.slim3.struts.web.WebLocator;

/**
 * @author higa
 * 
 */
public class S3RequestProcessorTest extends CleanableTestCase {

    private static final String CONFIG_PATH = "org/slim3/struts/action/slim3_configuration.properties";

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request = new MockHttpServletRequest(
            servletContext, "/hoge.do");

    private MockHttpServletResponse response = new MockHttpServletResponse();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Configuration.initialize(CONFIG_PATH);
        WebLocator.setRequest(request);
    }

    @Override
    protected void tearDown() throws Exception {
        WebLocator.setRequest(null);
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void testProcessMultipart() throws Exception {
        request.setMethod("POST");
        request.setContentType("multipart/form-data");
        S3RequestProcessor processor = new S3RequestProcessor();
        HttpServletRequest req = processor.processMultipart(request);
        assertEquals(MultipartRequestWrapper.class, req.getClass());
        assertSame(req, WebLocator.getRequest());
    }

    /**
     * @throws Exception
     */
    public void testProcessMapping() throws Exception {
        request.getSession().setAttribute("aaa", "111");
        S3ActionMapping mapping = new S3ActionMapping();
        mapping.setPath("/hoge");
        mapping.setControllerName("hogeAction");
        mapping.setControllerClass(HogeController.class);
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(HogeController.class
                .getMethod("index"));
        mapping.addExecuteConfig(executeConfig);
        S3RequestProcessor processor = new S3RequestProcessor();
        S3ModuleConfig moduleConfig = new S3ModuleConfig("");
        moduleConfig.addActionConfig(mapping);
        processor.init(new ActionServlet(), moduleConfig);
        ActionMapping am = processor.processMapping(request, response, "/hoge");
        assertNotNull(am);
        assertNotNull(request.getAttribute(Globals.MAPPING_KEY));
        HogeController action = (HogeController) ActionUtil.getAction();
        assertNotNull(action);
        assertEquals("111", action.aaa);
        assertNull(action.bbb);
    }

    /**
     * @throws Exception
     */
    public void testProcessExecuteConfigForGET() throws Exception {
        S3ActionMapping mapping = new S3ActionMapping();
        mapping.setPath("/hoge");
        mapping.setControllerClass(HogeController.class);
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(HogeController.class
                .getMethod("index"));
        mapping.addExecuteConfig(executeConfig);
        S3RequestProcessor processor = new S3RequestProcessor();
        S3ModuleConfig moduleConfig = new S3ModuleConfig("");
        moduleConfig.addActionConfig(mapping);
        processor.init(new ActionServlet(), moduleConfig);
        processor.processExecuteConfig(request, response, mapping);
        assertEquals("index", S3ExecuteConfigUtil.getExecuteConfig()
                .getMethod().getName());
    }

    /**
     * @throws Exception
     */
    public void testProcessExecuteConfigForPOST() throws Exception {
        request.setParameter("submit", "aaa");
        S3ActionMapping mapping = new S3ActionMapping();
        mapping.setPath("/hoge");
        mapping.setControllerClass(HogeController.class);
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(HogeController.class
                .getMethod("submit"));
        mapping.addExecuteConfig(executeConfig);
        S3RequestProcessor processor = new S3RequestProcessor();
        S3ModuleConfig moduleConfig = new S3ModuleConfig("");
        moduleConfig.addActionConfig(mapping);
        processor.init(new ActionServlet(), moduleConfig);
        processor.processExecuteConfig(request, response, mapping);
        assertEquals("submit", S3ExecuteConfigUtil.getExecuteConfig()
                .getMethod().getName());
    }

    /**
     * @throws Exception
     */
    public void testProcessRolesForNoRole() throws Exception {
        S3ActionMapping mapping = new S3ActionMapping();
        mapping.setPath("/hoge");
        mapping.setControllerClass(HogeController.class);
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(HogeController.class
                .getMethod("index"));
        mapping.addExecuteConfig(executeConfig);
        S3ExecuteConfigUtil.setExecuteConfig(executeConfig);
        S3RequestProcessor processor = new S3RequestProcessor();
        S3ModuleConfig moduleConfig = new S3ModuleConfig("");
        moduleConfig.addActionConfig(mapping);
        processor.init(new ActionServlet(), moduleConfig);
        processor.processExecuteConfig(request, response, mapping);
        assertTrue(processor.processRoles(request, response, mapping));
    }

    /**
     * @throws Exception
     */
    public void testProcessRolesForRoleOK() throws Exception {
        request = new MockHttpServletRequest(servletContext, "/hoge.do") {

            @Override
            public boolean isUserInRole(String role) {
                return "role1".equals(role);
            }

        };
        request.setAttribute("submit", "aaa");
        S3ActionMapping mapping = new S3ActionMapping();
        mapping.setPath("/hoge");
        mapping.setControllerClass(HogeController.class);
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(HogeController.class
                .getMethod("submit"));
        mapping.addExecuteConfig(executeConfig);
        S3ExecuteConfigUtil.setExecuteConfig(executeConfig);
        S3RequestProcessor processor = new S3RequestProcessor();
        S3ModuleConfig moduleConfig = new S3ModuleConfig("");
        moduleConfig.addActionConfig(mapping);
        processor.init(new ActionServlet(), moduleConfig);
        assertTrue(processor.processRoles(request, response, mapping));
    }

    /**
     * @throws Exception
     */
    public void testProcessRolesForRoleNG() throws Exception {
        request = new MockHttpServletRequest(servletContext, "/hoge.do") {

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }

        };
        request.setAttribute("submit", "aaa");
        S3ActionMapping mapping = new S3ActionMapping();
        mapping.setPath("/hoge");
        mapping.setControllerClass(HogeController.class);
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(HogeController.class
                .getMethod("submit"));
        executeConfig.setRoles(new String[] { "role1", "role2" });
        mapping.addExecuteConfig(executeConfig);
        S3ExecuteConfigUtil.setExecuteConfig(executeConfig);
        S3RequestProcessor processor = new S3RequestProcessor();
        S3ModuleConfig moduleConfig = new S3ModuleConfig("");
        moduleConfig.addActionConfig(mapping);
        processor.init(new ActionServlet(), moduleConfig);
        assertFalse(processor.processRoles(request, response, mapping));
    }

    /**
     * @throws Exception
     */
    public void testProcessActionForm() throws Exception {
        S3ActionMapping mapping = new S3ActionMapping();
        mapping.setName("HogeActionForm");
        S3RequestProcessor processor = new S3RequestProcessor();
        S3ModuleConfig moduleConfig = new S3ModuleConfig("");
        ActionFormWrapperClass wrapperClass = new ActionFormWrapperClass(
                "HogeActionForm");
        S3FormBeanConfig formConfig = new S3FormBeanConfig();
        formConfig.setName("HogeActionForm");
        formConfig.setDynaClass(wrapperClass);
        moduleConfig.addFormBeanConfig(formConfig);
        processor.init(new ActionServlet(), moduleConfig);
        ActionUtil.setAction(new HogeController());
        ActionForm actionForm = processor.processActionForm(request, response,
                mapping);
        assertNotNull(actionForm);
        assertEquals(ActionFormWrapper.class, actionForm.getClass());
        assertNotNull(request.getAttribute("HogeActionForm"));
    }

    /**
     * @throws Exception
     */
    public void testProcessActionCreate() throws Exception {
        S3ActionMapping mapping = new S3ActionMapping();
        S3RequestProcessor processor = new S3RequestProcessor();
        S3ModuleConfig moduleConfig = new S3ModuleConfig("");
        processor.init(new ActionServlet(), moduleConfig);
        ActionUtil.setAction(new HogeController());
        Action action = processor.processActionCreate(request, response,
                mapping);
        assertNotNull(action);
        assertEquals(ActionWrapper.class, action.getClass());
        assertNotNull(action.getServlet());
    }

    /**
     * @throws Exception
     */
    public void testGetMultipartHandler() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        assertNotNull(processor
                .getMultipartHandler("org.apache.struts.upload.CommonsMultipartRequestHandler"));
    }

    /**
     * @throws Exception
     */
    public void testGetMultipartHandlerForModuleConfig() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        S3ModuleConfig moduleConfig = new S3ModuleConfig("");
        processor.init(null, moduleConfig);
        assertNotNull(processor.getMultipartHandler(null));
    }

    /**
     * @throws Exception
     */
    public void testGetAllParameters() throws Exception {
        MultipartRequestHandler multipartHandler = new CommonsMultipartRequestHandler() {

            @SuppressWarnings("unchecked")
            @Override
            public Hashtable getAllElements() {
                Hashtable elements = new Hashtable();
                elements.put("aaa", "111");
                return elements;
            }

        };
        request.addParameter("bbb", "222");
        HttpServletRequest request2 = new MultipartRequestWrapper(request);
        S3RequestProcessor processor = new S3RequestProcessor();
        Map<String, Object> params = processor.getAllParameters(request2,
                multipartHandler);
        assertEquals("111", params.get("aaa"));
        assertEquals("222", ((String[]) params.get("bbb"))[0]);
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForSimple() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        processor.setProperty(bean, "aaa", new String[] { "111" });
        assertEquals("111", bean.aaa);
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForNestedBean() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        processor.setProperty(bean, "myBean.aaa", new String[] { "111" });
        assertEquals("111", bean.myBean.aaa);
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForNestedMap() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        processor.setProperty(bean, "map.aaa", new String[] { "111" });
        assertEquals("111", bean.map.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForIndexedArray() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        processor.setProperty(bean, "array[1]", new String[] { "111" });
        assertEquals("111", bean.array[1]);
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForIndexedList() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        processor.setProperty(bean, "list[1]", new String[] { "111" });
        assertEquals("111", bean.list.get(1));
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForIndexedNestedBean() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        processor.setProperty(bean, "myBeanArrayArray[1][1].aaa",
                new String[] { "111" });
        assertEquals("111", bean.myBeanArrayArray[1][1].aaa);
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForIndexedNestedMap() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        processor.setProperty(bean, "mapArrayArray[1][1].aaa",
                new String[] { "111" });
        assertEquals("111", bean.mapArrayArray[1][1].get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForIndexedListMap() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        processor.setProperty(bean, "mapList[1].aaa", new String[] { "111" });
        assertEquals("111", bean.mapList.get(1).get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForArrayTypeMismatch() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        try {
            processor.setProperty(bean, "myBeanArrayArray[1][1]",
                    new String[] { "111" });
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testSetSimpleProperty() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        processor.setSimpleProperty(bean, "aaa", new String[] { "111" });
        assertEquals("111", bean.aaa);
    }

    /**
     * @throws Exception
     */
    public void testSetSimplePropertyForIllegalProperty() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        processor.setSimpleProperty(bean, "xxx", null);
    }

    /**
     * @throws Exception
     */
    public void testSetSimplePropertyForArray() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        processor.setSimpleProperty(bean, "array", new String[] { "111" });
        assertEquals(1, bean.array.length);
        assertEquals("111", bean.array[0]);
    }

    /**
     * @throws Exception
     */
    public void testSetSimplePropertyForList() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        processor.setSimpleProperty(bean, "list", new String[] { "111" });
        assertEquals(1, bean.list.size());
        assertEquals("111", bean.list.get(0));
    }

    /**
     * @throws Exception
     */
    public void testSetSimplePropertyForEmpty() throws Exception {
        HogeController bean = new HogeController();
        bean.aaa = "111";
        S3RequestProcessor processor = new S3RequestProcessor();
        processor.setSimpleProperty(bean, "aaa", new String[0]);
        assertNull(bean.aaa);
    }

    /**
     * @throws Exception
     */
    public void testGetSimplePropertyForNotNull() throws Exception {
        HogeController bean = new HogeController();
        MyBean myBean = new MyBean();
        bean.myBean = myBean;
        S3RequestProcessor processor = new S3RequestProcessor();
        assertSame(myBean, processor.getSimpleProperty(bean, "myBean"));
    }

    /**
     * @throws Exception
     */
    public void testGetSimplePropertyForNull() throws Exception {
        HogeController bean = new HogeController();
        S3RequestProcessor processor = new S3RequestProcessor();
        assertNotNull(processor.getSimpleProperty(bean, "myBean"));
        assertNotNull(bean.myBean);
    }

    /**
     * @throws Exception
     */
    public void testParseIndex() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        IndexParsedResult result = processor.parseIndex("12].aaa");
        assertEquals(1, result.indexes.length);
        assertEquals(12, result.indexes[0]);
        assertEquals("aaa", result.name);
    }

    /**
     * @throws Exception
     */
    public void testParseIndexForNest() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        IndexParsedResult result = processor.parseIndex("12][34].aaa");
        assertEquals(2, result.indexes.length);
        assertEquals(12, result.indexes[0]);
        assertEquals(34, result.indexes[1]);
        assertEquals("aaa", result.name);
    }

    /**
     * @throws Exception
     */
    public void testParseIndexForNoName() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        IndexParsedResult result = processor.parseIndex("12]");
        assertEquals(1, result.indexes.length);
        assertEquals(12, result.indexes[0]);
        assertEquals("", result.name);
    }

    /**
     * @throws Exception
     */
    public void testGetIndexedPropertyForArray() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        HogeController bean = new HogeController();
        MyBean result = (MyBean) processor.getIndexedProperty(bean,
                "myBeanArray", new int[] { 0 });
        assertNotNull(result);
        assertEquals(1, bean.myBeanArray.length);
    }

    /**
     * @throws Exception
     */
    public void testGetIndexedPropertyForNestedArray() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        HogeController bean = new HogeController();
        MyBean myBean = new MyBean();
        myBean.aaa = "111";
        bean.myBeanArrayArray = new MyBean[][] { new MyBean[] { myBean } };
        MyBean result = (MyBean) processor.getIndexedProperty(bean,
                "myBeanArrayArray", new int[] { 1, 2 });
        assertNotNull(result);
        assertEquals(2, bean.myBeanArrayArray.length);
        assertEquals(1, bean.myBeanArrayArray[0].length);
        assertEquals("111", bean.myBeanArrayArray[0][0].aaa);
        assertEquals(3, bean.myBeanArrayArray[1].length);
    }

    /**
     * @throws Exception
     */
    public void testGetIndexedPropertyForListBean() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        HogeController bean = new HogeController();
        MyBean result = (MyBean) processor.getIndexedProperty(bean,
                "myBeanList", new int[] { 0 });
        assertNotNull(result);
        assertEquals(1, bean.myBeanList.size());
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testGetIndexedPropertyForListMap() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        HogeController bean = new HogeController();
        Map result = (Map) processor.getIndexedProperty(bean, "mapList",
                new int[] { 0 });
        assertNotNull(result);
        assertEquals(1, bean.mapList.size());
    }

    /**
     * @throws Exception
     */
    public void testGetIndexedPropertyForNestedList() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        HogeController bean = new HogeController();
        MyBean myBean = new MyBean();
        myBean.aaa = "111";
        bean.myBeanListList = new ArrayList<List<MyBean>>();
        bean.myBeanListList.add(Arrays.asList(myBean));
        MyBean result = (MyBean) processor.getIndexedProperty(bean,
                "myBeanListList", new int[] { 1, 2 });
        assertNotNull(result);
        assertEquals(2, bean.myBeanListList.size());
        assertEquals(1, bean.myBeanListList.get(0).size());
        assertEquals("111", bean.myBeanListList.get(0).get(0).aaa);
        assertEquals(3, bean.myBeanListList.get(1).size());
    }

    /**
     * @throws Exception
     */
    public void testGetIndexedPropertyListNotParameterizedList()
            throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        HogeController bean = new HogeController();
        try {
            processor.getIndexedProperty(bean, "list2", new int[] { 1 });
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testGetIndexedPropertyNotListArray() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        HogeController bean = new HogeController();
        try {
            processor.getIndexedProperty(bean, "aaa", new int[] { 1, 2 });
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testExpandArray() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        int[] result = (int[]) processor.expandArray(new int[] { 1 },
                new int[] { 1 }, int.class);
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
    }

    /**
     * @throws Exception
     */
    public void testExpandArrayNest() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        int[][] result = (int[][]) processor.expandArray(
                new int[][] { new int[] { 1 } }, new int[] { 1, 2 }, int.class);
        assertEquals(2, result.length);
        assertEquals(1, result[0].length);
        assertEquals(1, result[0][0]);
        assertEquals(3, result[1].length);
    }

    /**
     * @throws Exception
     */
    public void testExpandArrayNestedBean() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        MyBean[][] result = (MyBean[][]) processor.expandArray(
                new MyBean[][] { new MyBean[] { new MyBean() } }, new int[] {
                        1, 2 }, MyBean.class);
        assertEquals(2, result.length);
        assertEquals(1, result[0].length);
        assertNotNull(result[0][0]);
        assertEquals(3, result[1].length);
    }

    /**
     * @throws Exception
     */
    public void testGetArrayElementType() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        assertEquals(MyBean.class, processor.getArrayElementClass(
                new MyBean[0][0].getClass(), 2));
    }

    /**
     * @throws Exception
     */
    public void testGetArrayValue() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        assertEquals(1, processor.getArrayValue(
                new int[][] { new int[] { 1 } }, new int[] { 0, 0 }, int.class));
        assertNotNull(processor.getArrayValue(
                new MyBean[][] { new MyBean[] { null } }, new int[] { 0, 0 },
                MyBean.class));
    }

    /**
     * @throws Exception
     */
    public void testConvertConcreteClass() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        assertEquals(int.class, processor.convertConcreteClass(int.class));
        assertEquals(HashMap.class, processor.convertConcreteClass(Map.class));
        assertEquals(ArrayList.class, processor
                .convertConcreteClass(List.class));
        try {
            processor.convertConcreteClass(Serializable.class);
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testSetArrayValue() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        int[] array = new int[] { 0 };
        processor.setArrayValue(array, new int[] { 0 }, 1);
        assertEquals(1, array[0]);
    }

    /**
     * @throws Exception
     */
    public void testSetArrayValueNest() throws Exception {
        S3RequestProcessor processor = new S3RequestProcessor();
        int[][] array = new int[][] { new int[] { 0 } };
        processor.setArrayValue(array, new int[] { 0, 0 }, 1);
        assertEquals(1, array[0][0]);
    }

    /**
     * @throws Exception
     */
    public void testExportPropertiesForward() throws Exception {
        S3ActionMapping mapping = new S3ActionMapping();
        mapping.setPath("/hoge");
        mapping.setControllerClass(HogeController.class);
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(HogeController.class
                .getMethod("index"));
        mapping.addExecuteConfig(executeConfig);
        S3RequestProcessor processor = new S3RequestProcessor();
        S3ModuleConfig moduleConfig = new S3ModuleConfig("");
        moduleConfig.addActionConfig(mapping);
        processor.init(new ActionServlet(), moduleConfig);
        HogeController action = new HogeController();
        ActionUtil.setAction(action);
        action.aaa = "111";
        action.bbb = "222";
        processor.exportProperties(request, response, BeanUtil
                .getBeanDesc(HogeController.class), false);
        assertEquals("111", request.getSession().getAttribute("aaa"));
        assertEquals("222", request.getAttribute("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testExportPropertiesRedirect() throws Exception {
        S3ActionMapping mapping = new S3ActionMapping();
        mapping.setPath("/hoge");
        mapping.setControllerClass(HogeController.class);
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(HogeController.class
                .getMethod("index"));
        mapping.addExecuteConfig(executeConfig);
        S3RequestProcessor processor = new S3RequestProcessor();
        S3ModuleConfig moduleConfig = new S3ModuleConfig("");
        moduleConfig.addActionConfig(mapping);
        processor.init(new ActionServlet(), moduleConfig);
        HogeController action = new HogeController();
        ActionUtil.setAction(action);
        action.aaa = "111";
        action.bbb = "222";
        processor.exportProperties(request, response, BeanUtil
                .getBeanDesc(HogeController.class), true);
        assertEquals("111", request.getSession().getAttribute("aaa"));
        assertNull(request.getAttribute("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testExportPropertiesRedirectPropertyIsNull() throws Exception {
        request.getSession().setAttribute("aaa", "111");
        S3ActionMapping mapping = new S3ActionMapping();
        mapping.setPath("/hoge");
        mapping.setControllerClass(HogeController.class);
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(HogeController.class
                .getMethod("index"));
        mapping.addExecuteConfig(executeConfig);
        S3RequestProcessor processor = new S3RequestProcessor();
        S3ModuleConfig moduleConfig = new S3ModuleConfig("");
        moduleConfig.addActionConfig(mapping);
        processor.init(new ActionServlet(), moduleConfig);
        HogeController action = new HogeController();
        ActionUtil.setAction(action);
        action.aaa = null;
        processor.exportProperties(request, response, BeanUtil
                .getBeanDesc(HogeController.class), true);
        assertNull(request.getSession().getAttribute("aaa"));
    }
}