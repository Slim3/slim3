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
package org.slim3.gen.util;

/**
 * A utility class for {@link String}.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public final class StringUtil {

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
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

}
