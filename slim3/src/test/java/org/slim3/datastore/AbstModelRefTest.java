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
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.ByteUtil;

/**
 * @author higa
 * 
 */
public class AbstModelRefTest extends AppEngineTestCase {

    private ModelRef<Hoge> ref = new ModelRef<Hoge>(Hoge.class);

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructor() throws Exception {
        assertThat(ref.modelClass, is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void serialize() throws Exception {
        byte[] bytes = ByteUtil.toByteArray(ref);
        ModelRef<Hoge> ref2 = ByteUtil.toObject(bytes);
        assertThat(ref2.modelClass, is(nullValue()));
        assertThat(ref2.modelClassName, is(Hoge.class.getName()));
        assertThat(ref2.getModelMeta(), is(notNullValue()));
        assertThat(ref2.getModelClass().getName(), is(Hoge.class.getName()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getModelMeta() throws Exception {
        ModelMeta<Hoge> modelMeta = ref.getModelMeta();
        assertThat(modelMeta, is(notNullValue()));
        assertThat(ref.getModelMeta(), is(sameInstance(modelMeta)));
    }
}