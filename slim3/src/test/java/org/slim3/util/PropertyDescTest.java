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
package org.slim3.util;

import java.lang.reflect.Method;

import junit.framework.TestCase;


/**
 * @author higa
 * 
 */
public class PropertyDescTest extends TestCase {

    private int aaa = 1;

    /**
     * @return the aaa
     */
    public int getAaa() {
        return aaa;
    }

    /**
     * @param aaa
     *            the aaa to set
     */
    public void setAaa(int aaa) {
        this.aaa = aaa;
    }

    /**
     * 
     * @throws Exception
     */
    public void testSetReadMethod() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        Method m = getClass().getDeclaredMethod("getAaa");
        pd.setReadMethod(m);
        assertSame(m, pd.getReadMethod());
        assertTrue(pd.isReadable());
        assertFalse(pd.isWritable());
    }

    /**
     * 
     * @throws Exception
     */
    public void testSetWriteMethod() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        Method m = getClass().getDeclaredMethod("setAaa", int.class);
        pd.setWriteMethod(m);
        assertSame(m, pd.getWriteMethod());
        assertFalse(pd.isReadable());
        assertTrue(pd.isWritable());
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetValue() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        Method m = getClass().getDeclaredMethod("getAaa");
        pd.setReadMethod(m);
        assertEquals(1, pd.getValue(this));
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetValueForNotReadable() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        try {
            pd.getValue(this);
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testSetValue() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        Method m = getClass().getDeclaredMethod("setAaa", int.class);
        pd.setWriteMethod(m);
        pd.setValue(this, "2");
        assertEquals(2, aaa);
    }

    /**
     * 
     * @throws Exception
     */
    public void testSetValueForNotWritable() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        try {
            pd.setValue(this, null);
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testSetValueForIllegalValue() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        Method m = getClass().getDeclaredMethod("setAaa", int.class);
        pd.setWriteMethod(m);
        try {
            pd.setValue(this, "xxx");
            fail();
        } catch (WrapRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}