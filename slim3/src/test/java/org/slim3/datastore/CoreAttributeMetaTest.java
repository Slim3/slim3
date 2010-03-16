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

/**
 * @author higa
 * 
 */
public class CoreAttributeMetaTest {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    @Test
    public void equal() throws Exception {
        assertThat(meta.myString.equal("a"), is(EqualCriterion.class));
        assertThat(meta.myString.equal(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void notEqual() throws Exception {
        assertThat(meta.myString.notEqual("a"), is(NotEqualCriterion.class));
        assertThat(meta.myString.notEqual(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void lessThan() throws Exception {
        assertThat(meta.myString.lessThan("a"), is(LessThanCriterion.class));
        assertThat(meta.myString.lessThan(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void lessThanOrEqual() throws Exception {
        assertThat(
            meta.myString.lessThanOrEqual("a"),
            is(LessThanOrEqualCriterion.class));
        assertThat(meta.myString.lessThanOrEqual(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void greaterThan() throws Exception {
        assertThat(
            meta.myString.greaterThan("a"),
            is(GreaterThanCriterion.class));
        assertThat(meta.myString.greaterThan(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void greaterThanOrEqual() throws Exception {
        assertThat(
            meta.myString.greaterThanOrEqual("a"),
            is(GreaterThanOrEqualCriterion.class));
        assertThat(meta.myString.greaterThanOrEqual(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void in() throws Exception {
        assertThat(meta.myString.in(Arrays.asList("a")), is(InCriterion.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void inForVarargs() throws Exception {
        assertThat(meta.myString.in("a"), is(InCriterion.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test(expected = NullPointerException.class)
    public void inForNull() throws Exception {
        assertThat(
            meta.myString.in((Iterable<String>) null),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void isNotNull() throws Exception {
        assertThat(meta.myString.isNotNull(), is(IsNotNullCriterion.class));
    }
}