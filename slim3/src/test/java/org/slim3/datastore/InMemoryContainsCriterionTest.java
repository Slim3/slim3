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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;

/**
 * @author higa
 * 
 */
public class InMemoryContainsCriterionTest extends AppEngineTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructor() throws Exception {
        InMemoryContainsCriterion c = new InMemoryContainsCriterion(meta.myString, "aaa");
        assertThat(c.value, is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void acceptInternal() throws Exception {
        InMemoryContainsCriterion c = new InMemoryContainsCriterion(meta.myString, "bc");
        assertThat(c.acceptInternal("abc"), is(true));
        assertThat(c.acceptInternal(null), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void acceptInternalForNull() throws Exception {
        InMemoryContainsCriterion c = new InMemoryContainsCriterion(meta.myString, null);
        assertThat(c.acceptInternal("abc"), is(false));
        assertThat(c.acceptInternal(null), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void accept() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("abc");
        InMemoryFilterCriterion c = new InMemoryContainsCriterion(meta.myString, "b");
        assertThat(c.accept(hoge), is(true));
        hoge.setMyString("aa");
        assertThat(c.accept(hoge), is(false));
        hoge.setMyString(null);
        assertThat(c.accept(hoge), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void acceptForNull() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("abc");
        InMemoryFilterCriterion c = new InMemoryContainsCriterion(meta.myString, null);
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
        hoge.setMyStringList(Arrays.asList("abc"));
        InMemoryFilterCriterion c =
            new InMemoryContainsCriterion(meta.myStringList, "b");
        assertThat(c.accept(hoge), is(true));
        hoge.setMyStringList(Arrays.asList("aaa"));
        assertThat(c.accept(hoge), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToString() throws Exception {
        InMemoryFilterCriterion c = new InMemoryContainsCriterion(meta.myString, "aaa");
        assertThat(c.toString(), is("myString.contains(aaa)"));
    }
}