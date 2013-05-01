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
package org.slim3.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author higa
 * 
 */
public class HotReloadingClassLoaderTest {

    private static final String ROOT_PACKAGE =
        HotReloadingClassLoaderTest.class.getPackage().getName();
    /**
     * 
     */
    protected static final String CONTROLLER_CLASS_NAME =
        ROOT_PACKAGE + ".controller.HogeController";

    /**
     * 
     */
    protected ClassLoader originalClassLoader;

    /**
     * 
     */
    protected HotReloadingClassLoader hotClassLoader;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        originalClassLoader = Thread.currentThread().getContextClassLoader();
        hotClassLoader =
            new HotReloadingClassLoader(
                originalClassLoader,
                ROOT_PACKAGE,
                "cool");
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    @SuppressWarnings("rawtypes")
    public void loadClass() throws Exception {
        Class clazz = hotClassLoader.loadClass(CONTROLLER_CLASS_NAME);
        assertThat(clazz, is(not(nullValue())));
        assertThat(
            (HotReloadingClassLoader) clazz.getClassLoader(),
            is(sameInstance(hotClassLoader)));
        assertThat(
            hotClassLoader.loadClass(CONTROLLER_CLASS_NAME),
            is(sameInstance(clazz)));
        assertThat(clazz.getPackage(), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isTarget() throws Exception {
        assertThat(hotClassLoader.isTarget(CONTROLLER_CLASS_NAME), is(true));
        assertThat(hotClassLoader.isTarget(ROOT_PACKAGE
            + ".cool.service.AaaService"), is(false));
        assertThat(hotClassLoader.isTarget(String.class.getName()), is(false));
    }
}
