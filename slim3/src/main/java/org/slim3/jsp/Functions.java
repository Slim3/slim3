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
package org.slim3.jsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.slim3.controller.ControllerConstants;
import org.slim3.util.BooleanUtil;
import org.slim3.util.HtmlUtil;
import org.slim3.util.LocaleLocator;
import org.slim3.util.RequestLocator;
import org.slim3.util.RequestUtil;
import org.slim3.util.ResponseLocator;
import org.slim3.util.StringUtil;
import org.slim3.util.TimeZoneLocator;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * JSP functions of Slim3.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class Functions {

    private static String BR = "<br />";

    private static String NBSP = "&nbsp;";

    private static String ARRAY_SUFFIX = "Array";

    private static List<String> EMPTY_STRING_LIST = new ArrayList<String>(0);

    /**
     * Encodes the input object. If the object is a string, it is escaped as
     * HTML. If the object is a key, it is encoded as Base64. Anything else is
     * converted to a string using toString() method.
     * 
     * @param input
     *            the input value
     * @return the escaped value
     */
    public static String h(Object input) {
        if (input == null || "".equals(input)) {
            return "";
        }
        if (input.getClass() == String.class) {
            return HtmlUtil.escape(input.toString());
        }
        if (input.getClass() == Key.class) {
            return KeyFactory.keyToString((Key) input);
        }
        return input.toString();
    }

    /**
     * Returns context-relative URL.
     * 
     * @param input
     *            the input value
     * @return context-relative URL
     */
    public static String url(String input) {
        boolean empty = StringUtil.isEmpty(input);
        if (!empty && input.indexOf(':') >= 0) {
            return input;
        }
        HttpServletRequest request = request();
        String contextPath = request.getContextPath();
        StringBuilder sb = new StringBuilder(50);
        if (contextPath.length() > 1) {
            sb.append(contextPath);
        }
        String path =
            (String) request.getAttribute(ControllerConstants.BASE_PATH_KEY);
        if (path == null) {
            path = RequestUtil.getPath(request);
        }
        int pos = path.lastIndexOf('/');
        path = path.substring(0, pos + 1);
        if (empty) {
            sb.append(path);
        } else if (input.startsWith("/")) {
            sb.append(input);
        } else {
            sb.append(path).append(input);
        }
        return ResponseLocator.get().encodeURL(sb.toString());
    }

    /**
     * Returns blobstore URL.
     * 
     * @param url
     *            the part of url
     * @return context-relative URL
     */
    public static String blobstoreUrl(String url) {
        BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
        boolean empty = StringUtil.isEmpty(url);
        if (!empty && url.indexOf(':') >= 0) {
            return bs.createUploadUrl(url);
        }
        HttpServletRequest request = request();
        String path =
            (String) request.getAttribute(ControllerConstants.BASE_PATH_KEY);
        if (path == null) {
            path = RequestUtil.getPath(request);
        }
        int pos = path.lastIndexOf('/');
        path = path.substring(0, pos + 1);
        if (empty) {
            return bs.createUploadUrl(path);
        } else if (url.startsWith("/")) {
            return bs.createUploadUrl(url);
        }
        return bs.createUploadUrl(path + url);
    }

    /**
     * Converts blank to entity reference nbsp.
     * 
     * @param input
     *            the input value
     * @return the converted value
     */
    public static String nbsp(String input) {
        if (StringUtil.isEmpty(input)) {
            return "";
        }
        return input.replaceAll(" ", NBSP);
    }

    /**
     * Converts line break to br tag.
     * 
     * @param input
     *            the input value
     * @return the converted value
     */
    public static String br(String input) {
        if (StringUtil.isEmpty(input)) {
            return "";
        }
        return input.replaceAll("\r\n", BR).replaceAll("\r", BR).replaceAll(
            "\n",
            BR);
    }

    /**
     * Returns the current locale.
     * 
     * @return the current locale.
     */
    public static Locale locale() {
        return LocaleLocator.get();
    }

    /**
     * Returns the current time zone.
     * 
     * @return the current time zone.
     */
    public static TimeZone timeZone() {
        return TimeZoneLocator.get();
    }

    /**
     * Returns the text tag representation.
     * 
     * @param name
     *            the property name
     * @return the text tag representation
     * @throws IllegalArgumentException
     *             if the property name ends with "Array"
     */
    public static String text(String name) throws IllegalArgumentException {
        if (name.endsWith(ARRAY_SUFFIX)) {
            throw new IllegalArgumentException("The property name("
                + name
                + ") must not end with \"Array\".");
        }
        HttpServletRequest request = request();
        return "name=\""
            + name
            + "\" value=\""
            + h(request.getAttribute(name))
            + "\"";
    }

    /**
     * Returns the hidden tag representation.
     * 
     * @param name
     *            the property name
     * @return the hidden tag representation
     * @throws IllegalArgumentException
     *             if the property name ends with "Array"
     */
    public static String hidden(String name) throws IllegalArgumentException {
        return text(name);
    }

    /**
     * Returns the checkbox tag representation.
     * 
     * @param name
     *            the property name
     * @return the checkbox tag representation
     * @throws IllegalArgumentException
     *             if the property name ends with "Array"
     */
    public static String checkbox(String name) throws IllegalArgumentException {
        if (name.endsWith(ARRAY_SUFFIX)) {
            throw new IllegalArgumentException("The checkbox property name("
                + name
                + ") must not end with \"Array\".");
        }
        HttpServletRequest request = request();
        return "name=\""
            + name
            + "\""
            + (BooleanUtil.toPrimitiveBoolean(request.getAttribute(name))
                ? " checked=\"checked\""
                : "");
    }

    /**
     * Returns the multibox tag representation.
     * 
     * @param name
     *            the property name
     * @param value
     *            the value
     * @return the multibox tag representation
     * @throws IllegalArgumentException
     *             if the property name does not end with "Array"
     * @throws IllegalStateException
     *             if the property is not an array or if the property is not a
     *             string array
     */
    public static String multibox(String name, String value)
            throws IllegalArgumentException, IllegalStateException {
        if (!name.endsWith(ARRAY_SUFFIX)) {
            throw new IllegalArgumentException("The multibox property name("
                + name
                + ") must end with \"Array\".");
        }
        HttpServletRequest request = request();
        Object o = request.getAttribute(name);
        List<String> list = null;
        if (o != null) {
            if (!o.getClass().isArray()) {
                throw new IllegalStateException("The multibox property("
                    + name
                    + ") must be an array.");
            }
            if (o.getClass().getComponentType() != String.class) {
                throw new IllegalStateException("The multibox property("
                    + name
                    + ") must be a string array.");
            }
            list = Arrays.asList((String[]) o);
        } else {
            list = EMPTY_STRING_LIST;
        }
        return "name=\""
            + name
            + "\" value=\""
            + h(value)
            + "\""
            + (list.contains(value) ? " checked=\"checked\"" : "");
    }

    /**
     * Returns the radio tag representation.
     * 
     * @param name
     *            the property name
     * @param value
     *            the value
     * @return the radio tag representation
     * @throws IllegalArgumentException
     *             if the property name ends with "Array"
     */
    public static String radio(String name, String value)
            throws IllegalArgumentException {
        if (name.endsWith(ARRAY_SUFFIX)) {
            throw new IllegalArgumentException("The radio property name("
                + name
                + ") must not end with \"Array\".");
        }
        HttpServletRequest request = request();
        String s = StringUtil.toString(request.getAttribute(name));
        return "name=\""
            + name
            + "\" value=\""
            + h(value)
            + "\""
            + (value == null && s == null || value != null && value.equals(s)
                ? " checked=\"checked\""
                : "");
    }

    /**
     * Returns the select option tag representation.
     * 
     * @param name
     *            the property name
     * @param value
     *            the value
     * @return the select option tag representation
     * @throws IllegalArgumentException
     *             if the property name ends with "Array"
     */
    public static String select(String name, String value)
            throws IllegalArgumentException {
        if (name.endsWith(ARRAY_SUFFIX)) {
            throw new IllegalArgumentException("The select property name("
                + name
                + ") must not end with \"Array\".");
        }
        HttpServletRequest request = request();
        String s = StringUtil.toString(request.getAttribute(name));
        return "value=\""
            + h(value)
            + "\""
            + (value == null && s == null || value != null && value.equals(s)
                ? " selected=\"selected\""
                : "");
    }

    /**
     * Returns the multiselect option tag representation.
     * 
     * @param name
     *            the property name
     * @param value
     *            the value
     * @return the multiselect option tag representation
     * @throws IllegalArgumentException
     *             if the property name does not end with "Array"
     * @throws IllegalStateException
     *             if the property is not an array or if the property is not a
     *             string array
     */
    public static String multiselect(String name, String value)
            throws IllegalArgumentException, IllegalStateException {
        if (!name.endsWith(ARRAY_SUFFIX)) {
            throw new IllegalArgumentException("The multiselect property name("
                + name
                + ") must end with \"Array\".");
        }
        HttpServletRequest request = request();
        Object o = request.getAttribute(name);
        List<String> list = null;
        if (o != null) {
            if (!o.getClass().isArray()) {
                throw new IllegalStateException("The multiselect property("
                    + name
                    + ") must be an array.");
            }
            if (o.getClass().getComponentType() != String.class) {
                throw new IllegalStateException("The multiselect property("
                    + name
                    + ") must be a string array.");
            }
            list = Arrays.asList((String[]) o);
        } else {
            list = EMPTY_STRING_LIST;
        }
        return "value=\""
            + h(value)
            + "\""
            + (list.contains(value) ? " selected=\"selected\"" : "");
    }

    /**
     * Returns the error style class
     * 
     * @param name
     *            the name
     * @param styleClass
     *            the error style class
     * @return the error style class
     */
    @SuppressWarnings("unchecked")
    public static String errorClass(String name, String styleClass) {
        HttpServletRequest request = request();
        Map<String, String> errors =
            (Map<String, String>) request
                .getAttribute(ControllerConstants.ERRORS_KEY);
        if (errors != null && errors.containsKey(name)) {
            return styleClass;
        }
        return "";
    }

    /**
     * Returns errors iterator.
     * 
     * @return errors iterator
     */
    @SuppressWarnings("unchecked")
    public static Iterator<String> errors() {
        HttpServletRequest request = request();
        Map<String, String> errors =
            (Map<String, String>) request
                .getAttribute(ControllerConstants.ERRORS_KEY);
        if (errors != null) {
            return errors.values().iterator();
        }
        return null;
    }

    /**
     * Returns a string representation of {@link Key}. Use {@link #h(Object)}.
     * 
     * @param key
     *            the key
     * @return a string representation of {@link Key}
     */
    @Deprecated
    public static String key(Key key) {
        if (key == null) {
            return "";
        }
        return KeyFactory.keyToString(key);
    }

    /**
     * Returns the hidden tag representation. Use {@link #hidden(String)}.
     * 
     * @param name
     *            the property name
     * @return the hidden tag representation
     * @throws IllegalArgumentException
     *             if the property name ends with "Array"
     */
    @Deprecated
    public static String hiddenKey(String name) throws IllegalArgumentException {
        if (name.endsWith(ARRAY_SUFFIX)) {
            throw new IllegalArgumentException("The hidden property name("
                + name
                + ") must not end with \"Array\".");
        }
        HttpServletRequest request = request();
        Object value = request.getAttribute(name);
        String s = "";
        if (value != null) {
            if (value instanceof Key) {
                s = key((Key) value);
            } else {
                s = value.toString();
            }
        }
        return "name=\"" + name + "\" value=\"" + s + "\"";
    }

    /**
     * Returns the current request.
     * 
     * @return the current request
     * @throws IllegalStateException
     *             if the current request does not exists
     */
    protected static HttpServletRequest request() throws IllegalStateException {
        HttpServletRequest request = RequestLocator.get();
        if (request == null) {
            throw new IllegalStateException(
                "JSP should be called via FrontController.");
        }
        return request;
    }

    private Functions() {
    }
}