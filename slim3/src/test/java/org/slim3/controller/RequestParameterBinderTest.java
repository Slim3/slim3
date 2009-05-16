/*
 * Copyright 2004-2009 the original author or authors.
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
package org.slim3.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.slim3.controller.RequestParameterBinder.IndexParsedResult;
import org.slim3.controller.controller.HogeController;
import org.slim3.controller.controller.MyBean;
import org.slim3.exception.WrapRuntimeException;

/**
 * @author higa
 * 
 */
public class RequestParameterBinderTest extends TestCase {

    private HogeController bean = new HogeController();

    private RequestParameterBinder binder = new RequestParameterBinder();

    /**
     * @throws Exception
     */
    public void testBind() throws Exception {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("aaa", new String[] { "111" });
        binder.bind(bean, parameters);
        assertEquals("111", bean.getAaa());
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForSimple() throws Exception {
        binder.setProperty(bean, "aaa", new String[] { "111" });
        assertEquals("111", bean.getAaa());
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForNestedBean() throws Exception {
        binder.setProperty(bean, "bean.aaa", new String[] { "111" });
        assertEquals("111", bean.getBean().getAaa());
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForNestedMap() throws Exception {
        binder.setProperty(bean, "map.aaa", new String[] { "111" });
        assertEquals("111", bean.getMap().get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForIndexedArray() throws Exception {
        binder.setProperty(bean, "array[1]", new String[] { "111" });
        assertEquals("111", bean.getArray()[1]);
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForIndexedList() throws Exception {
        binder.setProperty(bean, "list[1]", new String[] { "111" });
        assertEquals("111", bean.getList().get(1));
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForIndexedNestedBean() throws Exception {
        binder.setProperty(
            bean,
            "beanArrayArray[1][1].aaa",
            new String[] { "111" });
        assertEquals("111", bean.getBeanArrayArray()[1][1].getAaa());
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForIndexedNestedMap() throws Exception {
        binder.setProperty(
            bean,
            "mapArrayArray[1][1].aaa",
            new String[] { "111" });
        assertEquals("111", bean.getMapArrayArray()[1][1].get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForIndexedListMap() throws Exception {
        binder.setProperty(bean, "mapList[1].aaa", new String[] { "111" });
        assertEquals("111", bean.getMapList().get(1).get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testSetPropertyForArrayTypeMismatch() throws Exception {
        try {
            binder.setProperty(
                bean,
                "beanArrayArray[1][1]",
                new String[] { "111" });
            fail();
        } catch (WrapRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testSetSimpleProperty() throws Exception {
        binder.setSimpleProperty(bean, "aaa", new String[] { "111" });
        assertEquals("111", bean.getAaa());
    }

    /**
     * @throws Exception
     */
    public void testSetSimplePropertyForIllegalProperty() throws Exception {
        HogeController bean = new HogeController();
        binder.setSimpleProperty(bean, "xxx", null);
    }

    /**
     * @throws Exception
     */
    public void testSetSimplePropertyForArray() throws Exception {
        binder.setSimpleProperty(bean, "array", new String[] { "111" });
        assertEquals(1, bean.getArray().length);
        assertEquals("111", bean.getArray()[0]);
    }

    /**
     * @throws Exception
     */
    public void testSetSimplePropertyForList() throws Exception {
        binder.setSimpleProperty(bean, "list", new String[] { "111" });
        assertEquals(1, bean.getList().size());
        assertEquals("111", bean.getList().get(0));
    }

    /**
     * @throws Exception
     */
    public void testSetSimplePropertyForEmptyString() throws Exception {
        bean.setAaa("111");
        binder.setSimpleProperty(bean, "aaa", new String[0]);
        assertNull(bean.getAaa());
    }

    /**
     * @throws Exception
     */
    public void testGetSimplePropertyForNotNull() throws Exception {
        MyBean myBean = new MyBean();
        bean.setBean(myBean);
        assertSame(myBean, binder.getSimpleProperty(bean, "bean"));
    }

    /**
     * @throws Exception
     */
    public void testGetSimplePropertyForNull() throws Exception {
        assertNotNull(binder.getSimpleProperty(bean, "bean"));
        assertNotNull(bean.getBean());
    }

    /**
     * @throws Exception
     */
    public void testParseIndex() throws Exception {
        IndexParsedResult result = binder.parseIndex("[12].aaa");
        assertEquals(1, result.indexes.length);
        assertEquals(12, result.indexes[0]);
        assertEquals("aaa", result.name);
    }

    /**
     * @throws Exception
     */
    public void testParseIndexForNest() throws Exception {
        IndexParsedResult result = binder.parseIndex("[12][34].aaa");
        assertEquals(2, result.indexes.length);
        assertEquals(12, result.indexes[0]);
        assertEquals(34, result.indexes[1]);
        assertEquals("aaa", result.name);
    }

    /**
     * @throws Exception
     */
    public void testParseIndexForNoName() throws Exception {
        IndexParsedResult result = binder.parseIndex("[12]");
        assertEquals(1, result.indexes.length);
        assertEquals(12, result.indexes[0]);
        assertEquals("", result.name);
    }

    /**
     * @throws Exception
     */
    public void testGetIndexedPropertyForArray() throws Exception {
        MyBean result =
            (MyBean) binder.getIndexedProperty(
                bean,
                "beanArray",
                new int[] { 0 });
        assertNotNull(result);
        assertEquals(1, bean.getBeanArray().length);
    }

    /**
     * @throws Exception
     */
    public void testGetIndexedPropertyForNestedArray() throws Exception {
        MyBean myBean = new MyBean();
        myBean.setAaa("111");
        bean.setBeanArrayArray(new MyBean[][] { new MyBean[] { myBean } });
        MyBean result =
            (MyBean) binder.getIndexedProperty(
                bean,
                "beanArrayArray",
                new int[] { 1, 2 });
        assertNotNull(result);
        assertEquals(2, bean.getBeanArrayArray().length);
        assertEquals(1, bean.getBeanArrayArray()[0].length);
        assertEquals("111", bean.getBeanArrayArray()[0][0].getAaa());
        assertEquals(3, bean.getBeanArrayArray()[1].length);
    }

    /**
     * @throws Exception
     */
    public void testGetIndexedPropertyForListBean() throws Exception {
        MyBean result =
            (MyBean) binder.getIndexedProperty(
                bean,
                "beanList",
                new int[] { 0 });
        assertNotNull(result);
        assertEquals(1, bean.getBeanList().size());
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testGetIndexedPropertyForListMap() throws Exception {
        Map result =
            (Map) binder.getIndexedProperty(bean, "mapList", new int[] { 0 });
        assertNotNull(result);
        assertEquals(1, bean.getMapList().size());
    }

    /**
     * @throws Exception
     */
    public void testGetIndexedPropertyForNestedList() throws Exception {
        MyBean myBean = new MyBean();
        myBean.setAaa("111");
        bean.setBeanListList(new ArrayList<List<MyBean>>());
        bean.getBeanListList().add(Arrays.asList(myBean));
        MyBean result =
            (MyBean) binder.getIndexedProperty(bean, "beanListList", new int[] {
                1,
                2 });
        assertNotNull(result);
        assertEquals(2, bean.getBeanListList().size());
        assertEquals(1, bean.getBeanListList().get(0).size());
        assertEquals("111", bean.getBeanListList().get(0).get(0).getAaa());
        assertEquals(3, bean.getBeanListList().get(1).size());
    }

    /**
     * @throws Exception
     */
    public void testGetIndexedPropertyListNotParameterizedList()
            throws Exception {
        try {
            binder.getIndexedProperty(bean, "list2", new int[] { 1 });
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testGetIndexedPropertyNotListAndArray() throws Exception {
        try {
            binder.getIndexedProperty(bean, "aaa", new int[] { 1, 2 });
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testExpandArray() throws Exception {
        int[] result =
            (int[]) binder.expandArray(
                new int[] { 1 },
                new int[] { 1 },
                int.class);
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
    }

    /**
     * @throws Exception
     */
    public void testExpandArrayForNest() throws Exception {
        int[][] result =
            (int[][]) binder.expandArray(
                new int[][] { new int[] { 1 } },
                new int[] { 1, 2 },
                int.class);
        assertEquals(2, result.length);
        assertEquals(1, result[0].length);
        assertEquals(1, result[0][0]);
        assertEquals(3, result[1].length);
    }

    /**
     * @throws Exception
     */
    public void testExpandArrayForNestedBean() throws Exception {
        MyBean[][] result =
            (MyBean[][]) binder.expandArray(
                new MyBean[][] { new MyBean[] { new MyBean() } },
                new int[] { 1, 2 },
                MyBean.class);
        assertEquals(2, result.length);
        assertEquals(1, result[0].length);
        assertNotNull(result[0][0]);
        assertEquals(3, result[1].length);
    }

    /**
     * @throws Exception
     */
    public void testGetArrayElementType() throws Exception {
        assertEquals(MyBean.class, binder.getArrayElementClass(new MyBean[0][0]
            .getClass(), 2));
    }

    /**
     * @throws Exception
     */
    public void testGetArrayValue() throws Exception {
        assertEquals(1, binder.getArrayValue(
            new int[][] { new int[] { 1 } },
            new int[] { 0, 0 },
            int.class));
        assertNotNull(binder.getArrayValue(
            new MyBean[][] { new MyBean[] { null } },
            new int[] { 0, 0 },
            MyBean.class));
    }

    /**
     * @throws Exception
     */
    public void testToConcreteClass() throws Exception {
        assertEquals(int.class, binder.toConcreteClass(int.class));
        assertEquals(HashMap.class, binder.toConcreteClass(Map.class));
        assertEquals(ArrayList.class, binder.toConcreteClass(List.class));
        try {
            binder.toConcreteClass(Serializable.class);
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testSetArrayValue() throws Exception {
        int[] array = new int[] { 0 };
        binder.setArrayValue(array, new int[] { 0 }, 1);
        assertEquals(1, array[0]);
    }

    /**
     * @throws Exception
     */
    public void testSetArrayValueForNest() throws Exception {
        int[][] array = new int[][] { new int[] { 0 } };
        binder.setArrayValue(array, new int[] { 0, 0 }, 1);
        assertEquals(1, array[0][0]);
    }
}