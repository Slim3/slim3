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
package org.slim3.gen.desc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slim3.gen.util.ClassUtil;

/**
 * Represents a model meta description.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ModelMetaDesc implements ClassDesc {

    /** the package name */
    protected final String packageName;

    /** the simple name */
    protected final String simpleName;

    /** {@code true} if model is abstract */
    protected final boolean abstrct;

    /** the modelClassName */
    protected final String modelClassName;

    /** the kind of entity */
    protected final String kind;

    /** the class hierarchy list */
    protected final List<String> classHierarchyList;

    /** the class name list of model listener */
    protected final List<String> modelListenerClassNames =
        new ArrayList<String>();

    /** the key attribute meta description */
    protected AttributeMetaDesc keyAttributeMetaDesc;

    /** the version attribute meta description */
    protected AttributeMetaDesc versionAttributeMetaDesc;

    /** the list of attribute meta descriptions */
    protected final List<AttributeMetaDesc> attributeMetaDescList =
        new ArrayList<AttributeMetaDesc>();

    /** {@code true} if this instance state is error */
    protected boolean error;

    /**
     * Creates a new {@link ModelMetaDesc}.
     * 
     * @param packageName
     *            the package name
     * @param simpleName
     *            the simple name
     * @param abstrct
     *            {@code true} if model is abstract
     * @param modelClassName
     *            the modelClassName
     * @param kind
     *            the kind of entity
     * @param classHierarchyList
     *            the class hierarchy list
     */
    public ModelMetaDesc(String packageName, String simpleName,
            boolean abstrct, String modelClassName, String kind,
            List<String> classHierarchyList) {
        if (packageName == null) {
            throw new NullPointerException("The packageName parameter is null.");
        }
        if (simpleName == null) {
            throw new NullPointerException("The simpleName parameter is null.");
        }
        if (modelClassName == null) {
            throw new NullPointerException(
                "The modelClassName parameter is null.");
        }
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.abstrct = abstrct;
        this.modelClassName = modelClassName;
        this.kind = kind;
        this.classHierarchyList = classHierarchyList;
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

    public String getQualifiedName() {
        return ClassUtil.getQualifiedName(packageName, simpleName);
    }

    /**
     * Returns {@code true} if model is abstract.
     * 
     * @return {@code true} if model is abstract
     */
    public boolean isAbstrct() {
        return abstrct;
    }

    /**
     * Returns the modelClassName.
     * 
     * @return the modelClassName
     */
    public String getModelClassName() {
        return modelClassName;
    }

    /**
     * Returns the kind of entity.
     * 
     * @return the kind of entity
     */
    public String getKind() {
        return kind;
    }

    /**
     * Returns the class hierarchy list.
     * 
     * @return the class hierarchy list
     */
    public List<String> getClassHierarchyList() {
        return classHierarchyList;
    }

    /**
     * Adds the attribute meta description.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description.
     */
    public void addAttributeMetaDesc(AttributeMetaDesc attributeMetaDesc) {
        if (attributeMetaDesc.isPrimaryKey()) {
            keyAttributeMetaDesc = attributeMetaDesc;
        }
        if (attributeMetaDesc.isVersion()) {
            versionAttributeMetaDesc = attributeMetaDesc;
        }
        this.attributeMetaDescList.add(attributeMetaDesc);
    }

    /**
     * Returns the list of attribute meta descriptions.
     * 
     * @return the list of attribute meta descriptions
     */
    public List<AttributeMetaDesc> getAttributeMetaDescList() {
        return Collections.unmodifiableList(attributeMetaDescList);
    }

    /**
     * Returns the key attribute meta description.
     * 
     * @return the key attribute meta description
     */
    public AttributeMetaDesc getKeyAttributeMetaDesc() {
        return keyAttributeMetaDesc;
    }

    /**
     * Returns the version attribute meta description.
     * 
     * @return the version attribute meta description
     */
    public AttributeMetaDesc getVersionAttributeMetaDesc() {
        return versionAttributeMetaDesc;
    }

    /**
     * Returns {@code true} if this instance is error.
     * 
     * @return {@code true} if this instance is error
     */
    public boolean isError() {
        return error;
    }

    /**
     * Sets {@code true} if this instance is error.
     * 
     * @param error
     *            {@code true} this instance state is error
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * @param modelListenerClassName
     *            the class name of model listener
     */
    public void addModelListenerClassName(String modelListenerClassName) {
        this.modelListenerClassNames.add(modelListenerClassName);
    }

    /**
     * 
     * @return the class name list of model listener
     */
    public List<String> getModelListenerClassNames() {
        return modelListenerClassNames;
    }

}
