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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

/**
 * @author higa
 * 
 */
public class AppEngineTesterTest {

    private AppEngineTester tester = new AppEngineTester();

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        tester.setUp();
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        tester.tearDown();
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getLibDir() throws Exception {
        File libDir = AppEngineTester.getLibDir();
        System.out.println(libDir);
        assertThat(libDir.exists(), is(true));
        assertThat(libDir.isDirectory(), is(true));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getLocalRuntimeJar() throws Exception {
        File libDir = AppEngineTester.getLibDir();
        File implDir = new File(libDir, AppEngineTester.IMPL_DIR_NAME);
        if (implDir.exists()) {
            URL url =
                AppEngineTester.getLibraryURL(
                    implDir,
                    AppEngineTester.LOCAL_RUNTIME_LIB_NAME);
            assertThat(url, is(notNullValue()));
        }
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getApiStubsJar() throws Exception {
        File libDir = AppEngineTester.getLibDir();
        File implDir = new File(libDir, AppEngineTester.IMPL_DIR_NAME);
        if (implDir.exists()) {
            URL url =
                AppEngineTester.getLibraryURL(
                    implDir,
                    AppEngineTester.API_STUBS_LIB_NAME);
            System.out.println(url);
            assertThat(url, is(notNullValue()));
        }
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getCount() throws Exception {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        ds.put(new Entity("Hoge"));
        assertThat(tester.count("Hoge"), is(1));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void environment() throws Exception {
        assertThat(tester.environment, is(notNullValue()));
    }
}