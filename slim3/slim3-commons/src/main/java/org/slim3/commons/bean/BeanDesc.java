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
package org.slim3.commons.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slim3.commons.exception.PropertyNotFoundRuntimeException;
import org.slim3.commons.util.CaseInsensitiveMap;
import org.slim3.commons.util.StringUtil;

/**
 * This class describes bean information.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class BeanDesc {

    private Class<?> beanClass;

    private CaseInsensitiveMap<PropertyDesc> propertyDescCache = new CaseInsensitiveMap<PropertyDesc>();

    /**
     * Constructor.
     * 
     * @param beanClass
     *            the bean class
     */
    BeanDesc(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * Creates the bean descriptor.
     * 
     * @param beanClass
     *            the bean class
     * @return the bean descriptor
     * @throws NullPointerException
     *             if the beanClass parameter is null
     */
    public static BeanDesc create(Class<?> beanClass) {
        if (beanClass == null) {
            throw new NullPointerException("The beanClass parameter is null.");
        }
        BeanDesc beanDesc = new BeanDesc(beanClass);
        setupPropertyDesc(beanDesc);
        return beanDesc;
    }

    private static void setupPropertyDesc(BeanDesc beanDesc) {
        Set<String> illegalPropertyNames = new HashSet<String>();
        Method[] methods = beanDesc.beanClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            if (m.isBridge() || m.isSynthetic()) {
                continue;
            }
            String methodName = m.getName();
            if (methodName.startsWith("get")) {
                if (m.getParameterTypes().length != 0
                        || methodName.equals("getClass")
                        || m.getReturnType() == void.class) {
                    continue;
                }
                String propertyName = StringUtil.decapitalize(methodName
                        .substring(3));
                setupReadMethod(beanDesc, m, propertyName, illegalPropertyNames);
            } else if (methodName.startsWith("is")) {
                if (m.getParameterTypes().length != 0
                        || m.getReturnType() != boolean.class) {
                    continue;
                }
                String propertyName = StringUtil.decapitalize(methodName
                        .substring(2));
                setupReadMethod(beanDesc, m, propertyName, illegalPropertyNames);
            } else if (methodName.startsWith("set")) {
                if (m.getParameterTypes().length != 1
                        || methodName.equals("setClass")
                        || m.getReturnType() != void.class) {
                    continue;
                }
                String propertyName = StringUtil.decapitalize(methodName
                        .substring(3));
                setupWriteMethod(beanDesc, m, propertyName,
                        illegalPropertyNames);
            }
        }
        for (Iterator<String> i = illegalPropertyNames.iterator(); i.hasNext();) {
            beanDesc.propertyDescCache.remove(i.next());
        }
        addFields(beanDesc, beanDesc.beanClass);
        for (Class<?> superClass = beanDesc.beanClass.getSuperclass(); superClass != Object.class
                && superClass != null; superClass = superClass.getSuperclass()) {
            addFields(beanDesc, superClass);
        }
    }

    private static void setupReadMethod(BeanDesc beanDesc, Method readMethod,
            String propertyName, Set<String> illegalPropertyNames) {
        Class<?> propertyClass = readMethod.getReturnType();
        PropertyDesc propDesc = beanDesc.propertyDescCache.get(propertyName);
        if (propDesc != null) {
            if (!propDesc.getPropertyClass().equals(propertyClass)) {
                illegalPropertyNames.add(propertyName);
            } else {
                propDesc.setReadMethod(readMethod);
            }
        } else {
            propDesc = new PropertyDesc(propertyName, propertyClass,
                    beanDesc.beanClass);
            propDesc.setReadMethod(readMethod);
            beanDesc.propertyDescCache.put(propertyName, propDesc);
        }
    }

    private static void setupWriteMethod(BeanDesc beanDesc, Method writeMethod,
            String propertyName, Set<String> illegalPropertyNames) {
        Class<?> propertyClass = writeMethod.getParameterTypes()[0];
        PropertyDesc propDesc = beanDesc.propertyDescCache.get(propertyName);
        if (propDesc != null) {
            if (!propDesc.getPropertyClass().equals(propertyClass)) {
                illegalPropertyNames.add(propertyName);
            } else {
                propDesc.setWriteMethod(writeMethod);
            }
        } else {
            propDesc = new PropertyDesc(propertyName, propertyClass,
                    beanDesc.beanClass);
            propDesc.setWriteMethod(writeMethod);
            beanDesc.propertyDescCache.put(propertyName, propDesc);
        }
    }

    private static void addFields(BeanDesc beanDesc, Class<?> beanClass) {
        Field[] fields = beanClass.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            String fname = field.getName();
            if (!Modifier.isStatic(field.getModifiers())
                    && !Modifier.isFinal(field.getModifiers())) {
                PropertyDesc pd = beanDesc.propertyDescCache.get(fname);
                if (pd != null) {
                    pd.setField(field);
                } else if (Modifier.isPublic(field.getModifiers())) {
                    pd = new PropertyDesc(field.getName(), field.getType(),
                            beanClass);
                    pd.setField(field);
                    beanDesc.propertyDescCache.put(fname, pd);
                }

            }
        }
    }

    /**
     * Returns the bean class.
     * 
     * @return the bean class
     */
    public Class<?> getBeanClass() {
        return beanClass;
    }

    /**
     * Returns true if this class has the property.
     * 
     * @param propertyName
     *            the property name
     * @return whether this class has the property
     */
    public boolean hasPropertyDesc(String propertyName) {
        return propertyDescCache.containsKey(propertyName);
    }

    /**
     * Returns the property descriptor.
     * 
     * @param propertyName
     *            the property name
     * @return the property descriptor
     * @throws PropertyNotFoundRuntimeException
     *             if the property is not found
     */
    public PropertyDesc getPropertyDesc(String propertyName)
            throws PropertyNotFoundRuntimeException {
        PropertyDesc pd = propertyDescCache.get(propertyName);
        if (pd == null) {
            throw new PropertyNotFoundRuntimeException(beanClass, propertyName);
        }
        return pd;
    }

    /**
     * Returns the property descriptor for index.
     * 
     * @param index
     *            the index
     * @return the property descriptor
     */
    public PropertyDesc getPropertyDesc(int index) {
        return propertyDescCache.get(index);
    }

    /**
     * Returns the number of property descriptors.
     * 
     * @return the number of property descriptors.
     */
    public int getPropertyDescSize() {
        return propertyDescCache.size();
    }
}
