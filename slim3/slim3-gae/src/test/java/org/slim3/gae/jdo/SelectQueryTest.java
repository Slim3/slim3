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

import org.slim3.gae.unit.LocalJDOTestCase;

/**
 * @author higa
 * 
 */
public class SelectQueryTest extends LocalJDOTestCase {

    /**
     * @throws Exception
     */
    public void testWhere() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        assertNull(query.getFilterCriteria());
        assertSame(query, query.where());
        assertNotNull(query.getFilterCriteria());
        query.where(s.id.eq(Long.valueOf(1)));
        assertEquals(1, query.getFilterCriteria().length);
    }

    /**
     * @throws Exception
     */
    public void testEmptyFilterCriteria() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        query.where();
        assertNull(query.getFilter());
        assertNull(query.getParametersDeclaration());
        assertEquals(0, query.getParameters().length);
    }

    /**
     * @throws Exception
     */
    public void testNoFilterCriteria() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        assertNull(query.getFilter());
        assertNull(query.getParametersDeclaration());
        assertEquals(0, query.getParameters().length);
    }

    /**
     * @throws Exception
     */
    public void testSingleFilterCriteria() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        query.where(s.id.eq(Long.valueOf(1)));
        assertEquals("id == idParam", query.getFilter());
        assertEquals("java.lang.Long idParam", query.getParametersDeclaration());
        assertEquals(1, query.getParameters().length);
        assertEquals(Long.valueOf(1), query.getParameters()[0]);
    }

    /**
     * @throws Exception
     */
    public void testMultiFilterCriteria() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        query.where(s.id.eq(Long.valueOf(1)), s.name.eq("hoge"));
        assertEquals("id == idParam && name == nameParam", query.getFilter());
        assertEquals("java.lang.Long idParam, java.lang.String nameParam",
                query.getParametersDeclaration());
        assertEquals(2, query.getParameters().length);
        assertEquals(Long.valueOf(1), query.getParameters()[0]);
        assertEquals("hoge", query.getParameters()[1]);
    }

    /**
     * @throws Exception
     */
    public void testOrderBy() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        assertNull(query.getOrderCriteria());
        assertSame(query, query.orderBy());
        assertNotNull(query.getOrderCriteria());
        query.orderBy(s.id.asc());
        assertEquals(1, query.getOrderCriteria().length);
    }

    /**
     * @throws Exception
     */
    public void testEmptyOrderCriteria() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        assertSame(query, query.orderBy());
        assertNull(query.getOrdering());
    }

    /**
     * @throws Exception
     */
    public void testNoOrderCriterion() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        assertNull(query.getOrdering());
    }

    /**
     * @throws Exception
     */
    public void testSingleOrderCriteria() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        query.orderBy(s.id.asc());
        assertEquals("id asc", query.getOrdering());
    }

    /**
     * @throws Exception
     */
    public void testMultiOrderCriteria() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        query.orderBy(s.id.asc(), s.name.desc());
        assertEquals("id asc, name desc", query.getOrdering());
    }

    /**
     * @throws Exception
     */
    public void testRange() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        assertEquals(-1, query.getStartIndex());
        assertEquals(-1, query.getEndIndex());
        assertSame(query, query.range(50, 70));
        assertEquals(50, query.getStartIndex());
        assertEquals(70, query.getEndIndex());
    }

    /**
     * @throws Exception
     */
    public void testTimeoutMillis() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        assertEquals(-1, query.getTimeoutMillis());
        assertSame(query, query.timeoutMillis(1000));
        assertEquals(1000, query.getTimeoutMillis());
    }

    /**
     * @throws Exception
     */
    public void testNewQuery() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        assertNotNull(query.newQuery());
    }

    /**
     * @throws Exception
     */
    public void testGetQueryString() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        query.where(s.id.eq(Long.valueOf(1))).orderBy(s.id.asc()).range(0, 1);
        String queryStr = query.getQueryString();
        System.out.println(queryStr);
        assertEquals(
                "select from "
                        + Sample.class.getName()
                        + " where id == idParam order by id asc parameters java.lang.Long idParam range 0, 1",
                queryStr);
    }

    /**
     * @throws Exception
     */
    public void testGetQueryStringWithParameters() throws Exception {
        SampleMeta s = new SampleMeta();
        SelectQuery<Sample> query = new SelectQuery<Sample>(s, pm);
        query.where(s.id.eq(Long.valueOf(1))).orderBy(s.id.asc()).range(0, 1);
        String queryStr = query.getQueryStringWithParameters();
        System.out.println(queryStr);
        assertEquals(
                "select from "
                        + Sample.class.getName()
                        + " where id == idParam order by id asc parameters java.lang.Long idParam range 0, 1"
                        + " with [1]", queryStr);
    }
}