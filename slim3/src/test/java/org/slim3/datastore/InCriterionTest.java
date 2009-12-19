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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.LocalServiceTestCase;

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
        assertThat(c.filterPredicates, is(notNullValue()));
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
    @Test(expected = NullPointerException.class)
    public void constructorForNull() throws Exception {
        new InCriterion(meta.myString, null);
    }

    /**
     * @throws Exception
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorForEmptyParameter() throws Exception {
        new InCriterion(meta.myString, new ArrayList<String>());
    }

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void getFilterPredicates() throws Exception {
        InCriterion c =
            new InCriterion(meta.myString, Arrays.asList("aaa", "bbb"));
        FilterPredicate[] predicates = c.getFilterPredicates();
        assertThat(predicates.length, is(1));
        assertThat(predicates[0].getPropertyName(), is("myString"));
        assertThat(predicates[0].getOperator(), is(FilterOperator.IN));
        assertThat((List<String>) predicates[0].getValue(), hasItems(
            "aaa",
            "bbb"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getFilterPredicatesForEnum() throws Exception {
        InCriterion c =
            new InCriterion(meta.myEnum, Arrays.asList(SortDirection.ASCENDING));
        FilterPredicate[] predicates = c.getFilterPredicates();
        assertThat(predicates.length, is(1));
        assertThat(predicates[0].getPropertyName(), is("myEnum"));
        assertThat(predicates[0].getOperator(), is(FilterOperator.EQUAL));
        assertThat((String) predicates[0].getValue(), is("ASCENDING"));
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
    @Test(expected = NullPointerException.class)
    public void acceptForNull() throws Exception {
        new InCriterion(meta.myString, null);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void acceptForEmptry() throws Exception {
        new InCriterion(meta.myString, new ArrayList<String>());
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToString() throws Exception {
        InCriterion c = new InCriterion(meta.myString, Arrays.asList("aaa"));
        assertThat(c.toString(), is("myString in([aaa])"));
    }
}