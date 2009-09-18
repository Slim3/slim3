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
package org.slim3.tester;

import java.io.File;

import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;

/**
 * A tester for local services.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class ServiceTester {

    /**
     * The base path for api proxy local implementation.
     */
    protected String basePath = "build";

    /**
     * Returns the base path.
     * 
     * @return the base path
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * Sets the base path.
     * 
     * @param basePath
     *            the base path
     */
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    /**
     * Sets up the test environment.
     * 
     * @throws Exception
     *             if an exception has occurred
     * 
     */
    public void setUp() throws Exception {
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
        ApiProxy.setDelegate(new ApiProxyLocalImpl(new File(basePath)) {
        });
    }

    /**
     * Tears down the test environment.
     * 
     * @throws Exception
     *             if an exception has occurred
     */
    public void tearDown() throws Exception {
        ApiProxy.setDelegate(null);
        ApiProxy.setEnvironmentForCurrentThread(null);
    }
}