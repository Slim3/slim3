package com.google.appengine.api.datastore;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public final class Key_CustomFieldSerializer {

    public static void deserialize(SerializationStreamReader streamReader,
            Key instance) throws SerializationException {
    }

    public static Key instantiate(SerializationStreamReader streamReader)
            throws SerializationException {
        String kind = streamReader.readString();
        Key parentKey = (Key) streamReader.readObject();
        long id = streamReader.readLong();
        String name = streamReader.readString();
        AppIdNamespace appIdNamespace =
            (AppIdNamespace) streamReader.readObject();
        return new Key(kind, parentKey, id, name, appIdNamespace);
    }

    public static void serialize(SerializationStreamWriter streamWriter,
            Key instance) throws SerializationException {
        streamWriter.writeString(instance.getKind());
        streamWriter.writeObject(instance.getParent());
        streamWriter.writeLong(instance.getId());
        streamWriter.writeString(instance.getName());
        streamWriter.writeObject(instance.getAppIdNamespace());
    }
}