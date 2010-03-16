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
package org.slim3.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A utility class for {@link String}.
 * 
 * @author higa
 * @version 3.0
 */
public final class StringUtil {

    private static final String[] EMPTY_STRINGS = new String[0];

    private StringUtil() {
    }

    /**
     * Determines if the text is empty.
     * 
     * @param text
     *            the text
     * @return whether text is empty
     */
    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    /**
     * Decapitalizes the text according to JavaBeans specification.
     * 
     * @param text
     *            the text
     * 
     * @return the decapitalized text
     */
    public static String decapitalize(String text) {
        if (isEmpty(text)) {
            return text;
        }
        char chars[] = text.toCharArray();
        if (chars.length >= 2
            && Character.isUpperCase(chars[0])
            && Character.isUpperCase(chars[1])) {
            return text;
        }
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    /**
     * Capitalizes the text according to JavaBeans specification.
     * 
     * @param text
     *            the text
     * 
     * @return the capitalized text
     */
    public static String capitalize(String text) {
        if (isEmpty(text)) {
            return text;
        }
        char chars[] = text.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * Splits the text by the delimiter.
     * 
     * @param text
     *            the text
     * @param delim
     *            the delimiter
     * @return the array of strings
     */
    public static String[] split(String text, String delim) {
        if (isEmpty(text)) {
            return EMPTY_STRINGS;
        }
        List<String> list = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(text, delim);
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * Splits the array of bytes.
     * 
     * @param str
     *            the string
     * @param size
     *            the piece size
     * @return the split result.
     * @throws NullPointerException
     *             if the str parameter is null
     * @throws IllegalArgumentException
     *             if the size parameter is less than 1
     */
    public static String[] split(String str, int size)
            throws NullPointerException, IllegalArgumentException {
        if (str == null) {
            throw new NullPointerException("The str parameter is null.");
        }
        if (size <= 0) {
            throw new IllegalArgumentException(
                "The size parameter must be more than 0.");
        }
        int num = str.length() / size;
        int mod = str.length() % size;
        String[] ret = mod > 0 ? new String[num + 1] : new String[num];
        for (int i = 0; i < num; i++) {
            ret[i] = str.substring(i * size, (i + 1) * size);
        }
        if (mod > 0) {
            ret[num] = str.substring(num * size);
        }
        return ret;
    }

    /**
     * Joins the array of strings.
     * 
     * @param array
     *            the array of strings
     * @return the joined string
     * @throws NullPointerException
     *             if the array parameter is null or if the array contains null
     *             element
     */
    public static String join(String[] array) throws NullPointerException {
        if (array == null) {
            throw new NullPointerException("The array parameter is null.");
        }
        int count = 0;
        for (String s : array) {
            if (s == null) {
                throw new NullPointerException(
                    "The array contains null element.");
            }
            count += s.length();
        }
        StringBuilder sb = new StringBuilder(count);
        for (String s : array) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Converts the object into string.
     * 
     * @param o
     *            the object
     * @return string
     */
    public static String toString(Object o) {
        return o == null ? null : o.toString();
    }
}