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

import junit.framework.TestCase;

import org.slim3.datastore.meta.HogeMeta;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.SortPredicate;

/**
 * @author higa
 * 
 */
public class AttributeMetaTest extends TestCase {

    private HogeMeta h = new HogeMeta();

    /**
     * @throws Exception
     */
    public void testEqual() throws Exception {
        FilterPredicate filter = h.myString.equal("111");
        assertNotNull(filter);
        assertEquals("myString", filter.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filter.getOperator());
        assertEquals("111", filter.getValue());
    }

    /**
     * @throws Exception
     */
    public void testEqualForNull() throws Exception {
        assertNull(h.myString.equal(null));
    }

    /**
     * @throws Exception
     */
    public void testLessThan() throws Exception {
        FilterPredicate filter = h.myString.lessThan("111");
        assertNotNull(filter);
        assertEquals("myString", filter.getPropertyName());
        assertEquals(FilterOperator.LESS_THAN, filter.getOperator());
        assertEquals("111", filter.getValue());
    }

    /**
     * @throws Exception
     */
    public void testLessThanForNull() throws Exception {
        assertNull(h.myString.lessThan(null));
    }

    /**
     * @throws Exception
     */
    public void testAsc() throws Exception {
        SortPredicate sort = h.myString.asc();
        assertNotNull(sort);
        assertEquals("myString", sort.getPropertyName());
        assertEquals(SortDirection.ASCENDING, sort.getDirection());
    }

    /**
     * @throws Exception
     */
    public void testDesc() throws Exception {
        SortPredicate sort = h.myString.desc();
        assertNotNull(sort);
        assertEquals("myString", sort.getPropertyName());
        assertEquals(SortDirection.DESCENDING, sort.getDirection());
    }
}
