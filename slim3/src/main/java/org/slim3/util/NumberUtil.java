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

import java.text.DecimalFormat;
import java.text.ParseException;


/**
 * A utility class for {@link Number}.
 * 
 * @author higa
 * @version 3.0
 */
public final class NumberUtil {

    /**
     * Converts text to {@link Number}.
     * 
     * @param text
     *            the text
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the converted value
     * @throws NullPointerException
     *             if the pattern parameter is null
     * @throws WrapRuntimeException
     *             if an error occurred while parsing the text
     */
    public static Number toNumber(String text, String pattern)
            throws NullPointerException, WrapRuntimeException {
        if (StringUtil.isEmpty(text)) {
            return null;
        }
        if (StringUtil.isEmpty(pattern)) {
            throw new NullPointerException("The pattern parameter is null.");
        }
        try {
            DecimalFormat df = new DecimalFormat(pattern);
            return df.parse(text);
        } catch (ParseException cause) {
            throw new WrapRuntimeException(
                "An error occurred while parsing the text("
                    + text
                    + "). Error message: "
                    + cause.getMessage(),
                cause);
        }
    }

    /**
     * Converts the {@link Number} value to text.
     * 
     * @param value
     *            the {@link Number} value
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @return the converted value
     * @throws NullPointerException
     *             if the pattern parameter is null
     */
    public static String toString(Number value, String pattern)
            throws NullPointerException {
        if (value == null) {
            return null;
        }
        if (StringUtil.isEmpty(pattern)) {
            throw new NullPointerException("The pattern parameter is null.");
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(value);
    }

    private NumberUtil() {
    }
}