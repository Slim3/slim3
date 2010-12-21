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
public class SortableAttributeMetaTest extends AppEngineTestCase {

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
    public void asc() throws Exception {
        SortableAttributeMeta<Hoge, String> attrMeta =
            new SortableAttributeMeta<Hoge, String>(
                meta,
                "myString",
                "myString",
                String.class);
        assertThat(attrMeta.asc, is(AscCriterion.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void desc() throws Exception {
        SortableAttributeMeta<Hoge, String> attrMeta =
            new SortableAttributeMeta<Hoge, String>(
                meta,
                "myString",
                "myString",
                String.class);
        assertThat(attrMeta.desc, is(DescCriterion.class));
    }
}