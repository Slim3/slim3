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

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class ClassUtilTest {

    /**
     * 
     */
    @Test
    public void newInstance() {
        String s = ClassUtil.newInstance(String.class);
        assertThat(s, is(notNullValue()));
    }

    /**
     * 
     */
    @Test
    public void newInstanceForClassName() {
        String s =
            ClassUtil.newInstance("java.lang.String", Thread
                .currentThread()
                .getContextClassLoader());
        assertThat(s, is(notNullValue()));
    }

    /**
     * 
     */
    @Test
    public void forName() {
        Class<?> clazz =
            ClassUtil.forName("java.lang.String", Thread
                .currentThread()
                .getContextClassLoader());
        assertThat(clazz, is(notNullValue()));
        assertThat(clazz.getName(), is(String.class.getName()));
    }

    /**
     * 
     */
    @Test(expected = WrapRuntimeException.class)
    public void forNameForBadName() {
        ClassUtil
            .forName("xxx", Thread.currentThread().getContextClassLoader());
    }
}