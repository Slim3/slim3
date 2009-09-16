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

import org.slim3.datastore.model.Hoge;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.SortPredicate;

/**
 * @author higa
 * 
 */
public class AttributeMetaTest extends TestCase {

    private ModelMeta<Hoge> hogeMeta = new ModelMeta<Hoge>(Hoge.class);

    private AttributeMeta<String> aaa =
        new AttributeMeta<String>(hogeMeta, "aaa", String.class);

    /**
     * @throws Exception
     */
    public void testEq() throws Exception {
        FilterPredicate filter = aaa.eq("111");
        assertNotNull(filter);
        assertEquals("aaa", filter.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filter.getOperator());
        assertEquals("111", filter.getValue());
    }

    /**
     * @throws Exception
     */
    public void testEqForNull() throws Exception {
        assertNull(aaa.eq(null));
    }

    /**
     * @throws Exception
     */
    public void testAsc() throws Exception {
        SortPredicate sort = aaa.asc();
        assertNotNull(sort);
        assertEquals("aaa", sort.getPropertyName());
        assertEquals(SortDirection.ASCENDING, sort.getDirection());
    }

    /**
     * @throws Exception
     */
    public void testDesc() throws Exception {
        SortPredicate sort = aaa.desc();
        assertNotNull(sort);
        assertEquals("aaa", sort.getPropertyName());
        assertEquals(SortDirection.DESCENDING, sort.getDirection());
    }
}
