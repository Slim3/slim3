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
package org.slim3.gen.task;

import org.slim3.gen.util.ClassUtil;

/**
 * Represents a builder to build a class name.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public class ClassNameBuilder {

    /** the buffer for class name */
    protected final StringBuilder buf = new StringBuilder(30);

    /**
     * Appends a name element.
     * 
     * @param element
     *            a name elemet
     */
    public void append(String element) {
        if (element == null) {
            throw new NullPointerException("The element parameter is null.");
        }
        if (buf.length() > 0 && !endsWithPiriod()) {
            buf.append(".");
        }
        if (element.startsWith(".")) {
            buf.append(element.substring(1));
        } else {
            buf.append(element);
        }
    }

    /**
     * Appends a suffix.
     * 
     * @param suffix
     *            a suffix.
     */
    public void appendSuffix(String suffix) {
        if (suffix == null) {
            throw new NullPointerException("The suffix parameter is null.");
        }
        buf.append(suffix);
    }

    /**
     * Returns {@code true} if {@link #buf} ends with '.'.
     * 
     * @return {@code true} if {@link #buf} ends with '.'
     */
    protected boolean endsWithPiriod() {
        return buf.length() > 0 && buf.charAt(buf.length() - 1) == '.';
    }

    /**
     * Returns the qualifed name.
     * 
     * @return the qualifed name
     */
    public String getQualifiedName() {
        return getQualifiedNameInternal();
    }

    /**
     * Returns the package name.
     * 
     * @return the package name.
     */
    public String getPackageName() {
        return ClassUtil.getPackageName(getQualifiedNameInternal());
    }

    /**
     * Returns the simple name.
     * 
     * @return the simple name
     */
    public String getSimpleName() {
        return ClassUtil.getSimpleName(getQualifiedNameInternal());
    }

    /**
     * Returns the qualifed name internally.
     * 
     * @return the qualifed name
     */
    protected String getQualifiedNameInternal() {
        return buf.toString();
    }

}
