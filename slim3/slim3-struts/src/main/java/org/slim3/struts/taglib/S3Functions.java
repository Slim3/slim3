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
package org.slim3.struts.taglib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.slim3.commons.exception.WrapRuntimeException;
import org.slim3.commons.util.StringUtil;
import org.slim3.struts.util.ControllerUtil;
import org.slim3.struts.web.WebLocator;

/**
 * JSP functions of Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3Functions {

    private static final int HIGHEST_SPECIAL = '>';

    private static String BR = "<br />";

    private static String NBSP = "&nbsp;";

    private static char[][] specialCharactersRepresentation = new char[HIGHEST_SPECIAL + 1][];

    static {
        specialCharactersRepresentation['&'] = "&amp;".toCharArray();
        specialCharactersRepresentation['<'] = "&lt;".toCharArray();
        specialCharactersRepresentation['>'] = "&gt;".toCharArray();
        specialCharactersRepresentation['"'] = "&#034;".toCharArray();
        specialCharactersRepresentation['\''] = "&#039;".toCharArray();
    }

    /**
     * Escapes string that could be interpreted as HTML.
     * 
     * @param input
     *            the input value
     * @return the escaped value
     */
    public static String h(Object input) {
        if (input == null) {
            return "";
        }
        String str = "";
        if (input.getClass().isArray()) {
            Class<?> clazz = input.getClass().getComponentType();
            if (clazz == String.class) {
                str = Arrays.toString((Object[]) input);
            } else if (clazz == boolean.class) {
                str = Arrays.toString((boolean[]) input);
            } else if (clazz == int.class) {
                str = Arrays.toString((int[]) input);
            } else if (clazz == long.class) {
                str = Arrays.toString((long[]) input);
            } else if (clazz == byte.class) {
                str = Arrays.toString((byte[]) input);
            } else if (clazz == short.class) {
                str = Arrays.toString((short[]) input);
            } else if (clazz == float.class) {
                str = Arrays.toString((float[]) input);
            } else if (clazz == double.class) {
                str = Arrays.toString((double[]) input);
            } else if (clazz == char.class) {
                str = Arrays.toString((char[]) input);
            } else {
                str = Arrays.toString((Object[]) input);
            }
        } else {
            str = input.toString();
        }
        return escape(str);
    }

    /**
     * Escapes string that could be interpreted as HTML.
     * 
     * @param input
     *            the input value
     * @return the escaped value
     */
    public static String escape(String input) {
        int start = 0;
        int length = input.length();
        char[] arrayBuffer = input.toCharArray();
        StringBuilder escapedBuffer = null;

        for (int i = 0; i < length; i++) {
            char c = arrayBuffer[i];
            if (c <= HIGHEST_SPECIAL) {
                char[] escaped = specialCharactersRepresentation[c];
                if (escaped != null) {
                    if (start == 0) {
                        escapedBuffer = new StringBuilder(length + 5);
                    }
                    if (start < i) {
                        escapedBuffer.append(arrayBuffer, start, i - start);
                    }
                    start = i + 1;
                    escapedBuffer.append(escaped);
                }
            }
        }
        if (start == 0) {
            return input;
        }
        if (start < length) {
            escapedBuffer.append(arrayBuffer, start, length - start);
        }
        return escapedBuffer.toString();
    }

    /**
     * Encodes string that could be interpreted as URL.
     * 
     * @param input
     *            the input value
     * @return the encoded value
     */
    public static String u(String input) {
        String encoding = WebLocator.getRequest().getCharacterEncoding();
        try {
            return URLEncoder.encode(input, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns context-relative URL.
     * 
     * @param input
     *            the input value
     * @return context-relative URL
     */
    public static String url(String input) {
        String contextPath = WebLocator.getRequest().getContextPath();
        StringBuilder sb = new StringBuilder();
        if (contextPath.length() > 1) {
            sb.append(contextPath);
        }
        if (StringUtil.isEmpty(input)) {
            sb.append(ControllerUtil.getPath());
        } else if (input.startsWith("/")) {
            sb.append(input);
        } else {
            sb.append(ControllerUtil.getPath()).append(input);
        }
        return WebLocator.getResponse().encodeURL(sb.toString());
    }

    /**
     * Converts string that could be interpreted as Date.
     * 
     * @param input
     *            the input value
     * @param pattern
     *            the date format pattern
     * @return the converted value
     * @throws NullPointerException
     *             if the pattern parameter is null
     * @throws RuntimeException
     *             if {@link ParseException} occurs
     */
    public static Date date(String input, String pattern)
            throws NullPointerException, RuntimeException {
        if (StringUtil.isEmpty(input)) {
            return null;
        }
        if (StringUtil.isEmpty(pattern)) {
            throw new NullPointerException("The pattern parameter is null.");
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(input);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts string that could be interpreted as Number.
     * 
     * @param input
     *            the input value
     * @param pattern
     *            the decimal format pattern
     * @return the converted value
     * @throws NullPointerException
     *             if the pattern parameter is null
     * @throws RuntimeException
     *             if {@link ParseException} occurs
     */
    public static Number number(String input, String pattern)
            throws NullPointerException, RuntimeException {
        if (StringUtil.isEmpty(input)) {
            return null;
        }
        if (StringUtil.isEmpty(pattern)) {
            throw new NullPointerException("The pattern parameter is null.");
        }
        try {
            DecimalFormat format = new DecimalFormat(pattern);
            return format.parse(input);
        } catch (ParseException e) {
            throw new WrapRuntimeException(e);
        }
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
                "\n", BR);
    }

    /**
     * Converts space that could be interpreted as HTML.
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
}