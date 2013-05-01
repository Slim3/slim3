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
package org.slim3.gen.desc;

import org.slim3.gen.Constants;
import org.slim3.gen.util.ClassUtil;
import org.slim3.gen.util.StringUtil;

/**
 * Represents a model meta class name.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public class ModelMetaClassName {

    /** the model class name */
    protected final String modelClassName;

    /** the model package */
    protected final String modelPackage;

    /** the meta package */
    protected final String metaPackage;

    /** the shared package */
    protected final String sharedPackage;

    /** the server package */
    protected final String serverPackage;

    /** the model meta package name */
    protected final String packageName;

    /** the model meta simple name */
    protected final String simpleName;

    /** the kind of entity */
    protected final String kind;

    /**
     * Creates a new {@link ModelMetaClassName}.
     * 
     * @param modelClassName
     *            the model class name
     * @param modelPackage
     *            the model package
     * @param metaPackage
     *            the meta package
     * @param sharedPackage
     *            the shared package
     * @param serverPackage
     *            the server package
     */
    public ModelMetaClassName(String modelClassName, String modelPackage,
            String metaPackage, String sharedPackage, String serverPackage) {
        if (modelClassName == null) {
            throw new NullPointerException(
                "The modelClassName parameter is null.");
        }
        if (modelPackage == null) {
            throw new NullPointerException(
                "The modelPackage parameter is null.");
        }
        if (metaPackage == null) {
            throw new NullPointerException("The metaPackage parameter is null.");
        }
        if (sharedPackage == null) {
            throw new NullPointerException(
                "The sharedPackage parameter is null.");
        }
        if (serverPackage == null) {
            throw new NullPointerException(
                "The serverPackage parameter is null.");
        }
        this.modelClassName = modelClassName;
        this.modelPackage = modelPackage;
        this.metaPackage = metaPackage;
        this.sharedPackage = sharedPackage;
        this.serverPackage = serverPackage;
        modelClassName =
            replacePackageName(modelClassName, modelPackage, metaPackage);
        modelClassName =
            replacePackageName(modelClassName, sharedPackage, serverPackage);
        this.packageName = ClassUtil.getPackageName(modelClassName);
        this.simpleName =
            ClassUtil.getSimpleName(modelClassName) + Constants.META_SUFFIX;
        this.kind = ClassUtil.getSimpleName(modelClassName);
    }

    /**
     * Replaces the package name.
     * 
     * @param originalPackageName
     *            the original package name
     * @param from
     *            the package to be replaced
     * @param to
     *            the package to be replacement
     * @return the meta package name.
     */
    protected String replacePackageName(String originalPackageName,
            String from, String to) {
        if (StringUtil.isEmpty(originalPackageName)) {
            return originalPackageName;
        }
        int index = originalPackageName.lastIndexOf("." + from + ".");
        if (index < 0) {
            return originalPackageName;
        }
        return originalPackageName.substring(0, index)
            + "."
            + to
            + "."
            + originalPackageName.substring(index + from.length() + 2);
    }

    /**
     * Returns the qualified name.
     * 
     * @return the qualified name
     */
    public String getQualifiedName() {
        return ClassUtil.getQualifiedName(packageName, simpleName);
    }

    /**
     * Returns the packageName.
     * 
     * @return the packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Returns the simpleName.
     * 
     * @return the simpleName
     */
    public String getSimpleName() {
        return simpleName;
    }

    /**
     * Returns the kind of entity
     * 
     * @return the kind of entity
     */
    public String getKind() {
        return kind;
    }
}
