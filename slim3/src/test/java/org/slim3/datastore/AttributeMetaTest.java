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
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.LocalServiceTestCase;

import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class AttributeMetaTest extends LocalServiceTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructor() throws Exception {
        assertThat((HogeMeta) meta.key.modelMeta, is(sameInstance(meta)));
        assertThat(meta.key.name, is("__key__"));
        assertThat(meta.key.attributeClass.getName(), is(Key.class.getName()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asc() throws Exception {
        assertThat(meta.myString.asc, is(AscCriterion.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void desc() throws Exception {
        assertThat(meta.myString.desc, is(DescCriterion.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getValue() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setKey(Datastore.createKey(Hoge.class, 1));
        hoge.setMyString("aaa");
        assertThat((String) meta.myString.getValue(hoge), is("aaa"));
        assertThat((Key) meta.key.getValue(hoge), is(hoge.getKey()));
    }
}