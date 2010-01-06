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

import java.util.Arrays;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class NotEqualCriterionTest extends AppEngineTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructor() throws Exception {
        NotEqualCriterion c = new NotEqualCriterion(meta.myString, "aaa");
        assertThat((String) c.value, is("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructorForEnum() throws Exception {
        NotEqualCriterion c =
            new NotEqualCriterion(meta.myEnum, SortDirection.ASCENDING);
        assertThat((String) c.value, is("ASCENDING"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getFilters() throws Exception {
        NotEqualCriterion c = new NotEqualCriterion(meta.myString, "aaa");
        Filter[] filters = c.getFilters();
        assertThat(filters.length, is(1));
        assertThat(filters[0].getPropertyName(), is("myString"));
        assertThat(filters[0].getOperator(), is(FilterOperator.NOT_EQUAL));
        assertThat((String) filters[0].getValue(), is("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getFiltersForEnum() throws Exception {
        NotEqualCriterion c =
            new NotEqualCriterion(meta.myEnum, SortDirection.ASCENDING);
        Filter[] filters = c.getFilters();
        assertThat(filters.length, is(1));
        assertThat(filters[0].getPropertyName(), is("myEnum"));
        assertThat(filters[0].getOperator(), is(FilterOperator.NOT_EQUAL));
        assertThat((String) filters[0].getValue(), is("ASCENDING"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getFiltersForNull() throws Exception {
        NotEqualCriterion c = new NotEqualCriterion(meta.myString, null);
        Filter[] filters = c.getFilters();
        assertThat(filters.length, is(1));
        assertThat(filters[0].getPropertyName(), is("myString"));
        assertThat(filters[0].getOperator(), is(FilterOperator.NOT_EQUAL));
        assertThat(filters[0].getValue(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void accept() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        FilterCriterion c = new NotEqualCriterion(meta.myString, "bbb");
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
            new NotEqualCriterion(meta.myEnum, SortDirection.ASCENDING);
        Hoge hoge = new Hoge();
        hoge.setMyEnum(SortDirection.DESCENDING);
        assertThat(c.accept(hoge), is(true));
        hoge.setMyEnum(SortDirection.ASCENDING);
        assertThat(c.accept(hoge), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void acceptForNull() throws Exception {
        Hoge hoge = new Hoge();
        FilterCriterion c = new NotEqualCriterion(meta.myString, null);
        assertThat(c.accept(hoge), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void acceptForCollection() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyIntegerList(Arrays.asList(1));
        FilterCriterion c = new NotEqualCriterion(meta.myIntegerList, 1);
        assertThat(c.accept(hoge), is(false));
        hoge.setMyIntegerList(Arrays.asList(2));
        assertThat(c.accept(hoge), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToString() throws Exception {
        NotEqualCriterion c = new NotEqualCriterion(meta.myString, "aaa");
        assertThat(c.toString(), is("myString != aaa"));
    }
}