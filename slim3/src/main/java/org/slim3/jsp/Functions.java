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
package org.slim3.jsp;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.slim3.controller.ControllerConstants;
import org.slim3.util.HtmlUtil;
import org.slim3.util.LocaleLocator;
import org.slim3.util.RequestLocator;
import org.slim3.util.ResponseLocator;
import org.slim3.util.StringUtil;
import org.slim3.util.TimeZoneLocator;

/**
 * JSP functions of Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class Functions {

    private static String BR = "<br />";

    /**
     * Escapes string that could be interpreted as HTML.
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
        if (input.getClass().isArray()) {
            Class<?> clazz = input.getClass().getComponentType();
            if (clazz.isPrimitive()) {
                if (clazz == boolean.class) {
                    return Arrays.toString((boolean[]) input);
                } else if (clazz == int.class) {
                    return Arrays.toString((int[]) input);
                } else if (clazz == long.class) {
                    return Arrays.toString((long[]) input);
                } else if (clazz == byte.class) {
                    return Arrays.toString((byte[]) input);
                } else if (clazz == short.class) {
                    return Arrays.toString((short[]) input);
                } else if (clazz == float.class) {
                    return Arrays.toString((float[]) input);
                } else if (clazz == double.class) {
                    return Arrays.toString((double[]) input);
                } else if (clazz == char.class) {
                    return Arrays.toString((char[]) input);
                }
            } else {
                return Arrays.toString((Object[]) input);
            }
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
        HttpServletRequest request = RequestLocator.get();
        String contextPath = request.getContextPath();
        StringBuilder sb = new StringBuilder(50);
        if (contextPath.length() > 1) {
            sb.append(contextPath);
        }
        String path = request.getServletPath();
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
        HttpServletRequest request = RequestLocator.get();
        Map<String, String> errors =
            (Map<String, String>) request
                .getAttribute(ControllerConstants.ERRORS_KEY);
        if (errors != null && errors.containsKey(name)) {
            return styleClass;
        }
        return "";
    }

    /**
     * Returns name and value attribute representation.
     * 
     * @param name
     *            the property name
     * @return name and value attribute representation
     */
    public static String property(String name) {
        HttpServletRequest request = RequestLocator.get();
        return "name = \""
            + name
            + "\" value = \""
            + h(request.getAttribute(name))
            + "\"";
    }

    private Functions() {
    }
}