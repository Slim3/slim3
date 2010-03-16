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
package org.slim3.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

/**
 * @author higa
 * 
 */
public class AppEngineUtilTest {

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        System.clearProperty(AppEngineUtil.ENVIRONMENT_KEY);
        System.clearProperty(AppEngineUtil.VERSION_KEY);
    }

    /**
     * @throws Exception
     */
    @Test
    public void isServer() throws Exception {
        assertThat(AppEngineUtil.isServer(), is(false));
        System.setProperty(
            AppEngineUtil.ENVIRONMENT_KEY,
            AppEngineUtil.DEVELOPMENT);
        assertThat(AppEngineUtil.isServer(), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isDevelopment() throws Exception {
        assertThat(AppEngineUtil.isDevelopment(), is(false));
        System.setProperty(
            AppEngineUtil.ENVIRONMENT_KEY,
            AppEngineUtil.DEVELOPMENT);
        assertThat(AppEngineUtil.isDevelopment(), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isProduction() throws Exception {
        assertThat(AppEngineUtil.isProduction(), is(false));
        System.setProperty(
            AppEngineUtil.ENVIRONMENT_KEY,
            AppEngineUtil.PRODUCTION);
        assertThat(AppEngineUtil.isProduction(), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getVersion() throws Exception {
        System.setProperty(AppEngineUtil.VERSION_KEY, "x.x.x");
        assertThat(AppEngineUtil.getVersion(), is("x.x.x"));
    }
}