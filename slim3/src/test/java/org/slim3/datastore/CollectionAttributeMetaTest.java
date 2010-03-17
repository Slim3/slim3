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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;

/**
 * @author higa
 * 
 */
public class CollectionAttributeMetaTest {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    @Test
    public void equal() throws Exception {
        assertThat(meta.myIntegerList.equal(1), is(EqualCriterion.class));
        assertThat(meta.myIntegerList.equal(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void notEqual() throws Exception {
        assertThat(meta.myIntegerList.notEqual(1), is(NotEqualCriterion.class));
        assertThat(meta.myIntegerList.notEqual(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void lessThan() throws Exception {
        assertThat(meta.myIntegerList.lessThan(1), is(LessThanCriterion.class));
        assertThat(meta.myIntegerList.lessThan(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void lessThanOrEqual() throws Exception {
        assertThat(
            meta.myIntegerList.lessThanOrEqual(1),
            is(LessThanOrEqualCriterion.class));
        assertThat(meta.myIntegerList.lessThanOrEqual(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void greaterThan() throws Exception {
        assertThat(
            meta.myIntegerList.greaterThan(1),
            is(GreaterThanCriterion.class));
        assertThat(meta.myIntegerList.greaterThan(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void greaterThanOrEqual() throws Exception {
        assertThat(
            meta.myIntegerList.greaterThanOrEqual(1),
            is(GreaterThanOrEqualCriterion.class));
        assertThat(
            meta.myIntegerList.greaterThanOrEqual(null),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void in() throws Exception {
        assertThat(
            meta.myIntegerList.in(Arrays.asList(1, 2)),
            is(InCriterion.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void inForVarargs() throws Exception {
        assertThat(meta.myIntegerList.in(1, 2), is(InCriterion.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test(expected = NullPointerException.class)
    public void inForNull() throws Exception {
        assertThat(
            meta.myIntegerList.in((Iterable<Integer>) null),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void isNotNull() throws Exception {
        assertThat(meta.myIntegerList.isNotNull(), is(IsNotNullCriterion.class));
    }
}