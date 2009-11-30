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