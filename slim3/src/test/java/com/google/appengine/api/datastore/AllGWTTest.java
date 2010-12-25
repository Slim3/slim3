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
package com.google.appengine.api.datastore;

import com.google.gwt.junit.tools.GWTTestSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author galdolber
 *
 */
public class AllGWTTest extends GWTTestSuite {

    /**
     * @return Suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(AllGWTTest.class.getName());
        //$JUnit-BEGIN$
        suite.addTestSuite(RpcSerializationGWTTest.class);
        suite.addTestSuite(KeyGWTTest.class);
        suite.addTestSuite(KeyFactoryGWTTest.class);
        //$JUnit-END$
        return suite;
    }

}
