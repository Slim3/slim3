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

import org.junit.Before;
import org.junit.Test;
import org.slim3.datastore.meta.BbbMeta;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author higa
 * 
 */
public class ModelRefAttributeMetaTest extends AppEngineTestCase {

    private BbbMeta meta = new BbbMeta();

    private Key key;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        key = KeyFactory.createKey("Hoge", 1);
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void equal() throws Exception {
        assertThat(meta.hogeRef.equal(key), is(EqualCriterion.class));
        assertThat(meta.hogeRef.equal(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void lessThan() throws Exception {
        assertThat(meta.hogeRef.lessThan(key), is(LessThanCriterion.class));
        assertThat(meta.hogeRef.lessThan(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void lessThanOrEqual() throws Exception {
        assertThat(
            meta.hogeRef.lessThanOrEqual(key),
            is(LessThanOrEqualCriterion.class));
        assertThat(meta.hogeRef.lessThanOrEqual(null), is(not(nullValue())));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void greaterThan() throws Exception {
        assertThat(
            meta.hogeRef.greaterThan(key),
            is(GreaterThanCriterion.class));
        assertThat(meta.hogeRef.greaterThan(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void greaterThanOrEqual() throws Exception {
        assertThat(
            meta.hogeRef.greaterThanOrEqual(key),
            is(GreaterThanOrEqualCriterion.class));
        assertThat(meta.hogeRef.greaterThanOrEqual(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void isNotNull() throws Exception {
        assertThat(meta.hogeRef.isNotNull(), is(IsNotNullCriterion.class));
    }
}