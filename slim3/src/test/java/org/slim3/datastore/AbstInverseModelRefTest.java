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
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.LocalServiceTestCase;

/**
 * @author higa
 * 
 */
public class AbstInverseModelRefTest extends LocalServiceTestCase {

    private Hoge hoge = new Hoge();

    private MyInverseModelRef ref =
        new MyInverseModelRef(Bbb.class, "hogeRef", hoge);

    /**
     * @throws Exception
     */
    @Test
    public void constructor() throws Exception {
        assertThat(ref.mappedPropertyName, is("hogeRef"));
        assertThat(ref.modelClass.getName(), is(Bbb.class.getName()));
        assertThat(ref.modelMeta, is(nullValue()));
        assertThat(ref.owner, is(sameInstance(hoge)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getOwnerKey() throws Exception {
        assertThat(ref.getOwnerKey(), is(nullValue()));
        hoge.setKey(Datastore.createKey(Hoge.class, 1));
        assertThat(ref.getOwnerKey(), is(hoge.getKey()));
    }

    private static class MyInverseModelRef extends
            AbstractInverseModelRef<Bbb, Hoge> {

        private static final long serialVersionUID = 1L;

        /**
         * @param modelClass
         * @param mappedPropertyName
         * @param owner
         * @throws NullPointerException
         */
        public MyInverseModelRef(Class<Bbb> modelClass,
                String mappedPropertyName, Hoge owner)
                throws NullPointerException {
            super(modelClass, mappedPropertyName, owner);
        }
    }
}