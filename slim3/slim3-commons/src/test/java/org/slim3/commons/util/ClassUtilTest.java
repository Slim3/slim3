/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import org.slim3.commons.exception.ClassNotFoundRuntimeException;

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
        String s = ClassUtil.newInstance("java.lang.String", Thread
                .currentThread().getContextClassLoader());
        assertNotNull(s);
    }

    /**
     * 
     */
    public void testForName() {
        Class<?> clazz = ClassUtil.forName("java.lang.String", Thread
                .currentThread().getContextClassLoader());
        assertNotNull(clazz);
        assertEquals(String.class, clazz);
    }

    /**
     * 
     */
    public void testForNameForException() {
        String className = "Xxx";
        try {
            ClassUtil.forName(className, Thread.currentThread()
                    .getContextClassLoader());
        } catch (ClassNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
            assertEquals(className, e.getClassName());
        }
    }

    /**
     * 
     */
    public void testIsPresent() {
        assertTrue(ClassUtil.isPresent("java.lang.String"));
        assertFalse(ClassUtil.isPresent("xxx"));
    }

    /**
     * 
     */
    public void testGetDeclaredField() {
        assertNotNull(ClassUtil.getDeclaredField(Hoge.class, "aaa"));
    }

    private static class Hoge {
        public String aaa;

        public String bbb;

        public String ccc;
    }
}
