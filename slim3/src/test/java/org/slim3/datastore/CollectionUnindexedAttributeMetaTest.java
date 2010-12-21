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
import java.util.List;

import org.junit.Test;
import org.slim3.datastore.json.JsonRootReader;
import org.slim3.datastore.json.JsonWriter;
import org.slim3.datastore.model.Hoge;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class CollectionUnindexedAttributeMetaTest {

    private ModelMeta<Hoge> meta = new ModelMeta<Hoge>("Hoge", Hoge.class) {

        @Override
        protected void setKey(Object model, Key key) {
        }

        @Override
        public Entity modelToEntity(Object model) {
            return null;
        }

        @Override
        protected void incrementVersion(Object model) {
        }

        @Override
        protected void prePut(Object model) {
        }

        @Override
        protected long getVersion(Object model) {
            return 0;
        }

        @Override
        protected Key getKey(Object model) {
            return null;
        }

        @Override
        public Hoge entityToModel(Entity entity) {
            return null;
        }

        @Override
        public String getClassHierarchyListName() {
            return null;
        }

        @Override
        public String getSchemaVersionName() {
            return null;
        }

        @Override
        protected void assignKeyToModelRefIfNecessary(AsyncDatastoreService ds,
                Object model) throws NullPointerException {
        }

        @Override
        protected void modelToJson(JsonWriter writer, Object model, int maxDepth, int currentDepth) {
        }

        @Override
        public Hoge jsonToModel(JsonRootReader reader, int maxDepth,
                int currentDepth) {
            return null;
        }
    };

    private CollectionUnindexedAttributeMeta<Hoge, List<Integer>, Integer> myIntegerList =
        new CollectionUnindexedAttributeMeta<Hoge, List<Integer>, Integer>(
            meta,
            "myIntegerList",
            "myIntegerList",
            List.class);

    /**
     * @throws Exception
     * 
     */
    @Test
    public void equal() throws Exception {
        assertThat(myIntegerList.equal(1), is(InMemoryEqualCriterion.class));
        assertThat(myIntegerList.equal(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void notEqual() throws Exception {
        assertThat(
            myIntegerList.notEqual(1),
            is(InMemoryNotEqualCriterion.class));
        assertThat(myIntegerList.notEqual(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void lessThan() throws Exception {
        assertThat(
            myIntegerList.lessThan(1),
            is(InMemoryLessThanCriterion.class));
        assertThat(myIntegerList.lessThan(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void lessThanOrEqual() throws Exception {
        assertThat(
            myIntegerList.lessThanOrEqual(1),
            is(InMemoryLessThanOrEqualCriterion.class));
        assertThat(myIntegerList.lessThanOrEqual(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void greaterThan() throws Exception {
        assertThat(
            myIntegerList.greaterThan(1),
            is(InMemoryGreaterThanCriterion.class));
        assertThat(myIntegerList.greaterThan(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void greaterThanOrEqual() throws Exception {
        assertThat(
            myIntegerList.greaterThanOrEqual(1),
            is(InMemoryGreaterThanOrEqualCriterion.class));
        assertThat(myIntegerList.greaterThanOrEqual(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void in() throws Exception {
        assertThat(
            myIntegerList.in(Arrays.asList(1, 2)),
            is(InMemoryInCriterion.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void inForVarargs() throws Exception {
        assertThat(myIntegerList.in(1, 2), is(InMemoryInCriterion.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test(expected = NullPointerException.class)
    public void inForNull() throws Exception {
        assertThat(
            myIntegerList.in((Iterable<Integer>) null),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void isNotNull() throws Exception {
        assertThat(
            myIntegerList.isNotNull(),
            is(InMemoryIsNotNullCriterion.class));
    }
}