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
package org.slim3.datastore;

import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class EntityQueryTest extends DatastoreTestCase {

    /**
     * @throws Exception
     */
    public void testFilter() throws Exception {
        EntityQuery q = new EntityQuery("Hoge");
        assertSame(q, q.filter("aaa", FilterOperator.EQUAL, "111"));
        assertEquals(1, q.query.getFilterPredicates().size());
        assertEquals("aaa", q.query
            .getFilterPredicates()
            .get(0)
            .getPropertyName());
        assertEquals(FilterOperator.EQUAL, q.query
            .getFilterPredicates()
            .get(0)
            .getOperator());
        assertEquals("111", q.query.getFilterPredicates().get(0).getValue());
    }

    /**
     * @throws Exception
     */
    public void testSort() throws Exception {
        EntityQuery q = new EntityQuery("Hoge");
        assertSame(q, q.sort("aaa", SortDirection.DESCENDING));
        assertEquals(1, q.query.getSortPredicates().size());
        assertEquals("aaa", q.query
            .getSortPredicates()
            .get(0)
            .getPropertyName());
        assertEquals(SortDirection.DESCENDING, q.query.getSortPredicates().get(
            0).getDirection());
    }
}