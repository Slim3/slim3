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

import org.slim3.gen.util.ClassUtil;

/**
 * Represents a GWT service description.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public class GWTServiceDesc implements ClassDesc {

    /** the package name */
    protected String packageName;

    /** the simple name */
    protected String simpleName;

    /** the remote service relative path */
    protected String remoteServiceRelativePath;

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
     * Returns the remote service relative path.
     * 
     * @return the remote service relative path
     */
    public String getRemoteServiceRelativePath() {
        return remoteServiceRelativePath;
    }

    /**
     * Sets the remote service relative path.
     * 
     * @param remoteServiceRelativePath
     *            the remote service relative path
     */
    public void setRemoteServiceRelativePath(String remoteServiceRelativePath) {
        this.remoteServiceRelativePath = remoteServiceRelativePath;
    }

}
