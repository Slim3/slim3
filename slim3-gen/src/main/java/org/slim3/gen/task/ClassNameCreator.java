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
package org.slim3.gen.task;

import org.slim3.gen.Constants;
import org.slim3.gen.util.StringUtil;

/**
 * Creates a class name.
 * 
 * @author taedium
 * @since 3.0
 */
public class ClassNameCreator {

    /** the packageName */
    protected final String packageName;

    /** the simpleName */
    protected final String simpleName;

    /**
     * Creates a new {@link ClassNameCreator}.
     * 
     * @param packageName
     *            the packageName
     * @param simpleName
     *            the simpleName
     */
    public ClassNameCreator(String packageName, String simpleName) {
        this.packageName = packageName;
        this.simpleName = simpleName;
    }

    /**
     * Creates a class name.
     * 
     * @return a class name.
     */
    public String createClassName() {
        return createClassName("");
    }

    /**
     * Creates a class name of test case.
     * 
     * @return a class name of test case
     */
    public String createTestCaseClassName() {
        return createClassName(Constants.TEST_SUFFIX);
    }

    /**
     * Creates a class name with a suffix.
     * 
     * @param suffix
     *            the suffix
     * @return a class name.
     */
    protected String createClassName(String suffix) {
        if (isDefaultPackage()) {
            return simpleName + suffix;
        }
        return packageName + "." + simpleName + suffix;
    }

    /**
     * Returns {@code true} if the class belongs to the default package.
     * 
     * @return {@code true} if the class belongs to the default package.
     */
    protected boolean isDefaultPackage() {
        return StringUtil.isEmpty(packageName);
    }

}
