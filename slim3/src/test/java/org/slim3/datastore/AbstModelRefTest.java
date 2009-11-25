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
import org.slim3.datastore.model.Hoge;
import org.slim3.util.ByteUtil;

/**
 * @author higa
 * 
 */
public class AbstModelRefTest {

    private HogeRef ref = new HogeRef();

    /**
     * @throws Exception
     * 
     */
    @Test
    public void serialize() throws Exception {
        byte[] bytes = ByteUtil.toByteArray(ref);
        HogeRef ref2 = ByteUtil.toObject(bytes);
        assertThat(ref2.modelClass, is(nullValue()));
        assertThat(ref2.modelClassName, is(Hoge.class.getName()));
        assertThat(ref2.getModelClass().getName(), is(Hoge.class.getName()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getModelMeta() throws Exception {
        assertThat(ref.getModelMeta(), is(Datastore.getModelMeta(Hoge.class)));
    }

    private static class HogeRef extends AbstractModelRef<Hoge> {

        private static final long serialVersionUID = 1L;

        /**
         *
         */
        public HogeRef() {
            super(Hoge.class);
        }

    }
}