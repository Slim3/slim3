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

import junit.framework.TestCase;

import org.slim3.datastore.meta.HogeMeta;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/**
 * @author higa
 * 
 */
public class IsNotNullCriterionTest extends TestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    public void testApply() throws Exception {
        Query query = new Query();
        IsNotNullCriterion c = new IsNotNullCriterion(meta.myString);
        c.apply(query);
        List<FilterPredicate> predicates = query.getFilterPredicates();
        assertEquals("myString", predicates.get(0).getPropertyName());
        assertEquals(FilterOperator.GREATER_THAN, predicates
            .get(0)
            .getOperator());
        assertNull(predicates.get(0).getValue());
    }
}