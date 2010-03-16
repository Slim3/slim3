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

import java.util.Locale;

/**
 * A utility class for {@link Locale}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class LocaleUtil {

    /**
     * Parses the string expression of {@link Locale}.
     * 
     * @param text
     *            the string expression of {@link Locale}
     * @return the parsed result
     */
    public static Locale parse(String text) {
        if (StringUtil.isEmpty(text)) {
            return null;
        }
        String[] array = StringUtil.split(text, "_");
        if (array.length == 1) {
            return new Locale(array[0]);
        } else if (array.length == 2) {
            return new Locale(array[0], array[1]);
        } else if (array.length == 3) {
            return new Locale(array[0], array[1], array[2]);
        } else {
            throw new IllegalArgumentException("The locale("
                + text
                + ") is invalid.");
        }
    }

    private LocaleUtil() {
    }
}
