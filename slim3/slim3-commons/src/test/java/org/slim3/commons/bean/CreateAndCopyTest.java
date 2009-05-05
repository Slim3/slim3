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
 */
public class CreateAndCopyTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testExecute() throws Exception {
        MyBean src = new MyBean();
        src.setAaa("aaa");
        MyBean dest = new CreateAndCopy<MyBean>(MyBean.class, src).execute();
        assertEquals("aaa", dest.getAaa());
    }

    /**
     * @throws Exception
     */
    public void testConstructorForDestClassIsNull() throws Exception {
        try {
            new CreateAndCopy<Integer>(null, "");
            fail();
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testConstructorForSrcIsNull() throws Exception {
        try {
            new CreateAndCopy<Integer>(Integer.class, null);
            fail();
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testExecuteForBeanToMap() throws Exception {
        MyBean src = new MyBean();
        src.setAaa("aaa");
        BeanMap dest = new CreateAndCopy<BeanMap>(BeanMap.class, src).execute();
        assertEquals("aaa", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testExecuteForMapToBean() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        MyBean dest = new CreateAndCopy<MyBean>(MyBean.class, src).execute();
        assertEquals("aaa", dest.getAaa());
    }

    /**
     * @throws Exception
     */
    public void testExecuteForMapToMap() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        BeanMap dest = new CreateAndCopy<BeanMap>(BeanMap.class, src).execute();
        assertEquals("aaa", dest.get("aaa"));
    }
}