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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class PropertyDescTest {

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
    @Test
    public void setReadMethod() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        Method m = getClass().getDeclaredMethod("getAaa");
        pd.setReadMethod(m);
        assertThat(pd.getReadMethod(), is(sameInstance(m)));
        assertThat(pd.isReadable(), is(true));
        assertThat(pd.isWritable(), is(false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void setWriteMethod() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        Method m = getClass().getDeclaredMethod("setAaa", int.class);
        pd.setWriteMethod(m);
        assertThat(pd.getWriteMethod(), is(sameInstance(m)));
        assertThat(pd.isReadable(), is(false));
        assertThat(pd.isWritable(), is(true));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getValue() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        Method m = getClass().getDeclaredMethod("getAaa");
        pd.setReadMethod(m);
        assertThat((Integer) pd.getValue(this), is(1));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getValueForNonPublicClass() throws Exception {
        Hoge hoge = new HogeImpl();
        hoge.setAaa("111");
        PropertyDesc pd = new PropertyDesc("aaa", String.class, HogeImpl.class);
        Method m = hoge.getClass().getMethod("getAaa");
        pd.setReadMethod(m);
        assertThat((String) pd.getValue(hoge), is("111"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void valueForNotReadable() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        pd.getValue(this);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void setValue() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        Method m = getClass().getDeclaredMethod("setAaa", int.class);
        pd.setWriteMethod(m);
        pd.setValue(this, "2");
        assertThat(aaa, is(2));
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void setValueForNotWritable() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        pd.setValue(this, null);
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = WrapRuntimeException.class)
    public void setValueForIllegalValue() throws Exception {
        PropertyDesc pd = new PropertyDesc("aaa", int.class, getClass());
        Method m = getClass().getDeclaredMethod("setAaa", int.class);
        pd.setWriteMethod(m);
        pd.setValue(this, "xxx");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void setValueForNonPublicClass() throws Exception {
        Hoge hoge = new HogeImpl();
        PropertyDesc pd = new PropertyDesc("aaa", String.class, HogeImpl.class);
        Method m = hoge.getClass().getMethod("setAaa", String.class);
        pd.setWriteMethod(m);
        pd.setValue(hoge, "111");
        assertThat(hoge.getAaa(), is("111"));
    }

    /**
     * 
     */
    public interface Hoge {
        /**
         * @return aaa
         */
        String getAaa();

        /**
         * @param aaa
         */
        void setAaa(String aaa);
    }

    private static class HogeImpl implements Hoge {
        private String aaa;

        /**
         * @return the aaa
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param aaa
         *            the aaa to set
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }
    }
}