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

import javax.jdo.PersistenceManager;

import org.slim3.jdo.PMF;
import org.slim3.jdo.SelectQuery;
import org.slim3.tester.DatastoreTestCase;

import slim3.it.model.Sample2;
import slim3.it.model.Sample2Meta;

/**
 * @author higa
 * 
 */
public class SelectQueryTest extends DatastoreTestCase {

    private PersistenceManager pm;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        pm = PMF.get().getPersistenceManager();
    }

    @Override
    public void tearDown() throws Exception {
        pm.close();
        pm = null;
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void testWhere() throws Exception {
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
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
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
        query.where();
        assertNull(query.getFilter());
        assertNull(query.getParametersDeclaration());
        assertEquals(0, query.getParameters().length);
    }

    /**
     * @throws Exception
     */
    public void testNoFilterCriteria() throws Exception {
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
        assertNull(query.getFilter());
        assertNull(query.getParametersDeclaration());
        assertEquals(0, query.getParameters().length);
    }

    /**
     * @throws Exception
     */
    public void testSingleFilterCriteria() throws Exception {
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
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
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
        query.where(s.id.eq(Long.valueOf(1)), s.name.eq("hoge"));
        assertEquals("id == idParam && name == nameParam", query.getFilter());
        assertEquals(
            "java.lang.Long idParam, java.lang.String nameParam",
            query.getParametersDeclaration());
        assertEquals(2, query.getParameters().length);
        assertEquals(Long.valueOf(1), query.getParameters()[0]);
        assertEquals("hoge", query.getParameters()[1]);
    }

    /**
     * @throws Exception
     */
    public void testOrderBy() throws Exception {
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
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
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
        assertSame(query, query.orderBy());
        assertNull(query.getOrdering());
    }

    /**
     * @throws Exception
     */
    public void testNoOrderCriterion() throws Exception {
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
        assertNull(query.getOrdering());
    }

    /**
     * @throws Exception
     */
    public void testSingleOrderCriteria() throws Exception {
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
        query.orderBy(s.id.asc());
        assertEquals("id asc", query.getOrdering());
    }

    /**
     * @throws Exception
     */
    public void testMultiOrderCriteria() throws Exception {
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
        query.orderBy(s.id.asc(), s.name.desc());
        assertEquals("id asc, name desc", query.getOrdering());
    }

    /**
     * @throws Exception
     */
    public void testRange() throws Exception {
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
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
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
        assertEquals(-1, query.getTimeoutMillis());
        assertSame(query, query.timeoutMillis(1000));
        assertEquals(1000, query.getTimeoutMillis());
    }

    /**
     * @throws Exception
     */
    public void testNewQuery() throws Exception {
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
        assertNotNull(query.newQuery());
    }

    /**
     * @throws Exception
     */
    public void testGetQueryString() throws Exception {
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
        query.where(s.id.eq(Long.valueOf(1))).orderBy(s.id.asc()).range(0, 1);
        String queryStr = query.getQueryString();
        System.out.println(queryStr);
        assertEquals(
            "select from "
                + Sample2.class.getName()
                + " where id == idParam order by id asc parameters java.lang.Long idParam range 0, 1",
            queryStr);
    }

    /**
     * @throws Exception
     */
    public void testGetQueryStringWithParameters() throws Exception {
        Sample2Meta s = new Sample2Meta();
        SelectQuery<Sample2> query = new SelectQuery<Sample2>(s, pm);
        query.where(s.id.eq(Long.valueOf(1))).orderBy(s.id.asc()).range(0, 1);
        String queryStr = query.getQueryStringWithParameters();
        System.out.println(queryStr);
        assertEquals(
            "select from "
                + Sample2.class.getName()
                + " where id == idParam order by id asc parameters java.lang.Long idParam range 0, 1"
                + " with [1]",
            queryStr);
    }
}