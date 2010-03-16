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
public class CallerUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void getCallerStack() throws Exception {
        Class<?>[] stack = CallerUtil.getCallerStack();
        assertThat(stack, is(notNullValue()));
        assertThat(stack[2].getName(), is(getClass().getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getCaller() throws Exception {
        assertThat(new Hoge().getCaller().getName(), is(getClass().getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getCallerClassLoader() throws Exception {
        assertThat(new Hoge().getCallerClassLoader(), is(getClass()
            .getClassLoader()));
    }

    private static class Hoge {
        public Class<?> getCaller() {
            return CallerUtil.getCaller();
        }

        public ClassLoader getCallerClassLoader() {
            return CallerUtil.getCallerClassLoader();
        }
    }
}
