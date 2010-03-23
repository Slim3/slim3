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
import static org.junit.Assert.assertThat;

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
public class InMemoryEqualCriterionTest extends AppEngineTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructor() throws Exception {
        InMemoryEqualCriterion c =
            new InMemoryEqualCriterion(meta.myString, "aaa");
        assertThat((String) c.value, is("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructorForEnum() throws Exception {
        InMemoryEqualCriterion c =
            new InMemoryEqualCriterion(meta.myEnum, SortDirection.ASCENDING);
        assertThat((String) c.value, is("ASCENDING"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void accept() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        InMemoryFilterCriterion c =
            new InMemoryEqualCriterion(meta.myString, "aaa");
        assertThat(c.accept(hoge), is(true));
        hoge.setMyString("bbb");
        assertThat(c.accept(hoge), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void acceptForEnum() throws Exception {
        InMemoryFilterCriterion c =
            new InMemoryEqualCriterion(meta.myEnum, SortDirection.ASCENDING);
        Hoge hoge = new Hoge();
        assertThat(c.accept(hoge), is(false));
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
        hoge.setMyString("aaa");
        InMemoryFilterCriterion c =
            new InMemoryEqualCriterion(meta.myString, null);
        assertThat(c.accept(hoge), is(false));
        hoge.setMyString(null);
        assertThat(c.accept(hoge), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void acceptForCollection() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyIntegerList(Arrays.asList(1));
        InMemoryFilterCriterion c =
            new InMemoryEqualCriterion(meta.myIntegerList, 1);
        assertThat(c.accept(hoge), is(true));
        hoge.setMyIntegerList(Arrays.asList(2));
        assertThat(c.accept(hoge), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToString() throws Exception {
        InMemoryEqualCriterion c =
            new InMemoryEqualCriterion(meta.myString, "aaa");
        assertThat(c.toString(), is("myString == aaa"));
    }
}