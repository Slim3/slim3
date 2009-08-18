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

import org.slim3.gen.Constants;
import org.slim3.gen.util.ClassUtil;

/**
 * Creates a GWT service async description factory.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class GWTServiceAsyncDescFactory {

    /** the service class name */
    protected final String serviceClassName;

    /**
     * Creates a new {@link GWTServiceAsyncDescFactory}.
     * 
     * @param serviceClassName
     *            the service class name
     */
    public GWTServiceAsyncDescFactory(String serviceClassName) {
        if (serviceClassName == null) {
            throw new NullPointerException(
                "The serviceClassName parameter is null.");
        }
        this.serviceClassName = serviceClassName;
    }

    /**
     * Creates a service async description.
     * 
     * @return a service async description.
     */
    public GWTServiceAsyncDesc createServiceAsyncDesc() {
        GWTServiceAsyncDesc serviceAsyncDesc = new GWTServiceAsyncDesc();
        serviceAsyncDesc.setPackageName(ClassUtil
            .getPackageName(serviceClassName));
        serviceAsyncDesc.setSimpleName(ClassUtil
            .getSimpleName(serviceClassName)
            + Constants.ASYNC_SUFFIX);
        return serviceAsyncDesc;
    }
}
