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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.LocalServiceTestCase;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.SortPredicate;

/**
 * @author higa
 * 
 */
public class DescCriterionTest extends LocalServiceTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    @Test
    public void apply() throws Exception {
        Query query = new Query();
        DescCriterion c = new DescCriterion(meta.myString);
        c.apply(query);
        List<SortPredicate> predicates = query.getSortPredicates();
        assertThat(predicates.get(0).getPropertyName(), is("myString"));
        assertThat(
            predicates.get(0).getDirection(),
            is(SortDirection.DESCENDING));
    }

    /**
     * @throws Exception
     */
    @Test
    public void compare() throws Exception {
        DescCriterion c = new DescCriterion(meta.myString);
        assertThat(c.compare(new Hoge(), new Hoge()), is(0));
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        assertThat(c.compare(new Hoge(), hoge), is(1));
        assertThat(c.compare(hoge, new Hoge()), is(-1));
        Hoge hoge2 = new Hoge();
        hoge2.setMyString("bbb");
        assertThat(c.compare(hoge, hoge2), is(1));
        assertThat(c.compare(hoge2, hoge), is(-1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void compareForEnum() throws Exception {
        DescCriterion c = new DescCriterion(meta.myEnum);
        assertThat(c.compare(new Hoge(), new Hoge()), is(0));
        Hoge hoge = new Hoge();
        hoge.setMyEnum(SortDirection.ASCENDING);
        assertThat(c.compare(new Hoge(), hoge), is(1));
        assertThat(c.compare(hoge, new Hoge()), is(-1));
        Hoge hoge2 = new Hoge();
        hoge2.setMyEnum(SortDirection.DESCENDING);
        assertThat(c.compare(hoge, hoge2) > 0, is(true));
        assertThat(c.compare(hoge2, hoge) < 0, is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getGreatestValue() throws Exception {
        DescCriterion c = new DescCriterion(meta.myIntegerList);
        assertThat(c.getGreatestValue(new ArrayList<Object>()), is(nullValue()));
        assertThat((Integer) c.getGreatestValue(Arrays.asList(1)), is(1));
        assertThat((Integer) c.getGreatestValue(Arrays.asList(9, 1, 3)), is(9));
    }

    /**
     * @throws Exception
     */
    @Test
    public void compareForCollection() throws Exception {
        DescCriterion c = new DescCriterion(meta.myIntegerList);
        Hoge hoge = new Hoge();
        hoge.setMyIntegerList(new ArrayList<Integer>());
        Hoge hoge2 = new Hoge();
        hoge2.setMyIntegerList(new ArrayList<Integer>());
        assertThat(c.compare(hoge, hoge2), is(0));

        hoge.getMyIntegerList().add(1);
        assertThat(c.compare(hoge, hoge2), is(-1));
        assertThat(c.compare(hoge2, hoge), is(1));

        hoge2.getMyIntegerList().add(2);
        assertThat(c.compare(hoge, hoge2), is(1));
        assertThat(c.compare(hoge2, hoge), is(-1));
    }
}