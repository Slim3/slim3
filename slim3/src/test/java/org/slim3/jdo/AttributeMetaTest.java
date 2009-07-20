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

    private SampleMeta m = new SampleMeta();

    /**
     * @throws Exception
     */
    public void testEq() throws Exception {
        EqCriterion criterion = m.id.eq("1");
        assertEquals("id == :0", criterion.getQueryString(":0"));
        assertEquals(Long.valueOf(1), criterion.getParameters()[0]);
    }

    /**
     * @throws Exception
     */
    public void testEqForNull() throws Exception {
        assertNull(m.id.eq(null));
    }

    /**
     * @throws Exception
     */
    public void testEqForEmpty() throws Exception {
        assertNull(m.name.eq(""));
    }

    /**
     * @throws Exception
     */
    public void testEqForArray() throws Exception {
        Long[] parameter = new Long[] { 1L };
        EqCriterion criterion = m.id.eq(parameter);
        assertEquals("id == :0", criterion.getQueryString(":0"));
        assertEquals(parameter, criterion.getParameters()[0]);
    }

    /**
     * @throws Exception
     */
    public void testEqForCollection() throws Exception {
        List<String> parameter = new ArrayList<String>();
        EqCriterion criterion = m.id.eq(parameter);
        assertEquals("id == :0", criterion.getQueryString(":0"));
        assertEquals(parameter, criterion.getParameters()[0]);
    }

    /**
     * @throws Exception
     */
    public void testEqForEmbedded() throws Exception {
        EqCriterion criterion = m.hoge.name.eq("1");
        assertEquals("hoge.name == :0", criterion.getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testLt() throws Exception {
        LtCriterion criterion = m.id.lt("1");
        assertEquals("id < :0", criterion.getQueryString(":0"));
        assertEquals(Long.valueOf(1), criterion.getParameters()[0]);
    }

    /**
     * @throws Exception
     */
    public void testLtForNull() throws Exception {
        assertNull(m.id.lt(null));
    }

    /**
     * @throws Exception
     */
    public void testLtForEmpty() throws Exception {
        assertNull(m.id.lt(""));
    }

    /**
     * @throws Exception
     */
    public void testLtForEmbedded() throws Exception {
        LtCriterion criterion = m.hoge.name.lt("1");
        assertEquals("hoge.name < :0", criterion.getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testLe() throws Exception {
        LeCriterion criterion = m.id.le("1");
        assertEquals("id <= :0", criterion.getQueryString(":0"));
        assertEquals(Long.valueOf(1), criterion.getParameters()[0]);
    }

    /**
     * @throws Exception
     */
    public void testLeForNull() throws Exception {
        assertNull(m.name.le(null));
    }

    /**
     * @throws Exception
     */
    public void testLeForEmpty() throws Exception {
        assertNull(m.name.le(""));
    }

    /**
     * @throws Exception
     */
    public void testLeForEmbedded() throws Exception {
        LeCriterion criterion = m.hoge.name.le("1");
        assertEquals("hoge.name <= :0", criterion.getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testGt() throws Exception {
        GtCriterion criterion = m.id.gt("1");
        assertEquals("id > :0", criterion.getQueryString(":0"));
        assertEquals(Long.valueOf(1), criterion.getParameters()[0]);
    }

    /**
     * @throws Exception
     */
    public void testGtForNull() throws Exception {
        assertNull(m.id.gt(null));
    }

    /**
     * @throws Exception
     */
    public void testGtForEmpty() throws Exception {
        assertNull(m.id.gt(""));
    }

    /**
     * @throws Exception
     */
    public void testGtForEmbedded() throws Exception {
        GtCriterion criterion = m.hoge.name.gt("1");
        assertEquals("hoge.name > :0", criterion.getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testGe() throws Exception {
        GeCriterion criterion = m.id.ge("1");
        assertEquals("id >= :0", criterion.getQueryString(":0"));
        assertEquals(Long.valueOf(1), criterion.getParameters()[0]);
    }

    /**
     * @throws Exception
     */
    public void testGeForNull() throws Exception {
        assertNull(m.id.ge(null));
    }

    /**
     * @throws Exception
     */
    public void testGeForEmpty() throws Exception {
        assertNull(m.id.ge(""));
    }

    /**
     * @throws Exception
     */
    public void testGeForEmbedded() throws Exception {
        GeCriterion criterion = m.hoge.name.ge("1");
        assertEquals("hoge.name >= :0", criterion.getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testContains() throws Exception {
        ContainsCriterion criterion = m.aaaArray.contains("1");
        assertEquals("aaaArray.contains(:0)", criterion.getQueryString(":0"));
        assertEquals(Long.valueOf(1), criterion.getParameters()[0]);
    }

    /**
     * @throws Exception
     */
    public void testContainsForNull() throws Exception {
        assertNull(m.aaaArray.contains(null));
    }

    /**
     * @throws Exception
     */
    public void testContainsForEmpty() throws Exception {
        assertNull(m.aaaArray.contains(""));
    }

    /**
     * @throws Exception
     */
    public void testContainsForEmbedded() throws Exception {
        ContainsCriterion criterion = m.hoge.aaaArray.contains("1");
        assertEquals("hoge.aaaArray.contains(:0)", criterion
            .getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testAsc() throws Exception {
        AscCriterion criterion = m.id.asc();
        assertNotNull(criterion);
        assertEquals("id asc", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testAscForEmbedded() throws Exception {
        AscCriterion criterion = m.hoge.name.asc();
        assertNotNull(criterion);
        assertEquals("hoge.name asc", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testDesc() throws Exception {
        DescCriterion criterion = m.id.desc();
        assertNotNull(criterion);
        assertEquals("id desc", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testDescForEmbedded() throws Exception {
        DescCriterion criterion = m.hoge.name.desc();
        assertNotNull(criterion);
        assertEquals("hoge.name desc", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testGetValue() throws Exception {
        Sample sample = new Sample();
        sample.setId(1L);
        assertEquals(1L, m.id.getValue(sample));
    }
}