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
    protected String packageName;

    /** the simple name */
    protected String simpleName;

    /** the modelClassName */
    protected String modelClassName;

    /** the list of attribute meta descriptions */
    protected List<AttributeMetaDesc> attributeMetaDescList =
        new ArrayList<AttributeMetaDesc>();

    /**
     * Returns the packageName.
     * 
     * @return the packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Sets the packageName.
     * 
     * @param packageName
     *            the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
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
     * Sets the simpleName.
     * 
     * @param simpleName
     *            the simpleName to set
     */
    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getQualifiedName() {
        return ClassUtil.getQualifiedName(packageName, simpleName);
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
     * Sets the modelClassName.
     * 
     * @param modelClassName
     *            the modelClassName to set
     */
    public void setModelClassName(String modelClassName) {
        this.modelClassName = modelClassName;
    }

    /**
     * Adds the attribute meta description.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description.
     */
    public void addAttributeMetaDesc(AttributeMetaDesc attributeMetaDesc) {
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

    public AttributeMetaDesc addAttributeMetaDesc(String name,
            AttributeMetaDesc attributeMetaDesc) {
        throw new AssertionError("not yet implemented.");
    }

    public AttributeMetaDesc getAttributeMetaDesc(String name) {
        throw new AssertionError("not yet implemented.");
    }

}
