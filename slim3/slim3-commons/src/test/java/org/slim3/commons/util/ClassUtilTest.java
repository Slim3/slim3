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
package org.slim3.commons.util;

import junit.framework.TestCase;

import org.slim3.commons.exception.WrapRuntimeException;

/**
 * @author higa
 * 
 */
public class ClassUtilTest extends TestCase {

    /**
     * 
     */
    public void testNewInstance() {
        String s = ClassUtil.newInstance(String.class);
        assertNotNull(s);
    }

    /**
     * 
     */
    public void testNewInstanceForClassName() {
        String s =
            ClassUtil.newInstance("java.lang.String", Thread
                .currentThread()
                .getContextClassLoader());
        assertNotNull(s);
    }

    /**
     * 
     */
    public void testForName() {
        Class<?> clazz =
            ClassUtil.forName("java.lang.String", Thread
                .currentThread()
                .getContextClassLoader());
        assertNotNull(clazz);
        assertEquals(String.class, clazz);
    }

    /**
     * 
     */
    public void testForNameForBadName() {
        try {
            ClassUtil.forName("xxx", Thread
                .currentThread()
                .getContextClassLoader());
            fail();
        } catch (WrapRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}