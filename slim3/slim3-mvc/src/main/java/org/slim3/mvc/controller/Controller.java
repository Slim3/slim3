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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slim3.commons.util.BooleanUtil;
import org.slim3.commons.util.ByteUtil;
import org.slim3.commons.util.DateUtil;
import org.slim3.commons.util.DoubleUtil;
import org.slim3.commons.util.FloatUtil;
import org.slim3.commons.util.IntegerUtil;
import org.slim3.commons.util.LongUtil;
import org.slim3.commons.util.ShortUtil;
import org.slim3.commons.util.StringUtil;
import org.slim3.mvc.MvcConstants;

/**
 * A base controller. This controller is created each request.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class Controller {

    /**
     * The servlet context.
     */
    protected ServletContext servletContext;

    /**
     * The request.
     */
    protected HttpServletRequest request;

    /**
     * The response.
     */
    protected HttpServletResponse response;

    /**
     * The path of this controller.
     */
    protected String path;

    /**
     * The path of this application.
     */
    protected String applicationPath;

    /**
     * The parsed request parameters.
     */
    protected Map<String, Object> parameters;

    /**
     * Returns the servlet context.
     * 
     * @return the servlet context
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Sets the servlet context.
     * 
     * @param servletContext
     *            the servlet context
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Returns the request.
     * 
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Sets the request.
     * 
     * @param request
     *            the request
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Returns the response.
     * 
     * @return the response
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * Sets the response.
     * 
     * @param response
     *            the response
     */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Returns the path of this controller.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path of this controller.
     * 
     * @param path
     *            the path
     * @throws NullPointerException
     *             if the path parameter is null
     */
    public void setPath(String path) throws NullPointerException {
        if (path == null) {
            throw new NullPointerException("The path parameter is null");
        }
        this.path = path;
        int pos = path.lastIndexOf('/');
        if (pos < 0) {
            throw new IllegalArgumentException("The path("
                + path
                + ") does not contain \"/\"");
        }
        applicationPath = path.substring(0, pos + 1);
    }

    /**
     * Returns the application path.
     * 
     * @return the application path
     */
    public String getApplicationPath() {
        return applicationPath;
    }

    /**
     * Returns the parsed request parameters.
     * 
     * @return the parsed request parameters
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * Sets the parsed request parameters.
     * 
     * @param parameters
     *            the parsed request parameters
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * Executes the action for request.
     * 
     * @return path to go.
     */
    public abstract Navigation execute();

    /**
     * Returns the locale.
     * 
     * @return the locale
     */
    public Locale getLocale() {
        Locale locale = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            locale = (Locale) session.getAttribute(MvcConstants.LOCALE_KEY);
            if (locale != null) {
                return locale;
            }
        }
        locale = request.getLocale();
        if (locale != null) {
            return locale;
        }
        return Locale.getDefault();
    }

    /**
     * Creates a new {@link Navigation} for "forward".
     * 
     * @param path
     *            the controller-relative path
     * @return a new {@link Navigation}
     */
    protected Navigation forward(String path) {
        return new Navigation(path, false);
    }

    /**
     * Creates a default {@link Navigation} for "forward".
     * 
     * @return a default {@link Navigation} for "forward".
     */
    protected Navigation forward() {
        return forward(calculateDefaultPath());
    }

    /**
     * Creates a new {@link Navigation} for "redirect".
     * 
     * @param path
     *            the controller-relative path
     * @return a new {@link Navigation}
     */
    protected Navigation redirect(String path) {
        return new Navigation(path, true);
    }

    /**
     * Calculates the default path when the path is not specified.
     * 
     * @return the default path
     * @throws IllegalStateException
     *             if this controller class name does not end with "Controller".
     */
    protected String calculateDefaultPath() throws IllegalStateException {
        String path = getClass().getSimpleName();
        if (!path.endsWith(MvcConstants.CONTROLLER_SUFFIX)) {
            throw new IllegalStateException("The controller class name("
                + getClass().getName()
                + ") does not end with \""
                + MvcConstants.CONTROLLER_SUFFIX
                + "\".");
        }
        path =
            path.substring(0, path.length()
                - MvcConstants.CONTROLLER_SUFFIX.length());
        return StringUtil.decapitalize(path) + MvcConstants.JSP_EXTENSION;
    }

    /**
     * Converts the object to the boolean object.
     * 
     * @param o
     *            the object
     * @return the boolean object
     */
    protected Boolean toBoolean(Object o) {
        return BooleanUtil.toBoolean(o);
    }

    /**
     * Converts the object to the byte object.
     * 
     * @param o
     *            the object
     * @return the byte object
     */
    protected Byte toByte(Object o) {
        return ByteUtil.toByte(o);
    }

    /**
     * Converts the object to the short object.
     * 
     * @param o
     *            the object
     * @return the short object
     */
    protected Short toShort(Object o) {
        return ShortUtil.toShort(o);
    }

    /**
     * Converts the object to the integer object.
     * 
     * @param o
     *            the object
     * @return the integer object
     */
    protected Integer toInteger(Object o) {
        return IntegerUtil.toInteger(o);
    }

    /**
     * Converts the object to the long object.
     * 
     * @param o
     *            the object
     * @return the long object
     */
    protected Long toLong(Object o) {
        return LongUtil.toLong(o);
    }

    /**
     * Converts the object to the float object.
     * 
     * @param o
     *            the object
     * @return the float object
     */
    protected Float toFloat(Object o) {
        return FloatUtil.toFloat(o);
    }

    /**
     * Converts the object to the double object.
     * 
     * @param o
     *            the object
     * @return the double object
     */
    protected Double toDouble(Object o) {
        return DoubleUtil.toDouble(o);
    }

    /**
     * Converts the object to the date object.
     * 
     * @param o
     *            the object
     * @return the date object
     */
    protected Date toDate(Object o) {
        return DateUtil.toDate(o);
    }

    /**
     * Converts the object to the date part of the date.
     * 
     * @param o
     *            the object
     * @return the date part of the date
     */
    protected Date toDateAndClearTimePart(Object o) {
        return DateUtil.toDateAndClearTimePart(o);
    }

    /**
     * Converts the object to the date time of the date.
     * 
     * @param o
     *            the object
     * @return the time part of the date
     */
    protected Date toDateAndClearDatePart(Object o) {
        return DateUtil.toDateAndClearDatePart(o);
    }

    /**
     * Converts the object to the date object.
     * 
     * @param text
     *            the text
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the date object
     */
    protected Date toDate(String text, String pattern) {
        return DateUtil.toDate(text, pattern);
    }

    /**
     * Returns the request attribute.
     * 
     * @param <T>
     *            the type
     * @param name
     *            the name
     * @return the request attribute
     */
    @SuppressWarnings("unchecked")
    protected <T> T getAttribute(String name) {
        return (T) request.getAttribute(name);
    }

    /**
     * Sets the request attribute.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    protected void setAttribute(String name, Object value) {
        request.setAttribute(name, value);
    }

    /**
     * Removes the attribute from the request.
     * 
     * @param <T>
     *            the attribute type
     * @param name
     *            the attribute name
     * @return the attribute value
     */
    @SuppressWarnings("unchecked")
    protected <T> T removeAttribute(String name) {
        Object value = getAttribute(name);
        request.removeAttribute(name);
        return (T) value;
    }

    /**
     * Returns the session attribute.
     * 
     * @param <T>
     *            the type
     * @param name
     *            the name
     * @return the session attribute
     */
    @SuppressWarnings("unchecked")
    protected <T> T getSessionAttribute(String name) {
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
     *            the name
     * @param value
     *            the value
     */
    protected void setSessionAttribute(String name, Object value) {
        HttpSession session = request.getSession();
        session.setAttribute(name, value);
    }

    /**
     * Removes the attribute from the session.
     * 
     * @param <T>
     *            the attribute type
     * @param name
     *            the attribute name
     * @return the attribute value
     */
    @SuppressWarnings("unchecked")
    protected <T> T removeSessionAttribute(String name) {
        Object value = getSessionAttribute(name);
        request.getSession().removeAttribute(name);
        return (T) value;
    }

    /**
     * Returns the servlet context attribute.
     * 
     * @param <T>
     *            the type
     * @param name
     *            the name
     * @return the servlet context attribute
     */
    @SuppressWarnings("unchecked")
    protected <T> T getServletContextAttribute(String name) {
        return (T) servletContext.getAttribute(name);
    }

    /**
     * Sets the servlet context attribute.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    protected void setServletContextAttribute(String name, Object value) {
        servletContext.setAttribute(name, value);
    }

    /**
     * Removes the attribute from the servlet context.
     * 
     * @param <T>
     *            the attribute type
     * @param name
     *            the attribute name
     * @return the attribute value
     */
    @SuppressWarnings("unchecked")
    protected <T> T removeServletContextAttribute(String name) {
        Object value = getServletContextAttribute(name);
        servletContext.removeAttribute(name);
        return (T) value;
    }

    /**
     * Returns the parsed request parameter as string.
     * 
     * @param name
     *            the name
     * @return the parsed request parameter
     */
    protected String getParameter(String name) {
        String[] value = getStringArrayParameter(name);
        if (value == null || value.length == 0) {
            return null;
        }
        return value[0];
    }

    /**
     * Returns the parsed request parameter as string array.
     * 
     * @param name
     *            the name
     * @return the parsed request parameter
     * @throws IllegalStateException
     *             if the class of the request parameter is not sring array
     */
    protected String[] getStringArrayParameter(String name)
            throws IllegalStateException {
        Object value = parameters.get(name);
        if (value == null) {
            return null;
        }
        if (value.getClass() == String[].class) {
            return (String[]) value;
        }
        throw new IllegalStateException("The class("
            + value.getClass().getName()
            + ") of the request parameter("
            + name
            + ") is not string array.");
    }

    /**
     * Returns the uploaded request parameter as byte array.
     * 
     * @param name
     *            the name
     * @return the uploaded request parameter
     */
    protected byte[] getByteArrayParameter(String name) {
        Object value = parameters.get(name);
        if (value == null) {
            return null;
        }
        if (value.getClass() == byte[].class) {
            return (byte[]) value;
        }
        throw new IllegalStateException("The class("
            + value.getClass().getName()
            + ") of the request parameter("
            + name
            + ") is not byte array.");
    }

    /**
     * Sets the locale to the session
     * 
     * @param locale
     *            the locale
     */
    protected void setLocale(Locale locale) {
        request.getSession().setAttribute(MvcConstants.LOCALE_KEY, locale);
    }
}