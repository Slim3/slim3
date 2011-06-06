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
package com.google.appengine.api.users;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Custom field serializer for User.
 * 
 * @author galdolber
 * 
 */
public final class User_CustomFieldSerializer {

    /**
     * @param streamReader
     * @param instance
     * @throws SerializationException
     */
    public static void deserialize(SerializationStreamReader streamReader,
            User instance) throws SerializationException {
    }

    /**
     * @param streamReader
     * @return Blob
     * @throws SerializationException
     */
    public static User instantiate(SerializationStreamReader streamReader)
            throws SerializationException {
        String authDomain = streamReader.readString();
        String email = streamReader.readString();
        String federatedIdentity = streamReader.readString();
        String userId = streamReader.readString();
        if (federatedIdentity != null) {
            return new User(email, authDomain, userId, federatedIdentity);
        }
        return new User(email, authDomain, userId);
    }

    /**
     * @param streamWriter
     * @param instance
     * @throws SerializationException
     */
    public static void serialize(SerializationStreamWriter streamWriter,
            User instance) throws SerializationException {
        streamWriter.writeString(instance.getAuthDomain());
        streamWriter.writeString(instance.getEmail());
        streamWriter.writeString(instance.getFederatedIdentity());
        streamWriter.writeString(instance.getUserId());
    }
}