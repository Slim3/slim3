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
package org.slim3.memcache;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.memcache.MemcacheSerialization;
import com.google.appengine.api.memcache.MemcacheSerialization.ValueAndFlags;

/**
 * @author higa
 * 
 */
public class S3MemcacheSerializationTest extends AppEngineTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void bytes() throws Exception {
        byte[] value = new byte[] { 1 };
        ValueAndFlags valueAndFlags = MemcacheSerialization.serialize(value);
        assertThat((byte[]) S3MemcacheSerialization.deserialize(
            valueAndFlags.value,
            valueAndFlags.flags.ordinal()), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void booleanForTrue() throws Exception {
        Boolean value = true;
        ValueAndFlags valueAndFlags = MemcacheSerialization.serialize(value);
        assertThat((Boolean) S3MemcacheSerialization.deserialize(
            valueAndFlags.value,
            valueAndFlags.flags.ordinal()), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void booleanForFalse() throws Exception {
        Boolean value = false;
        ValueAndFlags valueAndFlags = MemcacheSerialization.serialize(value);
        assertThat((Boolean) S3MemcacheSerialization.deserialize(
            valueAndFlags.value,
            valueAndFlags.flags.ordinal()), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testByte() throws Exception {
        Byte value = 1;
        ValueAndFlags valueAndFlags = MemcacheSerialization.serialize(value);
        assertThat((Byte) S3MemcacheSerialization.deserialize(
            valueAndFlags.value,
            valueAndFlags.flags.ordinal()), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testShort() throws Exception {
        Short value = 1;
        ValueAndFlags valueAndFlags = MemcacheSerialization.serialize(value);
        assertThat((Short) S3MemcacheSerialization.deserialize(
            valueAndFlags.value,
            valueAndFlags.flags.ordinal()), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testInteger() throws Exception {
        Integer value = 1;
        ValueAndFlags valueAndFlags = MemcacheSerialization.serialize(value);
        assertThat((Integer) S3MemcacheSerialization.deserialize(
            valueAndFlags.value,
            valueAndFlags.flags.ordinal()), is(value));
    }
}