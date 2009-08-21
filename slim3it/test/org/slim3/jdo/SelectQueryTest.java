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

import java.util.List;

import javax.jdo.JDOUserException;

import org.slim3.tester.JDOTestCase;

import slim3.it.meta.BlogMeta;
import slim3.it.model.Blog;

/**
 * @author higa
 * 
 */
public class SelectQueryTest extends JDOTestCase {

    private BlogMeta b;

    private SelectQuery<Blog> query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        b = new BlogMeta();
        query = new SelectQuery<Blog>(pm, b.getModelClass());
    }

    /**
     * @throws Exception
     */
    public void testWhere() throws Exception {
        assertNull(query.filterCriteria);
        assertSame(query, query.where());
        assertNotNull(query.filterCriteria);
        query.where(b.key.eq("1"));
        assertEquals(1, query.filterCriteria.length);
    }

    /**
     * @throws Exception
     */
    public void testEmptyFilterCriteria() throws Exception {
        query.where();
        assertNull(query.getFilter());
        assertEquals(0, query.getParameters().length);
    }

    /**
     * @throws Exception
     */
    public void testNoFilterCriteria() throws Exception {
        assertNull(query.getFilter());
        assertEquals(0, query.getParameters().length);
    }

    /**
     * @throws Exception
     */
    public void testSingleFilterCriteria() throws Exception {
        query.where(b.key.eq("aaa"));
        assertEquals("key == :0", query.getFilter());
        assertEquals(1, query.getParameters().length);
        assertEquals("aaa", query.getParameters()[0]);
    }

    /**
     * @throws Exception
     */
    public void testMultiFilterCriteria() throws Exception {
        query.where(b.key.eq("aaa"), b.title.eq("hoge"));
        assertEquals("key == :0 && title == :1", query.getFilter());
        assertEquals(2, query.getParameters().length);
        assertEquals("aaa", query.getParameters()[0]);
        assertEquals("hoge", query.getParameters()[1]);
    }

    /**
     * @throws Exception
     */
    public void testOrderBy() throws Exception {
        assertNull(query.orderCriteria);
        assertSame(query, query.orderBy());
        assertNotNull(query.orderCriteria);
        query.orderBy(b.key.asc());
        assertEquals(1, query.orderCriteria.length);
    }

    /**
     * @throws Exception
     */
    public void testEmptyOrderCriteria() throws Exception {
        assertSame(query, query.orderBy());
        assertNull(query.getOrdering());
    }

    /**
     * @throws Exception
     */
    public void testNoOrderCriterion() throws Exception {
        assertNull(query.getOrdering());
    }

    /**
     * @throws Exception
     */
    public void testSingleOrderCriteria() throws Exception {
        query.orderBy(b.key.asc());
        assertEquals("key asc", query.getOrdering());
    }

    /**
     * @throws Exception
     */
    public void testMultiOrderCriteria() throws Exception {
        query.orderBy(b.key.asc(), b.title.desc());
        assertEquals("key asc, title desc", query.getOrdering());
    }

    /**
     * @throws Exception
     */
    public void testRange() throws Exception {
        assertSame(query, query.range(50, 70));
        assertEquals(50, query.startIndex);
        assertEquals(70, query.endIndex);
    }

    /**
     * @throws Exception
     */
    public void testTimeoutMillis() throws Exception {
        assertSame(query, query.timeoutMillis(1000));
        assertEquals(1000, query.timeoutMillis);
    }

    /**
     * @throws Exception
     */
    public void testNewQuery() throws Exception {
        assertNotNull(query.newQuery());
    }

    /**
     * @throws Exception
     */
    public void testGetQueryString() throws Exception {
        query.where(b.key.eq("aaa")).orderBy(b.key.asc()).range(0, 1);
        String queryStr = query.getQueryString();
        assertEquals("select from "
            + Blog.class.getName()
            + " where key == :0 order by key asc range 0, 1", queryStr);
    }

    /**
     * @throws Exception
     */
    public void testGetQueryStringWithParameters() throws Exception {
        query.where(b.key.eq("aaa")).orderBy(b.key.asc()).range(0, 1);
        String queryStr = query.getQueryStringWithParameters();
        System.out.println(queryStr);
        assertEquals(
            "select from "
                + Blog.class.getName()
                + " where key == :0 order by key asc range 0, 1 with [aaa]",
            queryStr);
    }

    /**
     * @throws Exception
     */
    public void testGetResultList() throws Exception {
        makePersistentInTx(new Blog());
        List<Blog> list = query.getResultList();
        assertEquals(1, list.size());
    }

    /**
     * @throws Exception
     */
    public void testGetSingleResult() throws Exception {
        makePersistentInTx(new Blog());
        Blog blog = query.getSingleResult();
        assertNotNull(blog);
    }

    /**
     * @throws Exception
     */
    public void testGetSingleResultForTooMany() throws Exception {
        makePersistentInTx(new Blog());
        makePersistentInTx(new Blog());
        try {
            query.getSingleResult();
            fail();
        } catch (JDOUserException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testGetFirstResult() throws Exception {
        makePersistentInTx(new Blog());
        makePersistentInTx(new Blog());
        Blog blog = query.getFirstResult();
        assertNotNull(blog);
    }
}