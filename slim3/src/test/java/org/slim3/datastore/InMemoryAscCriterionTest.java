/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class InMemoryAscCriterionTest extends AppEngineTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    @Test
    public void compare() throws Exception {
        InMemoryAscCriterion c = new InMemoryAscCriterion(meta.myString);
        assertThat(c.compare(new Hoge(), new Hoge()), is(0));
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        assertThat(c.compare(new Hoge(), hoge), is(-1));
        assertThat(c.compare(hoge, new Hoge()), is(1));
        Hoge hoge2 = new Hoge();
        hoge2.setMyString("bbb");
        assertThat(c.compare(hoge, hoge2), is(-1));
        assertThat(c.compare(hoge2, hoge), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void compareForEnum() throws Exception {
        InMemoryAscCriterion c = new InMemoryAscCriterion(meta.myEnum);
        assertThat(c.compare(new Hoge(), new Hoge()), is(0));
        Hoge hoge = new Hoge();
        hoge.setMyEnum(SortDirection.ASCENDING);
        assertThat(c.compare(new Hoge(), hoge), is(-1));
        assertThat(c.compare(hoge, new Hoge()), is(1));
        Hoge hoge2 = new Hoge();
        hoge2.setMyEnum(SortDirection.DESCENDING);
        assertThat(c.compare(hoge, hoge2) < 0, is(true));
        assertThat(c.compare(hoge2, hoge) > 0, is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSmallestValue() throws Exception {
        InMemoryAscCriterion c = new InMemoryAscCriterion(meta.myIntegerList);
        assertThat(c.getSmallestValue(new ArrayList<Object>()), is(nullValue()));
        assertThat((Integer) c.getSmallestValue(Arrays.asList(1)), is(1));
        assertThat((Integer) c.getSmallestValue(Arrays.asList(9, 1, 3)), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void compareForCollection() throws Exception {
        InMemoryAscCriterion c = new InMemoryAscCriterion(meta.myIntegerList);
        Hoge hoge = new Hoge();
        hoge.setMyIntegerList(new ArrayList<Integer>());
        Hoge hoge2 = new Hoge();
        hoge2.setMyIntegerList(new ArrayList<Integer>());
        assertThat(c.compare(hoge, hoge2), is(0));

        hoge.getMyIntegerList().add(1);
        assertThat(c.compare(hoge, hoge2), is(1));
        assertThat(c.compare(hoge2, hoge), is(-1));

        hoge2.getMyIntegerList().add(2);
        assertThat(c.compare(hoge, hoge2), is(-1));
        assertThat(c.compare(hoge2, hoge), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToString() throws Exception {
        InMemoryAscCriterion c = new InMemoryAscCriterion(meta.myString);
        assertThat(c.toString(), is("myString asc"));
    }
}