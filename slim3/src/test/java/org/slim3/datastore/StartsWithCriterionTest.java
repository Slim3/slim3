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

import java.util.List;

import org.slim3.datastore.meta.HogeMeta;
import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/**
 * @author higa
 * 
 */
public class StartsWithCriterionTest extends DatastoreTestCase {

    /**
     * @throws Exception
     * 
     */
    public void testConstructor() throws Exception {
        HogeMeta meta = new HogeMeta();
        StartsWithCriterion c = new StartsWithCriterion(meta.myString, "aaa");
        assertEquals("aaa", c.value);
    }

    /**
     * @throws Exception
     * 
     */
    public void testApply() throws Exception {
        HogeMeta meta = new HogeMeta();
        Query query = new Query();
        StartsWithCriterion c = new StartsWithCriterion(meta.myString, "aaa");
        c.apply(query);
        List<FilterPredicate> predicates = query.getFilterPredicates();
        assertEquals(2, predicates.size());
        assertEquals("myString", predicates.get(0).getPropertyName());
        assertEquals(FilterOperator.GREATER_THAN_OR_EQUAL, predicates
            .get(0)
            .getOperator());
        assertEquals("aaa", predicates.get(0).getValue());
        assertEquals("myString", predicates.get(1).getPropertyName());
        assertEquals(FilterOperator.LESS_THAN, predicates.get(1).getOperator());
        assertEquals("aaa" + "\ufffd", predicates.get(1).getValue());
    }
}