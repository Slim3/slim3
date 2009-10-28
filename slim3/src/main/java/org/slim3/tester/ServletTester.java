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
package org.slim3.tester;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slim3.util.BooleanUtil;
import org.slim3.util.DateUtil;
import org.slim3.util.DoubleUtil;
import org.slim3.util.FloatUtil;
import org.slim3.util.IntegerUtil;
import org.slim3.util.LocaleLocator;
import org.slim3.util.LongUtil;
import org.slim3.util.NumberUtil;
import org.slim3.util.RequestLocator;
import org.slim3.util.ResponseLocator;
import org.slim3.util.ServletContextLocator;
import org.slim3.util.ShortUtil;
import org.slim3.util.StringUtil;
import org.slim3.util.TimeZoneLocator;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * A test case for Servlet environment.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class ServletTester extends LocalServiceTester {

    /**
     * The mock for {@link ServletContext}.
     */
    public MockServletContext servletContext = new MockServletContext();

    /**
     * The mock for {@link ServletConfig}.
     */
    public MockServletConfig config = new MockServletConfig(servletContext);

    /**
     * The mock for {@link HttpServletRequest}.
     */
    public MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    /**
     * The mock for {@link HttpServletResponse}.
     */
    public MockHttpServletResponse response = new MockHttpServletResponse();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ServletContextLocator.set(servletContext);
        RequestLocator.set(request);
        ResponseLocator.set(response);
        LocaleLocator.set(Locale.US);
        TimeZoneLocator.set(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public void tearDown() throws Exception {
        servletContext = null;
        config = null;
        request = null;
        response = null;
        ServletContextLocator.set(null);
        RequestLocator.set(null);
        ResponseLocator.set(null);
        LocaleLocator.set(null);
        TimeZoneLocator.set(null);
        super.tearDown();
    }

    /**
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     */
    public String param(String name) {
        return request.getParameter(name);
    }

    /**
     * Sets the request parameter.
     * 
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     */
    public void param(String name, Object value) {
        request.setParameter(name, StringUtil.toString(value));
    }

    /**
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     */
    public String[] paramValues(String name) {
        return request.getParameterValues(name);
    }

    /**
     * Returns the request attribute value as short.
     * 
     * @param name
     *            the attribute name
     * @return the short attribute value
     */
    public Short asShort(String name) {
        return ShortUtil.toShort(request.getAttribute(name));
    }

    /**
     * Returns the request attribute value as short.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the short attribute value
     */
    public Short asShort(String name, String pattern) {
        return ShortUtil.toShort(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
     * Returns the request attribute value as integer.
     * 
     * @param name
     *            the attribute name
     * @return the integer attribute value
     */
    public Integer asInteger(String name) {
        return IntegerUtil.toInteger(request.getAttribute(name));
    }

    /**
     * Returns the request attribute value as integer.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the integer attribute value
     */
    public Integer asInteger(String name, String pattern) {
        return IntegerUtil.toInteger(NumberUtil.toNumber(
            asString(name),
            pattern));
    }

    /**
     * Returns the request attribute value as long.
     * 
     * @param name
     *            the attribute name
     * @return the long attribute value
     */
    public Long asLong(String name) {
        return LongUtil.toLong(request.getAttribute(name));
    }

    /**
     * Returns the request attribute value as long.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the long attribute value
     */
    public Long asLong(String name, String pattern) {
        return LongUtil.toLong(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
     * Returns the request attribute value as float.
     * 
     * @param name
     *            the attribute name
     * @return the float attribute value
     */
    public Float asFloat(String name) {
        return FloatUtil.toFloat(request.getAttribute(name));
    }

    /**
     * Returns the request attribute value as float.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the float attribute value
     */
    public Float asFloat(String name, String pattern) {
        return FloatUtil.toFloat(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
     * Returns the request attribute value as double.
     * 
     * @param name
     *            the attribute name
     * @return the double attribute value
     */
    public Double asDouble(String name) {
        return DoubleUtil.toDouble(request.getAttribute(name));
    }

    /**
     * Returns the request attribute value as double.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the double attribute value
     */
    public Double asDouble(String name, String pattern) {
        return DoubleUtil
            .toDouble(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
     * Returns the request attribute value as string.
     * 
     * @param name
     *            the attribute name
     * @return the string attribute value
     */
    public String asString(String name) {
        return StringUtil.toString(request.getAttribute(name));
    }

    /**
     * Returns the request attribute value as boolean.
     * 
     * @param name
     *            the attribute name
     * @return the boolean attribute value
     */
    public Boolean asBoolean(String name) {
        return BooleanUtil.toBoolean(request.getAttribute(name));
    }

    /**
     * Returns the request attribute value as date.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the date attribute value
     */
    public Date asDate(String name, String pattern) {
        return DateUtil.toDate(asString(name), pattern);
    }

    /**
     * Returns the request attribute value as {@link Key}.
     * 
     * @param name
     *            the attribute name
     * @return the request attribute value as {@link Key}
     */
    public Key asKey(String name) {
        Object key = request.getAttribute(name);
        if (key == null) {
            return null;
        }
        if (key instanceof Key) {
            return (Key) key;
        }
        return KeyFactory.stringToKey(key.toString());
    }

    /**
     * Sets the request parameter.
     * 
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     */
    public void paramValues(String name, String[] value) {
        request.setParameter(name, value);
    }

    /**
     * Returns the request attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the request attribute
     */
    @SuppressWarnings("unchecked")
    public <T> T requestScope(String name) {
        return (T) request.getAttribute(name);
    }

    /**
     * Sets the request attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    public void requestScope(String name, Object value) {
        request.setAttribute(name, value);
    }

    /**
     * Returns the session attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the attribute value
     */
    @SuppressWarnings("unchecked")
    public <T> T sessionScope(String name) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (T) session.getAttribute(name);
    }

    /**
     * Sets the session attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    public void sessionScope(String name, Object value) {
        request.getSession().setAttribute(name, value);
    }

    /**
     * Returns the servlet context attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the attribute value
     */
    @SuppressWarnings("unchecked")
    public <T> T applicationScope(String name) {
        return (T) servletContext.getAttribute(name);
    }

    /**
     * Sets the servlet context attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    public void applicationScope(String name, Object value) {
        servletContext.setAttribute(name, value);
    }
}
