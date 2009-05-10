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

import org.slim3.controller.Controller;
import org.slim3.controller.ControllerConstants;
import org.slim3.controller.RequestLocator;
import org.slim3.controller.ResponseLocator;
import org.slim3.util.StringUtil;

/**
 * JSP functions of Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class Functions {

    private static final int HIGHEST_SPECIAL = '>';

    private static String BR = "<br />";

    private static char[][] specialCharactersRepresentation =
        new char[HIGHEST_SPECIAL + 1][];

    static {
        specialCharactersRepresentation['&'] = "&amp;".toCharArray();
        specialCharactersRepresentation['<'] = "&lt;".toCharArray();
        specialCharactersRepresentation['>'] = "&gt;".toCharArray();
        specialCharactersRepresentation[' '] = "&nbsp;".toCharArray();
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
        if (input == null || "".equals(input)) {
            return "";
        }
        if (input.getClass() == String.class) {
            return escape(input.toString());
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
     * Escapes string that could be interpreted as HTML.
     * 
     * @param input
     *            the input value
     * @return the escaped value
     */
    private static String escape(String input) {
        int start = 0;
        char[] arrayBuffer = input.toCharArray();
        int length = arrayBuffer.length;
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
        Controller controller =
            (Controller) RequestLocator.getRequest().getAttribute(
                ControllerConstants.CONTROLLER_KEY);
        if (controller == null) {
            return input;
        }
        String contextPath = RequestLocator.getRequest().getContextPath();
        StringBuilder sb = new StringBuilder(50);
        if (contextPath.length() > 1) {
            sb.append(contextPath);
        }
        if (empty) {
            sb.append(controller.getApplicationPath());
        } else if (input.startsWith("/")) {
            sb.append(input);
        } else {
            sb.append(controller.getApplicationPath()).append(input);
        }
        return ResponseLocator.getResponse().encodeURL(sb.toString());
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

    private Functions() {
    }
}