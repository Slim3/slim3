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
import static org.junit.matchers.JUnitMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.LocalServiceTestCase;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class InCriterionTest extends LocalServiceTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void constructor() throws Exception {
        InCriterion c = new InCriterion(meta.myString, Arrays.asList("aaa"));
        assertThat((List<String>) c.value, hasItem("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void constructorForEnum() throws Exception {
        InCriterion c =
            new InCriterion(meta.myEnum, Arrays.asList(SortDirection.ASCENDING));
        assertThat((List<String>) c.value, hasItem("ASCENDING"));
    }

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void apply() throws Exception {
        Query query = new Query();
        InCriterion c =
            new InCriterion(meta.myString, Arrays.asList("aaa", "bbb"));
        c.apply(query);
        List<FilterPredicate> predicates = query.getFilterPredicates();
        assertThat(predicates.size(), is(1));
        assertThat(predicates.get(0).getPropertyName(), is("myString"));
        assertThat(predicates.get(0).getOperator(), is(FilterOperator.IN));
        assertThat((List<String>) predicates.get(0).getValue(), hasItem("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void applyForEnum() throws Exception {
        Query query = new Query();
        InCriterion c =
            new InCriterion(meta.myEnum, Arrays.asList(SortDirection.ASCENDING));
        c.apply(query);
        List<FilterPredicate> predicates = query.getFilterPredicates();
        assertThat(predicates.get(0).getPropertyName(), is("myEnum"));
        assertThat(predicates.get(0).getOperator(), is(FilterOperator.EQUAL));
        assertThat((String) predicates.get(0).getValue(), is("ASCENDING"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void accept() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        FilterCriterion c =
            new InCriterion(meta.myString, Arrays.asList("aaa"));
        assertThat(c.accept(hoge), is(true));
        hoge.setMyString("bbb");
        assertThat(c.accept(hoge), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void acceptForEnum() throws Exception {
        FilterCriterion c =
            new InCriterion(meta.myEnum, Arrays.asList(SortDirection.ASCENDING));
        Hoge hoge = new Hoge();
        hoge.setMyEnum(SortDirection.DESCENDING);
        assertThat(c.accept(hoge), is(false));
        hoge.setMyEnum(SortDirection.ASCENDING);
        assertThat(c.accept(hoge), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void acceptForNull() throws Exception {
        Hoge hoge = new Hoge();
        FilterCriterion c = new InCriterion(meta.myString, null);
        assertThat(c.accept(hoge), is(false));
    }
}