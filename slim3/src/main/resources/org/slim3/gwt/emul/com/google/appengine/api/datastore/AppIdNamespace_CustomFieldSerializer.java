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

public final class AppIdNamespace_CustomFieldSerializer {

    public static void deserialize(SerializationStreamReader streamReader,
        AppIdNamespace instance) throws SerializationException {
    }

    public static AppIdNamespace instantiate(
        SerializationStreamReader streamReader) throws SerializationException {
        String appId = streamReader.readString();
        String namespace = streamReader.readString();
        return new AppIdNamespace(appId, namespace);
    }

    public static void serialize(SerializationStreamWriter streamWriter,
        AppIdNamespace instance) throws SerializationException {
        streamWriter.writeString(instance.getAppId());
        streamWriter.writeString(instance.getNamespace());
    }
}