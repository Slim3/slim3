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
package org.slim3.commons.bean;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class BeanUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetBeanDesc() throws Exception {
        BeanDesc beanDesc = BeanUtil.getBeanDesc(getClass());
        assertNotNull(beanDesc);
        assertSame(beanDesc, BeanUtil.getBeanDesc(getClass()));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBean() throws Exception {
        MyBean src = new MyBean();
        src.setAaa("aaa");
        MyBean dest = new MyBean();
        BeanUtil.copy(src, dest).execute();
        assertEquals("aaa", dest.getAaa());
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMap() throws Exception {
        MyBean src = new MyBean();
        src.setAaa("aaa");
        BeanMap dest = new BeanMap();
        BeanUtil.copy(src, dest).execute();
        assertEquals("aaa", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBean() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        MyBean dest = new MyBean();
        BeanUtil.copy(src, dest).execute();
        assertEquals("aaa", dest.getAaa());
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMap() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        BeanMap dest = new BeanMap();
        BeanUtil.copy(src, dest).execute();
        assertEquals("aaa", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCreateAndCopyBeanToBean() throws Exception {
        MyBean src = new MyBean();
        src.setAaa("aaa");
        MyBean dest = BeanUtil.createAndCopy(MyBean.class, src).execute();
        assertEquals("aaa", dest.getAaa());
    }

    /**
     * @throws Exception
     */
    public void testCreateAndCopyBeanToMap() throws Exception {
        MyBean src = new MyBean();
        src.setAaa("aaa");
        BeanMap dest = BeanUtil.createAndCopy(BeanMap.class, src).execute();
        assertEquals("aaa", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCreateAndCopyMapToBean() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        MyBean dest = BeanUtil.createAndCopy(MyBean.class, src).execute();
        assertEquals("aaa", dest.getAaa());
    }

    /**
     * @throws Exception
     */
    public void testCreateAndCopyMapToMap() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        BeanMap dest = BeanUtil.createAndCopy(BeanMap.class, src).execute();
        assertEquals("aaa", dest.get("aaa"));
    }
}
