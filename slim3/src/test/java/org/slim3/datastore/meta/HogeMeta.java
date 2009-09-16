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
package org.slim3.datastore.meta;

import org.slim3.datastore.AttributeMeta;
import org.slim3.datastore.ModelMeta;
import org.slim3.datastore.model.Hoge;

import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class HogeMeta extends ModelMeta<Hoge> {

    /**
     * 
     */
    public AttributeMeta<Key> key =
        new AttributeMeta<Key>(this, "key", Key.class);

    /**
     * 
     */
    public AttributeMeta<String> aaa =
        new AttributeMeta<String>(this, "aaa", String.class);

    /**
     * 
     */
    public AttributeMeta<byte[]> bbb =
        new AttributeMeta<byte[]>(this, "bbb", byte[].class);

    /**
     * 
     */
    public AttributeMeta<String> ccc =
        new AttributeMeta<String>(this, "ccc", String.class);

    /**
     * 
     */
    public AttributeMeta<Long> version =
        new AttributeMeta<Long>(this, "version", Long.class);

    /**
     * 
     */
    public HogeMeta() {
        super(Hoge.class);
    }
}