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

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.slim3.util.GenericsUtil;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class GenericsUtilTest extends TestCase {

    List<String> list;

    @SuppressWarnings("unchecked")
    List list2;

    Map<String, String> map;

    Map<String, String>[] maps;

    /**
     * 
     * @throws Exception
     */
    public void testGetRawClass() throws Exception {
        assertEquals(List.class, GenericsUtil.getRawClass(getClass()
            .getDeclaredField("list")
            .getGenericType()));
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetRawClassForArray() throws Exception {
        assertEquals(new Map[0].getClass(), GenericsUtil.getRawClass(getClass()
            .getDeclaredField("maps")
            .getGenericType()));
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetRawClassForNotParameterizedClass() throws Exception {
        assertEquals(List.class, GenericsUtil.getRawClass(getClass()
            .getDeclaredField("list2")
            .getGenericType()));
    }

    /**
     * 
     * @throws Exception
     */
    public void testTypeArguments() throws Exception {
        Type[] arguments =
            GenericsUtil.getTypeArguments(getClass()
                .getDeclaredField("list")
                .getGenericType());
        assertEquals(1, arguments.length);
        assertEquals(String.class, GenericsUtil.getRawClass(arguments[0]));
    }

    /**
     * 
     * @throws Exception
     */
    public void testTypeArguments2() throws Exception {
        Type[] arguments =
            GenericsUtil.getTypeArguments(getClass()
                .getDeclaredField("map")
                .getGenericType());
        assertEquals(2, arguments.length);
        assertEquals(String.class, GenericsUtil.getRawClass(arguments[0]));
        assertEquals(String.class, GenericsUtil.getRawClass(arguments[1]));
    }

    /**
     * 
     * @throws Exception
     */
    public void testTypeArgumentsForArray() throws Exception {
        Type[] arguments =
            GenericsUtil.getTypeArguments(getClass()
                .getDeclaredField("maps")
                .getGenericType());
        assertEquals(2, arguments.length);
        assertEquals(String.class, GenericsUtil.getRawClass(arguments[0]));
        assertEquals(String.class, GenericsUtil.getRawClass(arguments[1]));
    }

    /**
     * 
     * @throws Exception
     */
    public void testTypeArgumentsForNotParameterizedClass() throws Exception {
        Type[] arguments =
            GenericsUtil.getTypeArguments(getClass()
                .getDeclaredField("list2")
                .getGenericType());
        assertNull(arguments);
    }
}
