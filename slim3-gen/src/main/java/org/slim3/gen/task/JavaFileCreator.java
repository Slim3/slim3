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

import java.io.File;

import org.slim3.gen.Constants;
import org.slim3.gen.util.StringUtil;

/**
 * Creates a java file.
 * 
 * @author taedium
 * @since 3.0
 */
public class JavaFileCreator {

    /** the source directory */
    protected final File srcDir;

    /** the test source directory */
    protected final File testDir;

    /** the package name */
    protected final String packageName;

    /** the simple name */
    protected final String simpleName;

    /**
     * Creates a new {@link JavaFileCreator}.
     * 
     * @param packageName
     *            the package name
     * @param simpleName
     *            the simple name
     * @param srcDir
     *            the source directory
     * @param testDir
     *            the test source directory
     */
    public JavaFileCreator(File srcDir, File testDir, String packageName,
            String simpleName) {
        this.srcDir = srcDir;
        this.testDir = testDir;
        this.packageName = packageName;
        this.simpleName = simpleName;
    }

    /**
     * Creates a java file.
     * 
     * @return a java file
     */
    public File createJavaFile() {
        return createJavaFile(srcDir, "");
    }

    /**
     * Creates a test case java file.
     * 
     * @return a test case java file
     */
    public File createTestCaseJavaFile() {
        return createJavaFile(testDir, Constants.TEST_SUFFIX);
    }

    /**
     * Creates a java file with a suffix.
     * 
     * @param dir
     *            the parent directory
     * @param suffix
     *            the suffix
     * @return a java file
     */
    protected File createJavaFile(File dir, String suffix) {
        File packageDir = null;
        if (isDefaultPackage()) {
            packageDir = dir;
        } else {
            packageDir =
                new File(dir, packageName.replace(".", File.separator));
        }
        mkdirs(packageDir);
        return new File(packageDir, simpleName.replace('.', '/')
            + suffix
            + ".java");
    }

    /**
     * Creates the directory, including any necessary but nonexistent parent
     * directories.
     * 
     * @param dir
     *            the directory
     */
    protected void mkdirs(File dir) {
        dir.mkdirs();
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
