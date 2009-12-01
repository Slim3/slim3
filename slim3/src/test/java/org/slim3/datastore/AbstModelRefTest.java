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
import org.slim3.datastore.model.Aaa;
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.LocalServiceTestCase;
import org.slim3.util.ByteUtil;

/**
 * @author higa
 * 
 */
public class AbstModelRefTest extends LocalServiceTestCase {

    private ModelRef<Hoge> ref = new ModelRef<Hoge>(Hoge.class);

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

    /**
     * @throws Exception
     */
    @Test
    public void validateForKey() throws Exception {
        ref.validate(Datastore.allocateId("Hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void validateForModel() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setKey(Datastore.createKey(Hoge.class, 1));
        ref.validate(hoge);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void validateForModelWhenModelDoesNotHavePrimaryKey()
            throws Exception {
        Hoge hoge = new Hoge();
        ref.validate(hoge);
    }

    /**
     * @throws Exception
     */
    @Test
    public void validateForSubModel() throws Exception {
        ModelRef<Aaa> aaaRef = new ModelRef<Aaa>(Aaa.class);
        aaaRef.validate(Datastore.allocateId(Bbb.class));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void validateForIllegalKey() throws Exception {
        ref.validate(Datastore.allocateId("Foo"));
    }
}