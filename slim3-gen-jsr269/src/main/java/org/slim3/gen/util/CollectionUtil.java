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

import java.util.Collection;

/**
 * A utility class for {@link Collection}.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public final class CollectionUtil {

    /**
     * Joins strings with the separator.
     * 
     * @param collection
     *            the collection
     * @param separator
     *            the separator
     * @return a string joined with the separator
     */
    public static String join(Collection<String> collection, String separator) {
        if (collection == null) {
            throw new NullPointerException("The collection parameter is null.");
        }
        if (separator == null) {
            throw new NullPointerException("The separator parameter is null.");
        }
        StringBuilder buf = new StringBuilder();
        for (String s : collection) {
            buf.append("\"");
            buf.append(s);
            buf.append("\"");
            buf.append(separator);
        }
        if (buf.length() > 0) {
            buf.setLength(buf.length() - separator.length());
        }
        return buf.toString();
    }
}
