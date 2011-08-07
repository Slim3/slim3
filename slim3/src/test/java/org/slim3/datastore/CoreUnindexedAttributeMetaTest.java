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
public class CoreUnindexedAttributeMetaTest {

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
        
        @Override
        protected void postGet(Object model) {
            return;
        }
    };

    private CoreUnindexedAttributeMeta<Hoge, String> myString =
        new CoreUnindexedAttributeMeta<Hoge, String>(
            meta,
            "myString",
            "myString",
            String.class);

    /**
     * @throws Exception
     * 
     */
    @Test
    public void equal() throws Exception {
        assertThat(myString.equal("a"), is(InMemoryEqualCriterion.class));
        assertThat(myString.equal(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void notEqual() throws Exception {
        assertThat(myString.notEqual("a"), is(InMemoryNotEqualCriterion.class));
        assertThat(myString.notEqual(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void lessThan() throws Exception {
        assertThat(myString.lessThan("a"), is(InMemoryLessThanCriterion.class));
        assertThat(myString.lessThan(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void lessThanOrEqual() throws Exception {
        assertThat(
            myString.lessThanOrEqual("a"),
            is(InMemoryLessThanOrEqualCriterion.class));
        assertThat(myString.lessThanOrEqual(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void greaterThan() throws Exception {
        assertThat(
            myString.greaterThan("a"),
            is(InMemoryGreaterThanCriterion.class));
        assertThat(myString.greaterThan(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void greaterThanOrEqual() throws Exception {
        assertThat(
            myString.greaterThanOrEqual("a"),
            is(InMemoryGreaterThanOrEqualCriterion.class));
        assertThat(myString.greaterThanOrEqual(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void in() throws Exception {
        assertThat(
            myString.in(Arrays.asList("a")),
            is(InMemoryInCriterion.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void inForVarargs() throws Exception {
        assertThat(myString.in("a"), is(InMemoryInCriterion.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test(expected = NullPointerException.class)
    public void inForNull() throws Exception {
        assertThat(myString.in((Iterable<String>) null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void isNotNull() throws Exception {
        assertThat(myString.isNotNull(), is(InMemoryIsNotNullCriterion.class));
    }
}