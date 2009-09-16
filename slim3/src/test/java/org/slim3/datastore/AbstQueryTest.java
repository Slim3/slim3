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

import org.slim3.datastore.model.Hoge;

import junit.framework.TestCase;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class AbstQueryTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testConstructor() throws Exception {
        MyQuery query = new MyQuery(Hoge.class);
        assertEquals(Hoge.class, query.getModelClass());
        assertNotNull(query.nativeQuery);
        assertEquals("Hoge", query.nativeQuery.getKind());
    }

    /**
     * @throws Exception
     */
    public void testAddFilter() throws Exception {
        MyQuery query = new MyQuery(Hoge.class);
        query.addFilter("aaa", FilterOperator.EQUAL, "111");
        assertEquals(1, query.nativeQuery.getFilterPredicates().size());
    }

    /**
     * @throws Exception
     */
    public void testAddSort() throws Exception {
        MyQuery query = new MyQuery(Hoge.class);
        query.addSort("aaa", SortDirection.DESCENDING);
        assertEquals(1, query.nativeQuery.getSortPredicates().size());
    }

    /**
     * @throws Exception
     */
    public void testGetBeanDesc() throws Exception {
        MyQuery query = new MyQuery(Hoge.class);
        assertNotNull(query.getBeanDesc());
    }

    private static class MyQuery extends AbstractQuery {

        /**
         * @param modelClass
         * @throws NullPointerException
         */
        public MyQuery(Class<?> modelClass) throws NullPointerException {
            super(modelClass);
        }
    }
}
