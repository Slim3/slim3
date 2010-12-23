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
package org.slim3.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slim3.controller.upload.FileUpload;
import org.slim3.controller.validator.Errors;
import org.slim3.util.AppEngineUtil;
import org.slim3.util.BooleanUtil;
import org.slim3.util.DateUtil;
import org.slim3.util.DoubleUtil;
import org.slim3.util.FloatUtil;
import org.slim3.util.IntegerUtil;
import org.slim3.util.LongUtil;
import org.slim3.util.NumberUtil;
import org.slim3.util.ShortUtil;
import org.slim3.util.StringUtil;
import org.slim3.util.ThrowableUtil;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * A base controller. This controller is created each request.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public abstract class Controller {

    /**
     * The buffer size.
     */
    protected static final int BUFFER_SIZE = 1024;

    private static final Logger logger =
        Logger.getLogger(Controller.class.getName());

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
     * The base path.
     */
    protected String basePath;

    /**
     * The error messages.
     */
    protected Errors errors;

    /**
     * Runs the bare controller process.
     * 
     * @return the navigation
     * @throws Throwable
     *             if an {@link Throwable} occurred
     */
    public Navigation runBare() throws Throwable {
        Navigation navigation = null;
        Throwable error = null;
        navigation = setUp();
        if (navigation != null) {
            return navigation;
        }
        try {
            navigation = run();
        } catch (Throwable t) {
            error = t;
        } finally {
            try {
                tearDown();
            } catch (Throwable t) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, t.getMessage(), t);
                }
            }
        }
        if (error != null) {
            navigation = handleError(error);
        }
        return navigation;
    }

    /**
     * Sets up the this controller. This method is called before "run" method is
     * called.
     * 
     * @return the navigation
     */
    protected Navigation setUp() {
        return null;
    }

    /**
     * Override to run this controller
     * 
     * @return the navigation
     * @throws Exception
     *             if an {@link Exception} occurred
     */
    protected abstract Navigation run() throws Exception;

    /**
     * Tears down this controller. This method is called after "run" method is
     * called.
     * 
     */
    protected void tearDown() {
    }

    /**
     * Handles the error.
     * 
     * @param error
     *            the error
     * @return the navigation.
     * @throws Throwable
     *             if an {@link Throwable} occurred
     */
    protected Navigation handleError(Throwable error) throws Throwable {
        throw error;
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
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    protected String param(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return request.getParameter(name.toString());
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
    protected String[] paramValues(CharSequence name)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return request.getParameterValues(name.toString());
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
    protected <T> T requestScope(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return (T) request.getAttribute(name.toString());
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
    protected String asString(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        return StringUtil.toString(request.getAttribute(name.toString()));
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
    protected Short asShort(CharSequence name) throws NullPointerException {
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
     *             if the name parameter is null or if the pattern parameter is
     *             null
     */
    protected Short asShort(CharSequence name, String pattern)
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
    protected Integer asInteger(CharSequence name) throws NullPointerException {
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
     *             if the name parameter is null or if the pattern parameter is
     *             null
     */
    protected Integer asInteger(CharSequence name, String pattern)
            throws NullPointerException {
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
    protected Long asLong(CharSequence name) throws NullPointerException {
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
     *             if the name parameter is null or if the pattern parameter is
     *             null
     */
    protected Long asLong(CharSequence name, String pattern)
            throws NullPointerException {
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
    protected Float asFloat(CharSequence name) throws NullPointerException {
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
     *             if the name parameter is null or if the pattern parameter is
     *             null
     */
    protected Float asFloat(CharSequence name, String pattern)
            throws NullPointerException {
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
    protected Double asDouble(CharSequence name) throws NullPointerException {
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
     *             if the name parameter is null or if the pattern parameter is
     *             null
     */
    protected Double asDouble(CharSequence name, String pattern)
            throws NullPointerException {
        return DoubleUtil
            .toDouble(NumberUtil.toNumber(asString(name), pattern));
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
    protected Boolean asBoolean(CharSequence name) throws NullPointerException {
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
     *             if the name parameter is null or if the pattern parameter is
     *             null
     */
    protected Date asDate(CharSequence name, String pattern)
            throws NullPointerException {
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
    protected Key asKey(CharSequence name) throws NullPointerException {
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
     * Sets the request attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    protected void requestScope(CharSequence name, Object value)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        request.setAttribute(name.toString(), value);
    }

    /**
     * Removes the request attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the removed value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    @SuppressWarnings("unchecked")
    protected <T> T removeRequestScope(CharSequence name)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        T value = (T) request.getAttribute(name.toString());
        request.removeAttribute(name.toString());
        return value;
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
    protected <T> T sessionScope(CharSequence name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        String n = name.toString();
        T value = (T) session.getAttribute(n);
        if (value != null) {
            session.setAttribute(n, value);
        }
        return value;
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
    protected void sessionScope(CharSequence name, Object value)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        request.getSession().setAttribute(name.toString(), value);
    }

    /**
     * Removes the session attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the removed value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    @SuppressWarnings("unchecked")
    protected <T> T removeSessionScope(CharSequence name)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        T value = (T) session.getAttribute(name.toString());
        session.removeAttribute(name.toString());
        return value;
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
    protected <T> T applicationScope(CharSequence name)
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
    protected void applicationScope(CharSequence name, Object value)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        servletContext.setAttribute(name.toString(), value);
    }

    /**
     * Removes the servlet context attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the removed value
     * @throws NullPointerException
     *             if the name parameter is null
     */
    @SuppressWarnings("unchecked")
    protected <T> T removeApplicationScope(CharSequence name)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        T value = (T) servletContext.getAttribute(name.toString());
        servletContext.removeAttribute(name.toString());
        return value;
    }

    /**
     * Determines if this application is running on development server.
     * 
     * @return whether this application is running on development server
     */
    protected boolean isDevelopment() {
        return AppEngineUtil.isDevelopment();
    }

    /**
     * Downloads the data.
     * 
     * @param fileName
     *            the file name
     * @param data
     *            the data
     * @throws NullPointerException
     *             if the fileName parameter is null or if the data parameter is
     *             null
     */
    protected void download(String fileName, byte[] data)
            throws NullPointerException {
        if (fileName == null) {
            throw new NullPointerException(
                "The fileName parameter must not be null.");
        }
        if (data == null) {
            throw new NullPointerException(
                "The data parameter must not be null.");
        }
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; "
                + encodeFileName(fileName));
            OutputStream out =
                new BufferedOutputStream(response.getOutputStream());
            try {
                out.write(data);
            } finally {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            ThrowableUtil.wrapAndThrow(e);
        }
    }

    /**
     * Downloads the input stream data.
     * 
     * @param fileName
     *            the file name
     * @param in
     *            the input stream
     * @throws NullPointerException
     *             if the fileName parameter is null or if the in parameter is
     *             null
     */
    protected void download(String fileName, InputStream in) {
        if (fileName == null) {
            throw new NullPointerException(
                "The fileName parameter must not be null.");
        }
        if (in == null) {
            throw new NullPointerException("The in parameter must not be null.");
        }
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; "
                + encodeFileName(fileName));
            OutputStream out =
                new BufferedOutputStream(response.getOutputStream());
            try {
                byte[] buf = new byte[BUFFER_SIZE];
                int length;
                while ((length = in.read(buf)) > 0) {
                    out.write(buf, 0, length);
                }
            } finally {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            ThrowableUtil.wrapAndThrow(e);
        }
    }

    /**
     * Shows the data.
     * 
     * @param fileName
     *            the file name
     * @param data
     *            the data
     * @throws NullPointerException
     *             if the fileName parameter is null or if the data parameter is
     *             null
     */
    protected void show(String fileName, byte[] data)
            throws NullPointerException {
        if (fileName == null) {
            throw new NullPointerException(
                "The fileName parameter must not be null.");
        }
        if (data == null) {
            throw new NullPointerException(
                "The data parameter must not be null.");
        }
        try {
            String contentType =
                URLConnection.getFileNameMap().getContentTypeFor(fileName);
            if (contentType != null) {
                response.setContentType(contentType);
            }
            response.setHeader("Content-disposition", "inline; "
                + encodeFileName(fileName));
            OutputStream out =
                new BufferedOutputStream(response.getOutputStream());
            try {
                out.write(data);
            } finally {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            ThrowableUtil.wrapAndThrow(e);
        }
    }

    /**
     * Shoss the input stream data.
     * 
     * @param fileName
     *            the file name
     * @param in
     *            the input stream
     * @throws NullPointerException
     *             if the fileName parameter is null or if the in parameter is
     *             null
     */
    protected void show(String fileName, InputStream in) {
        if (fileName == null) {
            throw new NullPointerException(
                "The fileName parameter must not be null.");
        }
        if (in == null) {
            throw new NullPointerException("The in parameter must not be null.");
        }
        try {
            String contentType =
                URLConnection.getFileNameMap().getContentTypeFor(fileName);
            if (contentType != null) {
                response.setContentType(contentType);
            }
            response.setHeader("Content-disposition", "inline; "
                + encodeFileName(fileName));
            OutputStream out =
                new BufferedOutputStream(response.getOutputStream());
            try {
                byte[] buf = new byte[BUFFER_SIZE];
                int length;
                while ((length = in.read(buf)) > 0) {
                    out.write(buf, 0, length);
                }
            } finally {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            ThrowableUtil.wrapAndThrow(e);
        }
    }

    /**
     * Encodes the string as "application/x-www-form-urlencoded".
     * 
     * @param str
     *            the string
     * @return encoded string
     * @throws IOException
     *             if {@link IOException} occurred
     */
    protected String encodeFileName(String str) throws IOException {
        if (str == null) {
            return null;
        }
        String encodedStr = URLEncoder.encode(str, "UTF-8");
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && userAgent.indexOf("Opera") < 0) {
            if (userAgent.indexOf("Firefox") >= 0) {
                return "filename*=utf8'" + encodedStr;
            } else if (userAgent.indexOf("MSIE") >= 0
                || userAgent.indexOf("Chrome") >= 0) {
                return "filename=" + encodedStr;
            }
        }
        String encoding = response.getCharacterEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        return "filename=\""
            + new String(str.getBytes(encoding), "ISO-8859-1")
            + "\"";
    }

    /**
     * Returns the path before forwarding.
     * 
     * @return the path before forwarding
     */
    protected String getForwardServletPath() {
        return asString(ControllerConstants.FORWARD_SERVLET_PATH_KEY);
    }

    /**
     * Determines if this request is get method.
     * 
     * @return whether this request is get method
     */
    protected boolean isGet() {
        return "get".equalsIgnoreCase(request.getMethod());
    }

    /**
     * Determines if this request is post method.
     * 
     * @return whether this request is post method
     */
    protected boolean isPost() {
        return "post".equalsIgnoreCase(request.getMethod());
    }

    /**
     * Determines if this request is put method.
     * 
     * @return whether this request is put method
     */
    protected boolean isPut() {
        return "put".equalsIgnoreCase(request.getMethod());
    }

    /**
     * Determines if this request is delete method.
     * 
     * @return whether this request is delete method
     */
    protected boolean isDelete() {
        return "delete".equalsIgnoreCase(request.getMethod());
    }

    /**
     * Creates a new request handler.
     * 
     * @param request
     *            the request
     * @return a new request handler
     * 
     */
    protected RequestHandler createRequestHandler(HttpServletRequest request) {
        if (FileUpload.isMultipartContent(request)) {
            return new MultipartRequestHandler(request);
        }
        return new RequestHandler(request);
    }
}