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

/**
 * Creates a GWT service description factory.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class GWTServiceDescFactory {

    /** the service package name */
    protected final String packageName;

    /** the service simple name */
    protected final String simpleName;

    /** the remote service relative path */
    protected final String remoteServiceRelativePath;

    /**
     * Creates a new {@link GWTServiceDescFactory}.
     * 
     * @param packageName
     *            the service package name
     * @param simpleName
     *            the service simple name
     * @param remoteServiceRelativePath
     *            the remote service relative path
     */
    public GWTServiceDescFactory(String packageName, String simpleName,
            String remoteServiceRelativePath) {
        if (packageName == null) {
            throw new NullPointerException("The packageName parameter is null.");
        }
        if (simpleName == null) {
            throw new NullPointerException("The simpleName parameter is null.");
        }
        if (remoteServiceRelativePath == null) {
            throw new NullPointerException(
                "The remoteServiceRelativePath parameter is null.");
        }
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.remoteServiceRelativePath = remoteServiceRelativePath;
    }

    /**
     * Creates a service description.
     * 
     * @return a service description.
     */
    public GWTServiceDesc createServiceDesc() {
        GWTServiceDesc serviceDesc = new GWTServiceDesc();
        serviceDesc.setPackageName(packageName);
        serviceDesc.setSimpleName(simpleName);
        return serviceDesc;
    }
}
