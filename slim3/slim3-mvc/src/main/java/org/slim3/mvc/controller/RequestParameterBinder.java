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
package org.slim3.mvc.controller;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slim3.commons.bean.BeanDesc;
import org.slim3.commons.bean.BeanUtil;
import org.slim3.commons.bean.ParameterizedClassDesc;
import org.slim3.commons.bean.PropertyDesc;
import org.slim3.commons.exception.PropertyCanNotWriteRuntimeException;
import org.slim3.commons.util.ClassUtil;

/**
 * This class binds the request parameters to the controller.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class RequestParameterBinder {

    /**
     * The nested delimiter.
     */
    protected static final char NESTED_DELIM = '.';

    /**
     * The indexed start delimiter.
     */
    protected static final char INDEXED_DELIM = '[';

    /**
     * The indexed end delimiter.
     */
    protected static final char INDEXED_DELIM2 = ']';

    /**
     * Binds the request parameters to the controller.
     * 
     * @param controller
     *            the controller
     * @param parameters
     *            the request parameters
     * @throws NullPointerException
     *             if the controller parameter is null or if the parameters
     *             parameter is null
     * 
     */
    public void bind(Controller controller, Map<String, Object> parameters)
            throws NullPointerException {
        if (controller == null) {
            throw new NullPointerException("The controller parameter is null.");
        }
        if (parameters == null) {
            throw new NullPointerException("The parameters parameter is null.");
        }
        for (Iterator<String> i = parameters.keySet().iterator(); i.hasNext();) {
            String name = i.next();
            setProperty(controller, name, parameters.get(name));
        }
    }

    /**
     * Sets request parameter to the property of the action.
     * 
     * @param bean
     *            the bean
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     */
    protected void setProperty(Object bean, String name, Object value) {
        try {
            int nestedIndex = name.indexOf(NESTED_DELIM);
            int indexedIndex = name.indexOf(INDEXED_DELIM);
            if (nestedIndex < 0 && indexedIndex < 0) {
                setSimpleProperty(bean, name, value);
            } else if (nestedIndex >= 0 && indexedIndex >= 0) {
                if (nestedIndex < indexedIndex) {
                    setProperty(getSimpleProperty(bean, name.substring(
                        0,
                        nestedIndex)), name.substring(nestedIndex + 1), value);
                } else {
                    IndexParsedResult result =
                        parseIndex(name.substring(indexedIndex));
                    bean =
                        getIndexedProperty(bean, name
                            .substring(0, indexedIndex), result.indexes);
                    setProperty(bean, result.name, value);
                }
            } else if (nestedIndex >= 0) {
                Object o =
                    getSimpleProperty(bean, name.substring(0, nestedIndex));
                if (o == null) {
                    return;
                }
                setProperty(o, name.substring(nestedIndex + 1), value);
            } else {
                IndexParsedResult result =
                    parseIndex(name.substring(indexedIndex));
                setIndexedProperty(
                    bean,
                    name.substring(0, indexedIndex),
                    result.indexes,
                    value);
            }
        } catch (Throwable cause) {
            throw new PropertyCanNotWriteRuntimeException(
                bean.getClass(),
                name,
                value,
                cause);
        }
    }

    /**
     * Returns simple property.
     * 
     * @param bean
     *            the bean
     * @param name
     *            the property name
     * @return the property value
     */
    protected Object getSimpleProperty(Object bean, String name) {
        BeanDesc beanDesc = BeanUtil.getBeanDesc(bean.getClass());
        if (!beanDesc.hasPropertyDesc(name)) {
            return null;
        }
        PropertyDesc pd = beanDesc.getPropertyDesc(name);
        if (!pd.isReadable()) {
            return null;
        }
        Object value = pd.getValue(bean);
        if (value == null) {
            if (!Modifier.isAbstract(pd.getPropertyClass().getModifiers())) {
                value = ClassUtil.newInstance(pd.getPropertyClass());
                if (pd.isWritable()) {
                    pd.setValue(bean, value);
                }
            } else if (Map.class.isAssignableFrom(pd.getPropertyClass())) {
                value = new HashMap<String, Object>();
                if (pd.isWritable()) {
                    pd.setValue(bean, value);
                }
            }
        }
        return value;
    }

    /**
     * Sets simple property.
     * 
     * @param bean
     *            the bean
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     */
    @SuppressWarnings("unchecked")
    protected void setSimpleProperty(Object bean, String name, Object value) {
        if (bean instanceof Map) {
            Map m = (Map) bean;
            if (value instanceof String[]) {
                String[] values = (String[]) value;
                m.put(name, values.length > 0 ? values[0] : null);
            } else {
                m.put(name, value);
            }
            return;
        }
        BeanDesc beanDesc = BeanUtil.getBeanDesc(bean.getClass());
        if (!beanDesc.hasPropertyDesc(name)) {
            return;
        }
        PropertyDesc pd = beanDesc.getPropertyDesc(name);
        if (!pd.isWritable()) {
            return;
        }
        if (pd.getPropertyClass().isArray()) {
            pd.setValue(bean, value);
        } else if (List.class.isAssignableFrom(pd.getPropertyClass())) {
            List<String> list =
                Modifier.isAbstract(pd.getPropertyClass().getModifiers())
                    ? new ArrayList<String>()
                    : (List<String>) ClassUtil.newInstance(pd
                        .getPropertyClass());
            list.addAll(Arrays.asList((String[]) value));
            pd.setValue(bean, list);
        } else if (value == null) {
            pd.setValue(bean, null);
        } else if (value instanceof String[]) {
            String[] values = (String[]) value;
            pd.setValue(bean, values.length > 0 ? values[0] : null);
        } else {
            pd.setValue(bean, value);
        }
    }

    /**
     * Returns the indexed property.
     * 
     * @param bean
     *            the bean
     * @param name
     *            the property name
     * @param indexes
     *            The array of indexes
     * @return the value
     * 
     */
    @SuppressWarnings("unchecked")
    protected Object getIndexedProperty(Object bean, String name, int[] indexes) {
        BeanDesc beanDesc = BeanUtil.getBeanDesc(bean.getClass());
        if (!beanDesc.hasPropertyDesc(name)) {
            return null;
        }
        PropertyDesc pd = beanDesc.getPropertyDesc(name);
        if (!pd.isReadable()) {
            return null;
        }
        if (pd.getPropertyClass().isArray()) {
            Object array = pd.getValue(bean);
            Class<?> elementType =
                getArrayElementClass(pd.getPropertyClass(), indexes.length);
            if (array == null) {
                int[] newIndexes = new int[indexes.length];
                newIndexes[0] = indexes[0] + 1;
                array = Array.newInstance(elementType, newIndexes);
            }
            array = expandArray(array, indexes, elementType);
            pd.setValue(bean, array);
            return getArrayValue(array, indexes, elementType);
        } else if (List.class.isAssignableFrom(pd.getPropertyClass())) {
            List list = (List) pd.getValue(bean);
            if (list == null) {
                list = new ArrayList(Math.max(50, indexes[0]));
                pd.setValue(bean, list);
            }
            ParameterizedClassDesc pcd = pd.getParameterizedClassDesc();
            for (int i = 0; i < indexes.length; i++) {
                if (pcd == null
                    || !pcd.isParameterized()
                    || !List.class.isAssignableFrom(pcd.getRawClass())) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j <= i; j++) {
                        sb.append("[").append(indexes[j]).append("]");
                    }
                    throw new IllegalStateException("The property("
                        + pd.getName()
                        + sb
                        + ") of class("
                        + beanDesc.getBeanClass().getName()
                        + ") is not parametized.");
                }
                int size = list.size();
                pcd = pcd.getArguments()[0];
                for (int j = size; j <= indexes[i]; j++) {
                    if (i == indexes.length - 1) {
                        list.add(ClassUtil.newInstance(convertConcreteClass(pcd
                            .getRawClass())));
                    } else {
                        list.add(new ArrayList());
                    }
                }
                if (i < indexes.length - 1) {
                    list = (List) list.get(indexes[i]);
                }
            }
            return list.get(indexes[indexes.length - 1]);
        } else {
            throw new IllegalStateException("The property("
                + pd.getName()
                + ") of class("
                + beanDesc.getBeanClass().getName()
                + ") is not list and array.");
        }
    }

    /**
     * Sets indexed property.
     * 
     * @param bean
     *            the bean
     * @param name
     *            the property name
     * @param indexes
     *            the array of indexes
     * @param value
     *            the value
     */
    @SuppressWarnings("unchecked")
    protected void setIndexedProperty(Object bean, String name, int[] indexes,
            Object value) {
        BeanDesc beanDesc = BeanUtil.getBeanDesc(bean.getClass());
        if (!beanDesc.hasPropertyDesc(name)) {
            return;
        }
        PropertyDesc pd = beanDesc.getPropertyDesc(name);
        if (!pd.isWritable()) {
            return;
        }
        if (value.getClass().isArray() && Array.getLength(value) > 0) {
            value = Array.get(value, 0);
        }
        if (pd.getPropertyClass().isArray()) {
            Object array = pd.getValue(bean);
            Class<?> elementType =
                getArrayElementClass(pd.getPropertyClass(), indexes.length);
            if (array == null) {
                int[] newIndexes = new int[indexes.length];
                newIndexes[0] = indexes[0] + 1;
                array = Array.newInstance(elementType, newIndexes);
            }
            array = expandArray(array, indexes, elementType);
            pd.setValue(bean, array);
            setArrayValue(array, indexes, value);
        } else if (List.class.isAssignableFrom(pd.getPropertyClass())) {
            List list = (List) pd.getValue(bean);
            if (list == null) {
                list = new ArrayList(Math.max(50, indexes[0]));
                pd.setValue(bean, list);
            }
            ParameterizedClassDesc pcd = pd.getParameterizedClassDesc();
            for (int i = 0; i < indexes.length; i++) {
                if (pcd == null
                    || !pcd.isParameterized()
                    || !List.class.isAssignableFrom(pcd.getRawClass())) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j <= i; j++) {
                        sb.append("[").append(indexes[j]).append("]");
                    }
                    throw new IllegalStateException("The property("
                        + pd.getName()
                        + sb
                        + ") of class("
                        + beanDesc.getBeanClass().getName()
                        + ") is not List<T>.");
                }
                int size = list.size();
                pcd = pcd.getArguments()[0];
                for (int j = size; j <= indexes[i]; j++) {
                    if (i == indexes.length - 1) {
                        list.add(ClassUtil.newInstance(convertConcreteClass(pcd
                            .getRawClass())));
                    } else {
                        list.add(new ArrayList());
                    }
                }
                if (i < indexes.length - 1) {
                    list = (List) list.get(indexes[i]);
                }
            }
            list.set(indexes[indexes.length - 1], value);
        } else {
            throw new IllegalStateException("The property("
                + pd.getName()
                + ") of class("
                + beanDesc.getBeanClass().getName()
                + ") is not list and array.");
        }
    }

    /**
     * Returns the element class of the array.
     * 
     * @param clazz
     *            the class of the array
     * @param depth
     *            the array depth
     * @return the element class of the array
     */
    protected Class<?> getArrayElementClass(Class<?> clazz, int depth) {
        for (int i = 0; i < depth; i++) {
            clazz = clazz.getComponentType();
        }
        return clazz;
    }

    /**
     * Expands the array.
     * 
     * @param array
     *            the array
     * @param indexes
     *            the array of indexes
     * @param elementClass
     *            the element class of the array
     * @return expanded array
     */
    protected Object expandArray(Object array, int[] indexes,
            Class<?> elementClass) {
        int length = Array.getLength(array);
        if (length <= indexes[0]) {
            int[] newIndexes = new int[indexes.length];
            newIndexes[0] = indexes[0] + 1;
            Object newArray = Array.newInstance(elementClass, newIndexes);
            System.arraycopy(array, 0, newArray, 0, length);
            array = newArray;
        }
        if (indexes.length > 1) {
            int[] newIndexes = new int[indexes.length - 1];
            for (int i = 1; i < indexes.length; i++) {
                newIndexes[i - 1] = indexes[i];
            }
            Array.set(array, indexes[0], expandArray(Array.get(
                array,
                indexes[0]), newIndexes, elementClass));
        }
        return array;
    }

    /**
     * Adds the int value to the array.
     * 
     * @param array
     *            the array
     * @param value
     *            the int value
     * @return added array.
     */
    protected int[] addArray(int[] array, int value) {
        if (array == null) {
            throw new NullPointerException("The array parameter is null.");
        }
        int[] newArray = (int[]) Array.newInstance(int.class, array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = value;
        return newArray;
    }

    /**
     * Returns the value of the array.
     * 
     * @param array
     *            the array
     * @param indexes
     *            the array of indexes
     * @param elementClass
     *            the element class of the array
     * @return the value of the array
     */
    protected Object getArrayValue(Object array, int[] indexes,
            Class<?> elementClass) {
        Object value = array;
        elementClass = convertConcreteClass(elementClass);
        for (int i = 0; i < indexes.length; i++) {
            Object value2 = Array.get(value, indexes[i]);
            if (i == indexes.length - 1 && value2 == null) {
                value2 = ClassUtil.newInstance(elementClass);
                Array.set(value, indexes[i], value2);
            }
            value = value2;
        }
        return value;
    }

    /**
     * Sets the value of the array.
     * 
     * @param array
     *            the array
     * @param indexes
     *            the array of indexes
     * @param value
     *            the value of the array
     */
    protected void setArrayValue(Object array, int[] indexes, Object value) {
        for (int i = 0; i < indexes.length - 1; i++) {
            array = Array.get(array, indexes[i]);
        }
        Array.set(array, indexes[indexes.length - 1], value);
    }

    /**
     * Converts the abstract class to the concrete class.
     * 
     * @param clazz
     *            the class
     * @return converted class
     */
    protected Class<?> convertConcreteClass(Class<?> clazz) {
        if (!clazz.isPrimitive() && Modifier.isAbstract(clazz.getModifiers())) {
            if (Map.class.isAssignableFrom(clazz)) {
                return HashMap.class;
            } else if (List.class.isAssignableFrom(clazz)) {
                return ArrayList.class;
            } else {
                throw new IllegalArgumentException("The class("
                    + clazz.getName()
                    + ") can not be converted to concrete class.");
            }
        }
        return clazz;
    }

    /**
     * Parses the indexed property name.
     * 
     * @param name
     *            the property name
     * @return the parsed result.
     */
    protected IndexParsedResult parseIndex(String name) {
        IndexParsedResult result = new IndexParsedResult();
        while (true) {
            int index = name.indexOf(INDEXED_DELIM2);
            if (index < 0) {
                throw new IllegalArgumentException(INDEXED_DELIM2
                    + " is not found in ("
                    + name
                    + ").");
            }
            result.indexes =
                addArray(result.indexes, Integer.valueOf(
                    name.substring(1, index)).intValue());
            name = name.substring(index + 1);
            if (name.length() == 0) {
                break;
            } else if (name.charAt(0) == INDEXED_DELIM) {
                continue;
            } else if (name.charAt(0) == NESTED_DELIM) {
                name = name.substring(1);
                break;
            } else {
                throw new IllegalArgumentException(name);
            }
        }
        result.name = name;
        return result;
    }

    /**
     * 
     */
    protected static class IndexParsedResult {
        /**
         * The array of indexes.
         */
        public int[] indexes = new int[0];

        /**
         * The name except index part.
         */
        public String name;
    }
}