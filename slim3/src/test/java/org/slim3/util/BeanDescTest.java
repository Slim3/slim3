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

import org.slim3.util.BeanDesc;
import org.slim3.util.PropertyDesc;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class BeanDescTest extends TestCase {

    /**
     * 
     */
    public void testPropertyDesc() {
        BeanDesc beanDesc = BeanDesc.create(MyBean.class);
        PropertyDesc pd = beanDesc.getPropertyDesc("aaa");
        assertNotNull(pd);
        assertEquals("aaa", pd.getName());
        assertEquals(String.class, pd.getPropertyClass());
        assertNotNull(pd.getReadMethod());
        assertNotNull(pd.getWriteMethod());
    }

    /**
     * 
     */
    public void testPropertyDescForReadOnly() {
        BeanDesc beanDesc = BeanDesc.create(MyBean.class);
        PropertyDesc pd = beanDesc.getPropertyDesc("bbb");
        assertNotNull(pd);
        assertEquals("bbb", pd.getName());
        assertEquals(String.class, pd.getPropertyClass());
        assertNotNull(pd.getReadMethod());
        assertNull(pd.getWriteMethod());
    }

    /**
     * 
     */
    public void testPropertyDescForWriteOnly() {
        BeanDesc beanDesc = BeanDesc.create(MyBean.class);
        PropertyDesc pd = beanDesc.getPropertyDesc("ccc");
        assertNotNull(pd);
        assertEquals("ccc", pd.getName());
        assertEquals(String.class, pd.getPropertyClass());
        assertNull(pd.getReadMethod());
        assertNotNull(pd.getWriteMethod());
    }

    /**
     * 
     */
    public void testPropertyDescForBoolean() {
        BeanDesc beanDesc = BeanDesc.create(MyBean.class);
        PropertyDesc pd = beanDesc.getPropertyDesc("ddd");
        assertNotNull(pd);
        assertEquals("ddd", pd.getName());
        assertEquals(boolean.class, pd.getPropertyClass());
        assertNotNull(pd.getReadMethod());
        assertNull(pd.getWriteMethod());
    }

    /**
     * 
     */
    public void testPropertyDescForBooleanWrapper() {
        BeanDesc beanDesc = BeanDesc.create(MyBean.class);
        PropertyDesc pd = beanDesc.getPropertyDesc("eee");
        assertNotNull(pd);
        assertEquals("eee", pd.getName());
        assertEquals(Boolean.class, pd.getPropertyClass());
        assertNotNull(pd.getReadMethod());
        assertNull(pd.getWriteMethod());
    }

    /**
     * 
     */
    public void testPropertyDescForIsBooleanWrapper() {
        BeanDesc beanDesc = BeanDesc.create(MyBean.class);
        assertNull(beanDesc.getPropertyDesc("fff"));
    }

    /**
     * 
     */
    public void testPropertyDescForIllegalProperty() {
        BeanDesc beanDesc = BeanDesc.create(MyBean.class);
        assertNull(beanDesc.getPropertyDesc("hhh"));
    }

    private static class MyBean {
        private String aaa;

        private String bbb;

        @SuppressWarnings("unused")
        private String ccc;

        private boolean ddd;

        private Boolean eee;

        private Boolean fff;

        /**
         * @return the result
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param aaa
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        /**
         * @return the result
         */
        public String getBbb() {
            return bbb;
        }

        /**
         * @param ccc
         */
        public void setCcc(String ccc) {
            this.ccc = ccc;
        }

        /**
         * @return the result
         */
        public boolean isDdd() {
            return ddd;
        }

        /**
         * @return the result
         */
        public Boolean getEee() {
            return eee;
        }

        /**
         * @return the result
         */
        public Boolean isFff() {
            return fff;
        }

        /**
         * @return the result
         */
        public String getHhh() {
            return null;
        }

        /**
         * @param hhh
         */
        public void setHhh(int hhh) {
        }
    }
}