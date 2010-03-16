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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A utility for header.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class HeaderUtil {

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

    private HeaderUtil() {
    }

    /**
     * Converts the string value to date.
     * 
     * @param value
     *            the string value
     * @return converted value
     */
    public static long convertStringToDate(String value) {
        if (value == null) {
            return -1L;
        }
        try {
            return DATE_FORMAT.parse(value).getTime();
        } catch (ParseException ignore) {
        }
        throw new IllegalArgumentException(value);
    }

    /**
     * Converts the string value to int.
     * 
     * @param value
     *            the string value
     * @return converted value
     */
    public static int convertStringToInt(String value) {
        if (value == null) {
            return -1;
        }
        return Integer.parseInt(value);
    }

    /**
     * Converts the date value to string.
     * 
     * @param value
     *            the date value
     * @return converted value
     */
    public static String convertDateToString(long value) {
        return DATE_FORMAT.format(new Date(value));
    }

    /**
     * Converts the int value to string.
     * 
     * @param value
     *            the date value
     * @return converted value
     */
    public static String convertIntToString(int value) {
        return String.valueOf(value);
    }
}