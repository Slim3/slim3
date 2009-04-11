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
package org.slim3.struts.unit;

import org.slim3.struts.unit.HeaderUtil;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class HeaderUtilTest extends TestCase {

    /**
     * @throws Exception
     * 
     */
    public void testConvertDateToString() throws Exception {
        System.out.println(HeaderUtil.convertDateToString(0));
    }

    /**
     * @throws Exception
     * 
     */
    public void testConvertStringToDate() throws Exception {
        assertEquals(-1, HeaderUtil.convertStringToDate(null));
    }

    /**
     * @throws Exception
     * 
     */
    public void testConvertIntToString() throws Exception {
        assertEquals("0", HeaderUtil.convertIntToString(0));
    }

    /**
     * @throws Exception
     * 
     */
    public void testConvertStringToInt() throws Exception {
        assertEquals(0, HeaderUtil.convertStringToInt("0"));
        assertEquals(-1, HeaderUtil.convertStringToInt(null));
    }
}