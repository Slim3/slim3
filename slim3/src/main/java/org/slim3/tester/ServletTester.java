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
package org.slim3.tester;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
 * @since 1.0.0
 * 
 */
public class ServletTester extends AppEngineTester {

    /**
     * The key of blob keys.
     */
    protected static final String BLOBKEYS_KEY =
        "com.google.appengine.api.blobstore.upload.blobkeys";

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

    /**
     * The previous {@link ServletContext}.
     */
    protected ServletContext previousServletContext;

    /**
     * The previous {@link HttpServletRequest}.
     */
    protected HttpServletRequest previousRequest;

    /**
     * The previous {@link HttpServletResponse}.
     */
    protected HttpServletResponse previousResponse;

    /**
     * The previous {@link Locale}.
     */
    protected Locale previousLocale;

    /**
     * The previous {@link TimeZone}.
     */
    protected TimeZone previousTimeZone;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        previousServletContext = ServletContextLocator.get();
        previousRequest = RequestLocator.get();
        previousResponse = ResponseLocator.get();
        previousLocale = LocaleLocator.get();
        previousTimeZone = TimeZoneLocator.get();
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
        ServletContextLocator.set(previousServletContext);
        RequestLocator.set(previousRequest);
        ResponseLocator.set(previousResponse);
        LocaleLocator.set(previousLocale);
        TimeZoneLocator.set(previousTimeZone);
        super.tearDown();
    }

    /**
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public String param(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return request.getParameter(name.toString());
    }

    /**
     * Sets the request parameter.
     * 
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public void param(CharSequence name, Object value)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        request.setParameter(name.toString(), StringUtil.toString(value));
    }

    /**
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public String[] paramValues(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return request.getParameterValues(name.toString());
    }

    /**
     * Returns the request attribute value as short.
     * 
     * @param name
     *            the attribute name
     * @return the short attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Short asShort(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return ShortUtil.toShort(request.getAttribute(name.toString()));
    }

    /**
     * Returns the request attribute value as short.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the short attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Short asShort(CharSequence name, String pattern)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return ShortUtil.toShort(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
     * Returns the request attribute value as integer.
     * 
     * @param name
     *            the attribute name
     * @return the integer attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Integer asInteger(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return IntegerUtil.toInteger(request.getAttribute(name.toString()));
    }

    /**
     * Returns the request attribute value as integer.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the integer attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Integer asInteger(CharSequence name, String pattern)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
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
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Long asLong(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return LongUtil.toLong(request.getAttribute(name.toString()));
    }

    /**
     * Returns the request attribute value as long.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the long attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Long asLong(CharSequence name, String pattern)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return LongUtil.toLong(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
     * Returns the request attribute value as float.
     * 
     * @param name
     *            the attribute name
     * @return the float attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Float asFloat(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return FloatUtil.toFloat(request.getAttribute(name.toString()));
    }

    /**
     * Returns the request attribute value as float.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the float attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Float asFloat(CharSequence name, String pattern)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return FloatUtil.toFloat(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
     * Returns the request attribute value as double.
     * 
     * @param name
     *            the attribute name
     * @return the double attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Double asDouble(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return DoubleUtil.toDouble(request.getAttribute(name.toString()));
    }

    /**
     * Returns the request attribute value as double.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the double attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Double asDouble(CharSequence name, String pattern)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return DoubleUtil
            .toDouble(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
     * Returns the request attribute value as string.
     * 
     * @param name
     *            the attribute name
     * @return the string attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public String asString(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return StringUtil.toString(request.getAttribute(name.toString()));
    }

    /**
     * Returns the request attribute value as boolean.
     * 
     * @param name
     *            the attribute name
     * @return the boolean attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Boolean asBoolean(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return BooleanUtil.toBoolean(request.getAttribute(name.toString()));
    }

    /**
     * Returns the request attribute value as date.
     * 
     * @param name
     *            the attribute name
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the date attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Date asDate(CharSequence name, String pattern)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return DateUtil.toDate(asString(name), pattern);
    }

    /**
     * Returns the request attribute value as {@link Key}.
     * 
     * @param name
     *            the attribute name
     * @return the request attribute value as {@link Key}
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Key asKey(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        Object key = request.getAttribute(name.toString());
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
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public void paramValues(CharSequence name, String[] value)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        request.setParameter(name.toString(), value);
    }

    /**
     * Returns the request attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the request attribute
     * @throws NullPointerException
     *             if the name parameter is null
     */
    @SuppressWarnings("unchecked")
    public <T> T requestScope(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return (T) request.getAttribute(name.toString());
    }

    /**
     * Sets the request attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public void requestScope(CharSequence name, Object value)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        request.setAttribute(name.toString(), value);
    }

    /**
     * Returns the session attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    @SuppressWarnings("unchecked")
    public <T> T sessionScope(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (T) session.getAttribute(name.toString());
    }

    /**
     * Sets the session attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public void sessionScope(CharSequence name, Object value)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        request.getSession().setAttribute(name.toString(), value);
    }

    /**
     * Returns the servlet context attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    @SuppressWarnings("unchecked")
    public <T> T applicationScope(CharSequence name)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return (T) servletContext.getAttribute(name.toString());
    }

    /**
     * Sets the servlet context attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public void applicationScope(CharSequence name, Object value)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        servletContext.setAttribute(name.toString(), value);
    }

    /**
     * Determines if the test result is "redirect".
     * 
     * @return whether the test result is "redirect"
     */
    public boolean isRedirect() {
        return response.getRedirectPath() != null;
    }

    /**
     * Returns the destination path.
     * 
     * @return the destination path
     */
    public String getDestinationPath() {
        MockRequestDispatcher dispatcher =
            servletContext.getLatestRequestDispatcher();
        if (dispatcher != null) {
            return dispatcher.getPath();
        }
        if (response.getRedirectPath() != null) {
            return response.getRedirectPath();
        }
        return null;
    }

    /**
     * Adds a blob key.
     * 
     * @param name
     *            the blob key name
     * @param value
     *            the blob key value
     * @throws NullPointerException
     *             if the name parameter is null or if the value parameter is
     *             null
     */
    public void addBlobKey(String name, String value)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        if (value == null) {
            throw new NullPointerException(
                "The value parameter must not be null.");
        }
        Map<String, String> blobKeys = requestScope(BLOBKEYS_KEY);
        if (blobKeys == null) {
            blobKeys = new HashMap<String, String>();
            requestScope(BLOBKEYS_KEY, blobKeys);
        }
        blobKeys.put(name, value);
    }
}
