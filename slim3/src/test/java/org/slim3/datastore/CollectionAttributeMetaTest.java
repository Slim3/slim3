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

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;

import com.google.appengine.api.datastore.Query.SortDirection;

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
    public void contains() throws Exception {
        assertThat(meta.myIntegerList.contains(1), is(ContainsCriterion.class));
        assertThat(meta.myIntegerList.contains(null), is(not(nullValue())));
        ContainsCriterion c =
            (ContainsCriterion) meta.myEnumList
                .contains(SortDirection.ASCENDING);
        assertThat((String) c.value, is("ASCENDING"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void equal() throws Exception {
        assertThat(meta.myIntegerList.equal(1), is(EqualCriterion.class));
        assertThat(meta.myIntegerList.equal(null), is(notNullValue()));
        EqualCriterion c =
            (EqualCriterion) meta.myEnumList.equal(SortDirection.ASCENDING);
        assertThat((String) c.value, is("ASCENDING"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void lessThan() throws Exception {
        assertThat(meta.myIntegerList.lessThan(1), is(LessThanCriterion.class));
        assertThat(meta.myIntegerList.lessThan(null), is(notNullValue()));
        LessThanCriterion c =
            (LessThanCriterion) meta.myEnumList
                .lessThan(SortDirection.ASCENDING);
        assertThat((String) c.value, is("ASCENDING"));
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
        LessThanOrEqualCriterion c =
            (LessThanOrEqualCriterion) meta.myEnumList
                .lessThanOrEqual(SortDirection.ASCENDING);
        assertThat((String) c.value, is("ASCENDING"));
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
        GreaterThanCriterion c =
            (GreaterThanCriterion) meta.myEnumList
                .greaterThan(SortDirection.ASCENDING);
        assertThat((String) c.value, is("ASCENDING"));
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
            is(not(nullValue())));
        GreaterThanOrEqualCriterion c =
            (GreaterThanOrEqualCriterion) meta.myEnumList
                .greaterThanOrEqual(SortDirection.ASCENDING);
        assertThat((String) c.value, is("ASCENDING"));
    }

}