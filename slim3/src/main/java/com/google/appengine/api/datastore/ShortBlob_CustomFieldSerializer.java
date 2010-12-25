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
package com.google.appengine.api.datastore;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Custom field serializer for ShortBlob.
 * 
 * @author galdolber
 * 
 */
public final class ShortBlob_CustomFieldSerializer {

    /**
     * @param streamReader
     * @param instance
     * @throws SerializationException
     */
    public static void deserialize(SerializationStreamReader streamReader,
            ShortBlob instance) throws SerializationException {
    }

    /**
     * @param streamReader
     * @return Blob
     * @throws SerializationException
     */
    public static ShortBlob instantiate(SerializationStreamReader streamReader)
            throws SerializationException {
        int length = streamReader.readInt();
        byte[] bytes = new byte[length];
        for (int itemIndex = 0; itemIndex < length; ++itemIndex) {
            bytes[itemIndex] = streamReader.readByte();
        }

        return new ShortBlob(bytes);
    }

    /**
     * @param streamWriter
     * @param instance
     * @throws SerializationException
     */
    public static void serialize(SerializationStreamWriter streamWriter,
            ShortBlob instance) throws SerializationException {
        byte[] bytes = instance.getBytes();
        int itemCount = bytes.length;
        streamWriter.writeInt(itemCount);
        for (int itemIndex = 0; itemIndex < itemCount; ++itemIndex) {
            streamWriter.writeByte(bytes[itemIndex]);
        }
    }
}