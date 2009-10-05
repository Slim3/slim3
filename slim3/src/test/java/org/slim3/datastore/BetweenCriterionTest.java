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
import org.slim3.datastore.model.Hoge;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/**
 * @author higa
 * 
 */
public class BetweenCriterionTest extends TestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    public void testConstructor() throws Exception {
        BetweenCriterion c = new BetweenCriterion(meta.myString, "aaa", "ccc");
        assertEquals("aaa", c.start);
        assertEquals("ccc", c.end);
    }

    /**
     * @throws Exception
     * 
     */
    public void testApply() throws Exception {
        Query query = new Query();
        BetweenCriterion c = new BetweenCriterion(meta.myString, "aaa", "ccc");
        c.apply(query);
        List<FilterPredicate> predicates = query.getFilterPredicates();
        assertEquals(2, predicates.size());
        assertEquals("myString", predicates.get(0).getPropertyName());
        assertEquals(FilterOperator.GREATER_THAN_OR_EQUAL, predicates
            .get(0)
            .getOperator());
        assertEquals("aaa", predicates.get(0).getValue());
        assertEquals("myString", predicates.get(1).getPropertyName());
        assertEquals(FilterOperator.LESS_THAN_OR_EQUAL, predicates
            .get(1)
            .getOperator());
        assertEquals("ccc", predicates.get(1).getValue());
    }

    /**
     * @throws Exception
     */
    public void testAccept() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("a");
        FilterCriterion c = meta.myString.between("b", "d");
        assertFalse(c.accept(hoge));
        hoge.setMyString("b");
        assertTrue(c.accept(hoge));
        hoge.setMyString("c");
        assertTrue(c.accept(hoge));
        hoge.setMyString("d");
        assertTrue(c.accept(hoge));
        hoge.setMyString("e");
        assertFalse(c.accept(hoge));
    }
}