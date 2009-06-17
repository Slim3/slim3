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

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.slim3.controller.Controller;
import org.slim3.controller.ControllerConstants;
import org.slim3.controller.FrontController;
import org.slim3.controller.validator.Errors;
import org.slim3.util.BigDecimalUtil;
import org.slim3.util.BooleanUtil;
import org.slim3.util.ByteUtil;
import org.slim3.util.DateUtil;
import org.slim3.util.DoubleUtil;
import org.slim3.util.FloatUtil;
import org.slim3.util.IntegerUtil;
import org.slim3.util.LongUtil;
import org.slim3.util.NumberUtil;
import org.slim3.util.RequestLocator;
import org.slim3.util.ResponseLocator;
import org.slim3.util.ShortUtil;
import org.slim3.util.StringUtil;

/**
 * A test case for Slim3 Controller.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class ControllerTestCase extends TestCase {

    /**
     * The mock for {@link ServletContext}.
     */
    protected MockServletContext application;

    /**
     * The mock for {@link ServletConfig}.
     */
    protected MockServletConfig config;

    /**
     * The mock for {@link FilterConfig}.
     */
    protected MockFilterConfig filterConfig;

    /**
     * The front controller.
     */
    protected FrontController frontController;

    /**
     * The mock for {@link HttpServletRequest}.
     */
    protected MockHttpServletRequest request;

    /**
     * The mock for {@link HttpServletResponse}.
     */
    protected MockHttpServletResponse response;

    /**
     * A mock for {@link FilterChain}.
     */
    protected MockFilterChain filterChain;

    /**
     * Whether "start" method was called.
     */
    protected boolean startCalled = false;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setUpControllerPackage();
        application = new MockServletContext();
        config = new MockServletConfig(application);
        filterConfig = new MockFilterConfig(application);
        frontController = new FrontController();
        frontController.init(filterConfig);
        request = new MockHttpServletRequest(application);
        response = new MockHttpServletResponse();
        RequestLocator.set(request);
        ResponseLocator.set(response);
        filterChain = new MockFilterChain();
    }

    /**
     * Sets up the controller package automatically.
     */
    protected void setUpControllerPackage() {
        if (System.getProperty(ControllerConstants.CONTROLLER_PACKAGE_KEY) != null) {
            return;
        }
        String className = getClass().getName();
        int pos = className.indexOf(".controller.");
        if (pos < 0) {
            return;
        }
        String packageName = className.substring(0, pos + 11);
        System.setProperty(
            ControllerConstants.CONTROLLER_PACKAGE_KEY,
            packageName);
    }

    @Override
    protected void tearDown() throws Exception {
        application = null;
        config = null;
        filterConfig = null;
        frontController.destroy();
        frontController = null;
        request = null;
        response = null;
        RequestLocator.set(null);
        ResponseLocator.set(null);
        filterChain = null;
        tearDownControllerPackage();
        super.tearDown();
    }

    /**
     * Tears down the controller package.
     */
    protected void tearDownControllerPackage() {
        System.clearProperty(ControllerConstants.CONTROLLER_PACKAGE_KEY);
    }

    /**
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     */
    protected String param(String name) {
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
    protected void param(String name, String value) {
        request.setParameter(name, value);
    }

    /**
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     */
    protected String[] paramValues(String name) {
        return request.getParameterValues(name);
    }

    /**
     * Returns the request attribute value as byte.
     * 
     * @param name
     *            the attribute name
     * @return the byte attribute value
     */
    protected Byte asByte(String name) {
        return ByteUtil.toByte(request.getAttribute(name));
    }

    /**
     * Returns the request attribute value as byte.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the byte attribute value
     */
    protected Byte asByte(String name, String pattern) {
        return ByteUtil.toByte(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
     * Returns the request attribute value as short.
     * 
     * @param name
     *            the attribute name
     * @return the short attribute value
     */
    protected Short asShort(String name) {
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
    protected Short asShort(String name, String pattern) {
        return ShortUtil.toShort(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
     * Returns the request attribute value as integer.
     * 
     * @param name
     *            the attribute name
     * @return the integer attribute value
     */
    protected Integer asInteger(String name) {
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
    protected Integer asInteger(String name, String pattern) {
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
    protected Long asLong(String name) {
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
    protected Long asLong(String name, String pattern) {
        return LongUtil.toLong(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
     * Returns the request attribute value as float.
     * 
     * @param name
     *            the attribute name
     * @return the float attribute value
     */
    protected Float asFloat(String name) {
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
    protected Float asFloat(String name, String pattern) {
        return FloatUtil.toFloat(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
     * Returns the request attribute value as double.
     * 
     * @param name
     *            the attribute name
     * @return the double attribute value
     */
    protected Double asDouble(String name) {
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
    protected Double asDouble(String name, String pattern) {
        return DoubleUtil
            .toDouble(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
     * Returns the request attribute value as big decimal.
     * 
     * @param name
     *            the attribute name
     * @return the big decimal attribute value
     */
    protected BigDecimal asBigDecimal(String name) {
        return BigDecimalUtil.toBigDecimal(request.getAttribute(name));
    }

    /**
     * Returns the request attribute value as big decimal.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the big decimal attribute value
     */
    protected BigDecimal asBigDecimal(String name, String pattern) {
        return BigDecimalUtil.toBigDecimal(NumberUtil.toNumber(
            asString(name),
            pattern));
    }

    /**
     * Returns the request attribute value as string.
     * 
     * @param name
     *            the attribute name
     * @return the string attribute value
     */
    protected String asString(String name) {
        return StringUtil.toString(request.getAttribute(name));
    }

    /**
     * Returns the request attribute value as boolean.
     * 
     * @param name
     *            the attribute name
     * @return the boolean attribute value
     */
    protected Boolean asBoolean(String name) {
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
    protected Date asDate(String name, String pattern) {
        return DateUtil.toDate(asString(name), pattern);
    }

    /**
     * Sets the request parameter.
     * 
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     */
    protected void paramValues(String name, String[] value) {
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
    protected <T> T requestScope(String name) {
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
    protected void requestScope(String name, Object value) {
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
    protected <T> T sessionScope(String name) {
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
    protected void sessionScope(String name, Object value) {
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
    protected <T> T applicationScope(String name) {
        return (T) application.getAttribute(name);
    }

    /**
     * Sets the servlet context attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    protected void applicationScope(String name, Object value) {
        application.setAttribute(name, value);
    }

    /**
     * Starts the request process.
     * 
     * @param path
     *            the request path
     * @throws NullPointerException
     *             if the path parameter is null
     * @throws IllegalArgumentException
     *             if the path does not start with "/"
     * @throws IOException
     *             if {@link IOException} occurred
     * @throws ServletException
     *             if {@link ServletException} occurred
     */
    protected void start(String path) throws NullPointerException,
            IllegalArgumentException, IOException, ServletException {
        if (path == null) {
            throw new NullPointerException("The path parameter is null.");
        }
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("The path("
                + path
                + ") must start with \"/\".");
        }
        setUpStart();
        try {
            request.setServletPath(path);
            frontController.doFilter(request, response, filterChain);
            startCalled = true;
        } finally {
            tearDownStart();
        }
    }

    /**
     * Sets up the start process.
     */
    protected void setUpStart() {
    }

    /**
     * Tears down the start process.
     */
    protected void tearDownStart() {
    }

    /**
     * Determines if the test result is "redirect".
     * 
     * @return whether the test result is "redirect"
     */
    protected boolean isRedirect() {
        return response.getRedirectPath() != null;
    }

    /**
     * Returns the next path.
     * 
     * @return the next path
     */
    protected String getNextPath() {
        assertStartWasCalled();
        MockRequestDispatcher dispatcher =
            application.getLatestRequestDispatcher();
        if (dispatcher != null) {
            return dispatcher.getPath();
        }
        if (response.getRedirectPath() != null) {
            return response.getRedirectPath();
        }
        return filterChain.getPath();
    }

    /**
     * Returns the controller.
     * 
     * @param <T>
     *            the controller type
     * @return the controller
     */
    @SuppressWarnings("unchecked")
    protected <T extends Controller> T getController() {
        assertStartWasCalled();
        return (T) request.getAttribute(ControllerConstants.CONTROLLER_KEY);
    }

    /**
     * Returns the error messages.
     * 
     * @return the error messages
     */
    protected Errors getErrors() {
        return requestScope(ControllerConstants.ERRORS_KEY);
    }

    /**
     * Asserts that "start" method was called.
     */
    protected void assertStartWasCalled() {
        if (!startCalled) {
            throw new IllegalStateException(
                "Call ControllerTester#start() before getting the test results.");
        }
    }
}