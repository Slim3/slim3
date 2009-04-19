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
package org.slim3.gae.jdo;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class SelectQueryTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testEmptyCriterion() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s);
        assertSame(query, query.where());
        assertNull(query.getFilter());
        assertNull(query.getParametersDeclaration());
        assertEquals(0, query.getParameters().length);
    }

    /**
     * @throws Exception
     */
    public void testNoCriterion() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s);
        assertNull(query.getFilter());
        assertNull(query.getParametersDeclaration());
        assertEquals(0, query.getParameters().length);
    }

    /**
     * @throws Exception
     */
    public void testSingleCriterion() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s);
        assertSame(query, query.where(s.id.eq(Long.valueOf(1))));
        assertEquals(" where id == idParam", query.getFilter());
        assertEquals("Long idParam", query.getParametersDeclaration());
        assertEquals(1, query.getParameters().length);
        assertEquals(Long.valueOf(1), query.getParameters()[0]);
    }

    /**
     * @throws Exception
     */
    public void testMultiCriteria() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s);
        assertSame(query, query.where(s.id.eq(Long.valueOf(1)), s.name
                .eq("hoge")));
        assertEquals(" where id == idParam && name == nameParam", query
                .getFilter());
        assertEquals("Long idParam, String nameParam", query
                .getParametersDeclaration());
        assertEquals(2, query.getParameters().length);
        assertEquals(Long.valueOf(1), query.getParameters()[0]);
        assertEquals("hoge", query.getParameters()[1]);
    }
}
