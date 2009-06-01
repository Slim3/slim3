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

import java.util.ArrayList;
import java.util.List;

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
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", Long.class);
        EqCriterion criterion = aaa.eq("1");
        assertEquals("aaa == :0", criterion.getQueryString(":0"));
        assertEquals(Long.valueOf(1), criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testEqForNull() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", Long.class);
        assertNull(aaa.eq(null));
    }

    /**
     * @throws Exception
     */
    public void testEqForEmpty() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", String.class);
        assertNull(aaa.eq(""));
    }

    /**
     * @throws Exception
     */
    public void testEqForArray() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", String.class);
        String[] parameter = new String[] { "111" };
        EqCriterion criterion = aaa.eq(parameter);
        assertEquals("aaa == :0", criterion.getQueryString(":0"));
        assertEquals(parameter, criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testEqForCollection() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", String.class);
        List<String> parameter = new ArrayList<String>();
        EqCriterion criterion = aaa.eq(parameter);
        assertEquals("aaa == :0", criterion.getQueryString(":0"));
        assertEquals(parameter, criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testEqForEmbedded() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta("sample"), "aaa", String.class);
        EqCriterion criterion = aaa.eq("1");
        assertEquals("sample.aaa == :0", criterion.getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testLt() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", Long.class);
        LtCriterion criterion = aaa.lt("1");
        assertEquals("aaa < :0", criterion.getQueryString(":0"));
        assertEquals(Long.valueOf(1), criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testLtForNull() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", Long.class);
        assertNull(aaa.lt(null));
    }

    /**
     * @throws Exception
     */
    public void testLtForEmpty() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", String.class);
        assertNull(aaa.lt(""));
    }

    /**
     * @throws Exception
     */
    public void testLtForEmbedded() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta("sample"), "aaa", Long.class);
        LtCriterion criterion = aaa.lt("1");
        assertEquals("sample.aaa < :0", criterion.getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testLe() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", Long.class);
        LeCriterion criterion = aaa.le("1");
        assertEquals("aaa <= :0", criterion.getQueryString(":0"));
        assertEquals(Long.valueOf(1), criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testLeForNull() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", Long.class);
        assertNull(aaa.le(null));
    }

    /**
     * @throws Exception
     */
    public void testLeForEmpty() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", String.class);
        assertNull(aaa.le(""));
    }

    /**
     * @throws Exception
     */
    public void testLeForEmbedded() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta("sample"), "aaa", Long.class);
        LeCriterion criterion = aaa.le("1");
        assertEquals("sample.aaa <= :0", criterion.getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testGt() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", Long.class);
        GtCriterion criterion = aaa.gt("1");
        assertEquals("aaa > :0", criterion.getQueryString(":0"));
        assertEquals(Long.valueOf(1), criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testGtForNull() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", Long.class);
        assertNull(aaa.gt(null));
    }

    /**
     * @throws Exception
     */
    public void testGtForEmpty() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", String.class);
        assertNull(aaa.gt(""));
    }

    /**
     * @throws Exception
     */
    public void testGtForEmbedded() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta("sample"), "aaa", Long.class);
        GtCriterion criterion = aaa.gt("1");
        assertEquals("sample.aaa > :0", criterion.getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testGe() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", Long.class);
        GeCriterion criterion = aaa.ge("1");
        assertEquals("aaa >= :0", criterion.getQueryString(":0"));
        assertEquals(Long.valueOf(1), criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testGeForNull() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", Long.class);
        assertNull(aaa.ge(null));
    }

    /**
     * @throws Exception
     */
    public void testGeForEmpty() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", String.class);
        assertNull(aaa.ge(""));
    }

    /**
     * @throws Exception
     */
    public void testGeForEmbedded() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta("sample"), "aaa", Long.class);
        GeCriterion criterion = aaa.ge("1");
        assertEquals("sample.aaa >= :0", criterion.getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testContains() throws Exception {
        AttributeMeta aaaArray =
            new AttributeMeta(
                new SampleMeta(),
                "aaaArray",
                Long[].class,
                Long.class);
        ContainsCriterion criterion = aaaArray.contains("1");
        assertEquals("aaaArray.contains(:0)", criterion.getQueryString(":0"));
        assertEquals(Long.valueOf(1), criterion.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testContainsForNull() throws Exception {
        AttributeMeta aaaArray =
            new AttributeMeta(new SampleMeta(), "aaaArray", Long[].class);
        assertNull(aaaArray.contains(null));
    }

    /**
     * @throws Exception
     */
    public void testContainsForEmpty() throws Exception {
        AttributeMeta aaaArray =
            new AttributeMeta(new SampleMeta(), "aaaArray", String[].class);
        assertNull(aaaArray.contains(""));
    }

    /**
     * @throws Exception
     */
    public void testContainsForEmbedded() throws Exception {
        AttributeMeta aaaArray =
            new AttributeMeta(
                new SampleMeta("sample"),
                "aaaArray",
                Long[].class,
                Long.class);
        ContainsCriterion criterion = aaaArray.contains("1");
        assertEquals("sample.aaaArray.contains(:0)", criterion
            .getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testAsc() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", Long.class);
        AscCriterion criterion = aaa.asc();
        assertNotNull(criterion);
        assertEquals("aaa asc", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testAscForEmbedded() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta("sample"), "aaa", Long.class);
        AscCriterion criterion = aaa.asc();
        assertNotNull(criterion);
        assertEquals("sample.aaa asc", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testDesc() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta(), "aaa", Long.class);
        DescCriterion criterion = aaa.desc();
        assertNotNull(criterion);
        assertEquals("aaa desc", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testDescForEmbedded() throws Exception {
        AttributeMeta aaa =
            new AttributeMeta(new SampleMeta("sample"), "aaa", Long.class);
        DescCriterion criterion = aaa.desc();
        assertNotNull(criterion);
        assertEquals("sample.aaa desc", criterion.getQueryString());
    }
}