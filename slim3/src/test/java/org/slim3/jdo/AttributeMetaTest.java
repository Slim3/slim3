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
package org.slim3.jdo;

import org.slim3.jdo.AscCriterion;
import org.slim3.jdo.AttributeMeta;
import org.slim3.jdo.DescCriterion;
import org.slim3.jdo.EqCriterion;
import org.slim3.jdo.GeCriterion;
import org.slim3.jdo.GtCriterion;
import org.slim3.jdo.LeCriterion;
import org.slim3.jdo.LtCriterion;

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
        AttributeMeta<Long> id = new AttributeMeta<Long>("id");
        EqCriterion<Long> criterion = id.eq(Long.valueOf(1));
        assertEquals("idParam", criterion.getParameterName());
        assertEquals("id == idParam", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testEqForNull() throws Exception {
        AttributeMeta<Long> id = new AttributeMeta<Long>("id");
        assertNull(id.eq(null));
    }

    /**
     * @throws Exception
     */
    public void testLt() throws Exception {
        AttributeMeta<Long> id = new AttributeMeta<Long>("id");
        LtCriterion<Long> criterion = id.lt(Long.valueOf(1));
        assertEquals("idLtParam", criterion.getParameterName());
        assertEquals("id < idLtParam", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testLtForNull() throws Exception {
        AttributeMeta<Long> id = new AttributeMeta<Long>("id");
        assertNull(id.lt(null));
    }

    /**
     * @throws Exception
     */
    public void testLe() throws Exception {
        AttributeMeta<Long> id = new AttributeMeta<Long>("id");
        LeCriterion<Long> criterion = id.le(Long.valueOf(1));
        assertEquals("idLeParam", criterion.getParameterName());
        assertEquals("id <= idLeParam", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testLeForNull() throws Exception {
        AttributeMeta<Long> id = new AttributeMeta<Long>("id");
        assertNull(id.le(null));
    }

    /**
     * @throws Exception
     */
    public void testGt() throws Exception {
        AttributeMeta<Long> id = new AttributeMeta<Long>("id");
        GtCriterion<Long> criterion = id.gt(Long.valueOf(1));
        assertEquals("idGtParam", criterion.getParameterName());
        assertEquals("id > idGtParam", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testGtForNull() throws Exception {
        AttributeMeta<Long> id = new AttributeMeta<Long>("id");
        assertNull(id.gt(null));
    }

    /**
     * @throws Exception
     */
    public void testGe() throws Exception {
        AttributeMeta<Long> id = new AttributeMeta<Long>("id");
        GeCriterion<Long> criterion = id.ge(Long.valueOf(1));
        assertEquals("idGeParam", criterion.getParameterName());
        assertEquals("id >= idGeParam", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testGeForNull() throws Exception {
        AttributeMeta<Long> id = new AttributeMeta<Long>("id");
        assertNull(id.ge(null));
    }

    /**
     * @throws Exception
     */
    public void testAsc() throws Exception {
        AttributeMeta<Long> id = new AttributeMeta<Long>("id");
        AscCriterion criterion = id.asc();
        assertNotNull(criterion);
        assertEquals("id asc", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testDesc() throws Exception {
        AttributeMeta<Long> id = new AttributeMeta<Long>("id");
        DescCriterion criterion = id.desc();
        assertNotNull(criterion);
        assertEquals("id desc", criterion.getQueryString());
    }
}
