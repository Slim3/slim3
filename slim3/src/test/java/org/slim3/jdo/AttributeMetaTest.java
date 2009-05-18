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
package org.slim3.jdo;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class AttributeMetaTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testEq() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", Long.class);
        EqCriterion criterion = aaa.eq("1");
        assertEquals("aaaParam", criterion.getParameterName());
        assertEquals("aaa == aaaParam", criterion.getQueryString());
        assertEquals(Long.valueOf(1), criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testEqForNull() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", Long.class);
        assertNull(aaa.eq(null));
    }

    /**
     * @throws Exception
     */
    public void testEqForEmpty() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", String.class);
        assertNull(aaa.eq(""));
    }

    /**
     * @throws Exception
     */
    public void testLt() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", Long.class);
        LtCriterion criterion = aaa.lt("1");
        assertEquals("aaaLtParam", criterion.getParameterName());
        assertEquals("aaa < aaaLtParam", criterion.getQueryString());
        assertEquals(Long.valueOf(1), criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testLtForNull() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", Long.class);
        assertNull(aaa.lt(null));
    }

    /**
     * @throws Exception
     */
    public void testLtForEmpty() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", String.class);
        assertNull(aaa.lt(""));
    }

    /**
     * @throws Exception
     */
    public void testLe() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", Long.class);
        LeCriterion criterion = aaa.le("1");
        assertEquals("aaaLeParam", criterion.getParameterName());
        assertEquals("aaa <= aaaLeParam", criterion.getQueryString());
        assertEquals(Long.valueOf(1), criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testLeForNull() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", Long.class);
        assertNull(aaa.le(null));
    }

    /**
     * @throws Exception
     */
    public void testLeForEmpty() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", String.class);
        assertNull(aaa.le(""));
    }

    /**
     * @throws Exception
     */
    public void testGt() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", Long.class);
        GtCriterion criterion = aaa.gt("1");
        assertEquals("aaaGtParam", criterion.getParameterName());
        assertEquals("aaa > aaaGtParam", criterion.getQueryString());
        assertEquals(Long.valueOf(1), criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testGtForNull() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", Long.class);
        assertNull(aaa.gt(null));
    }

    /**
     * @throws Exception
     */
    public void testGtForEmpty() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", String.class);
        assertNull(aaa.gt(""));
    }

    /**
     * @throws Exception
     */
    public void testGe() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", Long.class);
        GeCriterion criterion = aaa.ge("1");
        assertEquals("aaaGeParam", criterion.getParameterName());
        assertEquals("aaa >= aaaGeParam", criterion.getQueryString());
        assertEquals(Long.valueOf(1), criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testGeForNull() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", Long.class);
        assertNull(aaa.ge(null));
    }

    /**
     * @throws Exception
     */
    public void testGeForEmpty() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", String.class);
        assertNull(aaa.ge(""));
    }

    /**
     * @throws Exception
     */
    public void testContains() throws Exception {
        AttributeMeta aaaArray = new AttributeMeta("aaaArray", Long[].class);
        ContainsCriterion criterion = aaaArray.contains("1");
        assertEquals("aaaArrayParam", criterion.getParameterName());
        assertEquals("aaaArray.contains(aaaArrayParam)", criterion
            .getQueryString());
        assertEquals(Long.valueOf(1), criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testContainsForNull() throws Exception {
        AttributeMeta aaaArray = new AttributeMeta("aaaArray", Long[].class);
        assertNull(aaaArray.contains(null));
    }

    /**
     * @throws Exception
     */
    public void testContainsForEmpty() throws Exception {
        AttributeMeta aaaArray = new AttributeMeta("aaaArray", String[].class);
        assertNull(aaaArray.contains(""));
    }

    /**
     * @throws Exception
     */
    public void testAsc() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", Long.class);
        AscCriterion criterion = aaa.asc();
        assertNotNull(criterion);
        assertEquals("aaa asc", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testDesc() throws Exception {
        AttributeMeta aaa = new AttributeMeta("aaa", Long.class);
        DescCriterion criterion = aaa.desc();
        assertNotNull(criterion);
        assertEquals("aaa desc", criterion.getQueryString());
    }
}
