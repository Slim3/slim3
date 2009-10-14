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

import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/**
 * @author higa
 * 
 */
public class ContainsNullCriterionTest extends DatastoreTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    public void testApply() throws Exception {
        Query query = new Query();
        ContainsNullCriterion c = new ContainsNullCriterion(meta.myIntegerList);
        c.apply(query);
        List<FilterPredicate> predicates = query.getFilterPredicates();
        assertEquals("myIntegerList", predicates.get(0).getPropertyName());
        assertEquals(FilterOperator.EQUAL, predicates.get(0).getOperator());
        assertEquals(null, predicates.get(0).getValue());
    }

    /**
     * @throws Exception
     */
    public void testAccept() throws Exception {
        Hoge hoge = new Hoge();
        ContainsNullCriterion c = new ContainsNullCriterion(meta.myIntegerList);
        assertFalse(c.accept(hoge));
        hoge.setMyIntegerList(new ArrayList<Integer>());
        assertFalse(c.accept(hoge));
        hoge.getMyIntegerList().add(1);
        assertFalse(c.accept(hoge));
        hoge.getMyIntegerList().add(null);
        assertTrue(c.accept(hoge));
    }
}