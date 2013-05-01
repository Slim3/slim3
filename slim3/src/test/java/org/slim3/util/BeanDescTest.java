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
public class BeanDescTest {

    private BeanDesc beanDesc = BeanDesc.create(MyBean.class);

    /**
     * 
     */
    @SuppressWarnings({ "rawtypes" })
    @Test
    public void propertyDesc() {
        PropertyDesc pd = beanDesc.getPropertyDesc("aaa");
        assertThat(pd, is(notNullValue()));
        assertThat(pd.getName(), is("aaa"));
        assertThat(pd.getPropertyClass(), equalTo((Class) String.class));
        assertThat(pd.getReadMethod(), is(notNullValue()));
        assertThat(pd.getWriteMethod(), is(notNullValue()));
    }

    /**
     * 
     */
    @SuppressWarnings({ "rawtypes" })
    @Test
    public void propertyDescForReadOnly() {
        PropertyDesc pd = beanDesc.getPropertyDesc("bbb");
        assertThat(pd, is(notNullValue()));
        assertThat(pd.getName(), is("bbb"));
        assertThat(pd.getPropertyClass(), equalTo((Class) String.class));
        assertThat(pd.getReadMethod(), is(notNullValue()));
        assertThat(pd.getWriteMethod(), is(nullValue()));
    }

    /**
     * 
     */
    @Test
    public void propertyDescForWriteOnly() {
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
    @Test
    public void propertyDescForBoolean() {
        PropertyDesc pd = beanDesc.getPropertyDesc("ddd");
        assertThat(pd, is(notNullValue()));
        assertThat(pd.getName(), is("ddd"));
        assertThat(pd.getPropertyClass().getName(), is(boolean.class.getName()));
        assertThat(pd.getReadMethod(), is(notNullValue()));
        assertThat(pd.getWriteMethod(), is(nullValue()));
    }

    /**
     * 
     */
    @Test
    public void propertyDescForBooleanWrapper() {
        PropertyDesc pd = beanDesc.getPropertyDesc("eee");
        assertThat(pd, is(notNullValue()));
        assertThat(pd.getName(), is("eee"));
        assertThat(pd.getPropertyClass().getName(), is(Boolean.class.getName()));
        assertThat(pd.getReadMethod(), is(notNullValue()));
        assertThat(pd.getWriteMethod(), is(nullValue()));
    }

    /**
     * 
     */
    @Test
    public void propertyDescForIsBooleanWrapper() {
        assertThat(beanDesc.getPropertyDesc("fff"), is(nullValue()));
    }

    /**
     * 
     */
    @Test
    public void propertyDescForIllegalProperty() {
        assertThat(beanDesc.getPropertyDesc("hhh"), is(nullValue()));
    }

    @SuppressWarnings("unused")
    private static class MyBean {
        private String aaa;

        private String bbb;

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