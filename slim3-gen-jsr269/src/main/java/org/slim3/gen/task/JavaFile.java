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

import java.io.File;

import org.slim3.gen.desc.ClassDesc;
import org.slim3.gen.util.StringUtil;

/**
 * Represents a java file.
 * 
 * @author taedium
 * @since 1.0.0
 */
public class JavaFile {

    /** the base directory */
    protected final File baseDir;

    /** the class description */
    protected final ClassDesc classDesc;

    /** the suffix of class name */
    protected final String suffix;

    /** the file which represents a java file */
    protected final File file;

    /**
     * Creates a new {@link JavaFile}.
     * 
     * @param baseDir
     *            the base directory
     * @param classDesc
     *            the class description
     */
    public JavaFile(File baseDir, ClassDesc classDesc) {
        this(baseDir, classDesc, "");
    }

    /**
     * Creates a new {@link JavaFile}.
     * 
     * @param baseDir
     *            the base directory
     * @param classDesc
     *            the class description
     * @param suffix
     *            the suffix of class name
     */
    public JavaFile(File baseDir, ClassDesc classDesc, String suffix) {
        super();
        this.baseDir = baseDir;
        this.classDesc = classDesc;
        this.suffix = suffix;
        this.file = createFile(baseDir, classDesc, suffix);
    }

    /**
     * Creates a file.
     * 
     * @param baseDir
     *            the base directory
     * @param classDesc
     *            the class description
     * @param suffix
     *            the suffix of class name
     * @return a file
     */
    protected File createFile(File baseDir, ClassDesc classDesc, String suffix) {
        File packageDir = null;
        if (StringUtil.isEmpty(classDesc.getPackageName())) {
            packageDir = baseDir;
        } else {
            packageDir =
                new File(baseDir, classDesc.getPackageName().replace(
                    ".",
                    File.separator));
        }
        mkdirs(packageDir);
        return new File(packageDir, classDesc.getSimpleName().replace('.', '/')
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
     * Returns the class name.
     * 
     * @return the class name
     */
    public String getClassName() {
        return classDesc.getQualifiedName() + suffix;
    }

    /**
     * Returns the file.
     * 
     * @return the file
     */
    public File getFile() {
        return file;
    }
}
