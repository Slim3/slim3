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

import org.junit.Test;
import org.slim3.datastore.json.JsonRootReader;
import org.slim3.datastore.json.JsonWriter;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class AbstAttributeMetaTest extends AppEngineTestCase {

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

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructor() throws Exception {
        AbstractAttributeMeta<Hoge, String> attrMeta =
            new AbstractAttributeMeta<Hoge, String>(
                meta,
                "aaa",
                "aaa2",
                String.class) {
            };
        assertThat(attrMeta.modelMeta, is(sameInstance(meta)));
        assertThat(attrMeta.name, is("aaa"));
        assertThat(attrMeta.attributeName, is("aaa2"));
        assertThat(
            attrMeta.attributeClass.getName(),
            is(String.class.getName()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getValue() throws Exception {
        AbstractAttributeMeta<Hoge, String> attrMeta =
            new AbstractAttributeMeta<Hoge, String>(
                meta,
                "myString",
                "myString",
                String.class) {
            };
        Hoge hoge = new Hoge();
        hoge.setKey(Datastore.createKey(Hoge.class, 1));
        hoge.setMyString("aaa");
        assertThat((String) attrMeta.getValue(hoge), is("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void charAt() throws Exception {
        AbstractAttributeMeta<Hoge, String> attrMeta =
            new AbstractAttributeMeta<Hoge, String>(
                meta,
                "myString",
                "myString",
                String.class) {
            };
        assertThat(attrMeta.charAt(3), is("myString".charAt(3)));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void length() throws Exception {
        AbstractAttributeMeta<Hoge, String> attrMeta =
            new AbstractAttributeMeta<Hoge, String>(
                meta,
                "myString",
                "myString",
                String.class) {
            };
        assertThat(attrMeta.length(), is("myString".length()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void subSequence() throws Exception {
        AbstractAttributeMeta<Hoge, String> attrMeta =
            new AbstractAttributeMeta<Hoge, String>(
                meta,
                "myString",
                "myString",
                String.class) {
            };
        assertThat(attrMeta.subSequence(1, 3), is("myString".subSequence(1, 3)));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void testToString() throws Exception {
        AbstractAttributeMeta<Hoge, String> attrMeta =
            new AbstractAttributeMeta<Hoge, String>(
                meta,
                "myString",
                "myString",
                String.class) {
            };
        assertThat(attrMeta.toString(), is("myString"));
    }
}