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

import java.util.List;
import java.util.Map;

import org.slim3.commons.bean.ParameterizedClassDesc;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class ParameterizedClassDescTest extends TestCase {

    List<String> list;

    Map<String, Integer> map;

    String str;

    /**
     * 
     * @throws Exception
     */
    public void testCreate() throws Exception {
        ParameterizedClassDesc pcd = ParameterizedClassDesc.create(getClass()
                .getDeclaredField("list").getGenericType());
        assertNotNull(pcd);
        assertEquals(List.class, pcd.getRawClass());
        assertEquals(1, pcd.getArguments().length);
        assertEquals(String.class, pcd.getArguments()[0].getRawClass());
    }

    /**
     * 
     * @throws Exception
     */
    public void testIsParameterizedForTrue() throws Exception {
        ParameterizedClassDesc pcd = ParameterizedClassDesc.create(getClass()
                .getDeclaredField("list").getGenericType());
        assertTrue(pcd.isParameterized());
    }

    /**
     * 
     * @throws Exception
     */
    public void testIsParameterizedForFalse() throws Exception {
        ParameterizedClassDesc pcd = ParameterizedClassDesc.create(getClass()
                .getDeclaredField("str").getGenericType());
        assertFalse(pcd.isParameterized());
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetElementClassOfCollection() throws Exception {
        ParameterizedClassDesc pcd = ParameterizedClassDesc.create(getClass()
                .getDeclaredField("list").getGenericType());
        assertEquals(String.class, pcd.getElementClassOfCollection());
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetElementClassOfCollectionForNotCollection()
            throws Exception {
        ParameterizedClassDesc pcd = ParameterizedClassDesc.create(getClass()
                .getDeclaredField("str").getGenericType());
        assertNull(pcd.getElementClassOfCollection());
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetKeyClassOfMap() throws Exception {
        ParameterizedClassDesc pcd = ParameterizedClassDesc.create(getClass()
                .getDeclaredField("map").getGenericType());
        assertEquals(String.class, pcd.getKeyClassOfMap());
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetKeyClassOfMapNotMap() throws Exception {
        ParameterizedClassDesc pcd = ParameterizedClassDesc.create(getClass()
                .getDeclaredField("str").getGenericType());
        assertNull(pcd.getKeyClassOfMap());
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetValueClassOfMap() throws Exception {
        ParameterizedClassDesc pcd = ParameterizedClassDesc.create(getClass()
                .getDeclaredField("map").getGenericType());
        assertEquals(Integer.class, pcd.getValueClassOfMap());
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetValueClassOfMapNotMap() throws Exception {
        ParameterizedClassDesc pcd = ParameterizedClassDesc.create(getClass()
                .getDeclaredField("str").getGenericType());
        assertNull(pcd.getValueClassOfMap());
    }
}
