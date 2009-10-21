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
import java.util.Arrays;
import java.util.List;

import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.SortPredicate;

/**
 * @author higa
 * 
 */
public class AscCriterionTest extends DatastoreTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    public void testApply() throws Exception {
        Query query = new Query();
        AscCriterion c = new AscCriterion(meta.myString);
        c.apply(query);
        List<SortPredicate> predicates = query.getSortPredicates();
        assertEquals("myString", predicates.get(0).getPropertyName());
        assertEquals(SortDirection.ASCENDING, predicates.get(0).getDirection());
    }

    /**
     * @throws Exception
     */
    public void testCompare() throws Exception {
        AscCriterion c = new AscCriterion(meta.myString);
        assertEquals(0, c.compare(new Hoge(), new Hoge()));
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        assertEquals(-1, c.compare(new Hoge(), hoge));
        assertEquals(1, c.compare(hoge, new Hoge()));
        Hoge hoge2 = new Hoge();
        hoge2.setMyString("bbb");
        assertEquals(-1, c.compare(hoge, hoge2));
        assertEquals(1, c.compare(hoge2, hoge));
    }

    /**
     * @throws Exception
     */
    public void testGetSmallestValue() throws Exception {
        AscCriterion c = new AscCriterion(meta.myIntegerList);
        assertNull(c.getSmallestValue(new ArrayList<Object>()));
        assertEquals(1, c.getSmallestValue(Arrays.asList(1)));
        assertEquals(1, c.getSmallestValue(Arrays.asList(9, 1, 3)));
    }

    /**
     * @throws Exception
     */
    public void testCompareForCollection() throws Exception {
        AscCriterion c = new AscCriterion(meta.myIntegerList);
        Hoge hoge = new Hoge();
        hoge.setMyIntegerList(new ArrayList<Integer>());
        Hoge hoge2 = new Hoge();
        hoge2.setMyIntegerList(new ArrayList<Integer>());
        assertEquals(0, c.compare(hoge, hoge2));

        hoge.getMyIntegerList().add(1);
        assertEquals(1, c.compare(hoge, hoge2));
        assertEquals(-1, c.compare(hoge2, hoge));

        hoge2.getMyIntegerList().add(2);
        assertEquals(-1, c.compare(hoge, hoge2));
        assertEquals(1, c.compare(hoge2, hoge));
    }
}