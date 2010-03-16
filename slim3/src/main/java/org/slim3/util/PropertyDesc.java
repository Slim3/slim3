/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.slim3.util;

import java.lang.reflect.Method;

/**
 * This class describes a property.
 * 
 * @author higa
 * @since 1.0.0
 */
public final class PropertyDesc {

    private String name;

    private Class<?> propertyClass;

    private Class<?> beanClass;

    private Method readMethod;

    private Method writeMethod;

    /**
     * Constructor.
     * 
     * @param name
     *            the property name
     * @param propertyClass
     *            the property class
     * @param beanClass
     *            the bean class
     * @throws NullPointerException
     *             if the propertyName parameter is null or the propertyClass
     *             parameter is null or the beanClass parameter is null
     */
    PropertyDesc(String name, Class<?> propertyClass, Class<?> beanClass)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        if (propertyClass == null) {
            throw new NullPointerException(
                "The propertyClass parameter is null.");
        }
        if (beanClass == null) {
            throw new NullPointerException("The beanClass parameter is null.");
        }
        this.name = name;
        this.propertyClass = propertyClass;
        this.beanClass = beanClass;
    }

    /**
     * Returns the property name.
     * 
     * @return the property name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the property class
     * 
     * @return the property class.
     */
    public Class<?> getPropertyClass() {
        return propertyClass;
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
     * Returns the read method.
     * 
     * @return the read method
     */
    public Method getReadMethod() {
        return readMethod;
    }

    /**
     * Determines if this property is readable.
     * 
     * @return whether this property is readable
     */
    public boolean isReadable() {
        return readMethod != null;
    }

    void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    /**
     * Returns the write method.
     * 
     * @return the write method
     */
    public Method getWriteMethod() {
        return writeMethod;
    }

    /**
     * Determines if this property is writable.
     * 
     * @return whether this property is writable
     */
    public boolean isWritable() {
        return writeMethod != null;
    }

    void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }

    /**
     * Returns the property value.
     * 
     * @param bean
     *            the bean
     * @return the value.
     * @throws IllegalStateException
     *             if the property is not readable
     * @throws WrapRuntimeException
     *             if reading the property is a failure.
     */
    public Object getValue(Object bean) throws IllegalStateException,
            WrapRuntimeException {
        if (!isReadable()) {
            throw new IllegalStateException("The property("
                + name
                + ") of the class("
                + beanClass.getName()
                + ") is not readable.");
        }
        try {
            try {
                return readMethod.invoke(bean);
            } catch (IllegalAccessException e) {
                readMethod.setAccessible(true);
                return readMethod.invoke(bean);
            }
        } catch (Throwable cause) {
            throw new WrapRuntimeException("Reading the property("
                + name
                + ") of the class("
                + beanClass.getName()
                + ") is a failure. Error message: "
                + cause.getMessage(), cause);
        }
    }

    /**
     * Sets the property value to the bean.
     * 
     * @param bean
     *            the bean
     * @param value
     *            the value
     * @throws IllegalStateException
     *             if the property is not writable
     * @throws WrapRuntimeException
     *             if an error occurred while writing the value to the property
     */
    public void setValue(Object bean, Object value)
            throws IllegalStateException, WrapRuntimeException {
        if (!isWritable()) {
            throw new IllegalStateException("The property("
                + name
                + ") of the class("
                + beanClass.getName()
                + ") is not writable.");
        }
        try {
            value = ConversionUtil.convert(value, propertyClass);
            try {
                writeMethod.invoke(bean, value);
            } catch (IllegalAccessException e) {
                writeMethod.setAccessible(true);
                writeMethod.invoke(bean, value);
            }
        } catch (Throwable cause) {
            throw new WrapRuntimeException("Writing the value("
                + value
                + ") to the property("
                + name
                + ") of the class("
                + beanClass.getName()
                + ") is a failure. Error message: "
                + cause.getMessage(), cause);
        }
    }
}