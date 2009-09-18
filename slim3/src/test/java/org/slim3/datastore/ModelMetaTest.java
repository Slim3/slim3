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

import junit.framework.TestCase;

import org.slim3.datastore.model.Hoge;

import com.google.appengine.api.datastore.Entity;

/**
 * @author higa
 * 
 */
public class ModelMetaTest extends TestCase {

    private HogeMeta hogeMeta = new HogeMeta();

    /**
     * @throws Exception
     */
    public void testConstructor() throws Exception {
        assertEquals(Hoge.class, hogeMeta.getModelClass());
    }

    private static class HogeMeta extends ModelMeta<Hoge> {

        /**
         *
         */
        public HogeMeta() {
            super(Hoge.class);
        }

        @Override
        public Hoge entityToModel(Entity entity) {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
