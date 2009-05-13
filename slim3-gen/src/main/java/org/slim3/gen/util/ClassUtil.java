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
package org.slim3.gen.util;

/**
 * A utility class for {@link Class}.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public final class ClassUtil {

    /**
     * Returns a package name of the qualifiedName.
     * 
     * @param qualifiedName
     *            the qualifiedName.
     * @return a package name.
     */
    public static String getPackageName(String qualifiedName) {
        if (qualifiedName == null) {
            throw new NullPointerException(
                    "The qualifiedName parameter is null.");
        }
        int pos = qualifiedName.lastIndexOf('.');
        if (pos < 0) {
            return "";
        }
        return qualifiedName.substring(0, pos);
    }

    /**
     * Returns a simple name of the qualifiedName.
     * 
     * @param qualifiedName
     *            the qualifiedName.
     * @return a simple name.
     */
    public static String getSimpleName(String qualifiedName) {
        if (qualifiedName == null) {
            throw new NullPointerException(
                    "The qualifiedName parameter is null.");
        }
        int pos = qualifiedName.lastIndexOf('.');
        if (pos < 0) {
            return qualifiedName;
        }
        return qualifiedName.substring(pos + 1);
    }
}
