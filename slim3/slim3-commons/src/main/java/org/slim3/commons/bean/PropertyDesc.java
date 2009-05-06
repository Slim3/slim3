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
package org.slim3.commons.bean;

import java.lang.reflect.Method;

import org.slim3.commons.exception.PropertyCanNotReadRuntimeException;
import org.slim3.commons.exception.PropertyCanNotWriteRuntimeException;
import org.slim3.commons.exception.PropertyNotReadableRuntimeException;
import org.slim3.commons.exception.PropertyNotWritableRuntimeException;
import org.slim3.commons.exception.WrapRuntimeException;
import org.slim3.commons.util.ConversionUtil;

/**
 * This class describes a property.
 * 
 * @author higa
 * @since 3.0
 */
public final class PropertyDesc {

    private String name;

    private Class<?> propertyClass;

    private Class<?> beanClass;

    private Method readMethod;

    private Method writeMethod;

    private ParameterizedClassDesc parameterizedClassDesc;

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
        if (parameterizedClassDesc == null) {
            parameterizedClassDesc =
                ParameterizedClassDesc
                    .create(readMethod.getGenericReturnType());
        }
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
        if (parameterizedClassDesc == null) {
            parameterizedClassDesc =
                ParameterizedClassDesc.create(writeMethod
                    .getGenericParameterTypes()[0]);
        }
    }

    /**
     * Returns the property value.
     * 
     * @param bean
     *            the bean
     * @return the value.
     * @throws PropertyNotReadableRuntimeException
     *             if the property is not readable
     * @throws PropertyCanNotReadRuntimeException
     *             if an exception is encountered while trying to read the
     *             property
     */
    public Object getValue(Object bean)
            throws PropertyNotReadableRuntimeException,
            PropertyCanNotReadRuntimeException {
        if (!isReadable()) {
            throw new PropertyNotReadableRuntimeException(beanClass, name);
        }
        try {
            return readMethod.invoke(bean);
        } catch (Throwable t) {
            throw new PropertyCanNotReadRuntimeException(beanClass, name, t);
        }
    }

    /**
     * Sets the property value to the bean.
     * 
     * @param bean
     *            the bean
     * @param value
     *            the value
     * @throws PropertyNotWritableRuntimeException
     *             if the property is not writable
     * @throws PropertyCanNotWriteRuntimeException
     *             if an other exception is encountered while trying to write
     *             value to the property
     */
    public void setValue(Object bean, Object value)
            throws PropertyNotWritableRuntimeException,
            PropertyCanNotWriteRuntimeException {
        if (!isWritable()) {
            throw new PropertyNotWritableRuntimeException(beanClass, name);
        }
        try {
            value = ConversionUtil.convert(value, propertyClass);
            writeMethod.invoke(bean, value);
        } catch (Throwable t) {
            if (t.getClass() == WrapRuntimeException.class) {
                t = t.getCause();
            }
            throw new PropertyCanNotWriteRuntimeException(
                beanClass,
                name,
                value,
                t);
        }
    }

    /**
     * Returns the parameterized class descriptor.
     * 
     * @return the parameterized class descriptor
     */
    public ParameterizedClassDesc getParameterizedClassDesc() {
        return parameterizedClassDesc;
    }
}