package com.google.appengine.api.datastore;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Custom field serializer for {@link Key}.
 * 
 * @author bufferings
 * @author higa
 * @since 3.0
 * 
 */
public final class Key_CustomFieldSerializer {

    /**
     * Deserializes the key.
     * 
     * @param streamReader
     *            the stream reader
     * @param instance
     *            the key
     * @throws SerializationException
     *             if an exception occurred when deserializing
     */
    public static void deserialize(SerializationStreamReader streamReader,
            Key instance) throws SerializationException {
    }

    /**
     * Instantiates a key.
     * 
     * @param streamReader
     *            the stream reader
     * @return a key
     * @throws SerializationException
     *             if an exception occurred when instantiating.
     */
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

    /**
     * Serializes the key.
     * 
     * @param streamWriter
     *            the stream writer
     * @param instance
     *            the key
     * @throws SerializationException
     *             if an exception occurred when serializing
     */
    public static void serialize(SerializationStreamWriter streamWriter,
            Key instance) throws SerializationException {
        streamWriter.writeString(instance.getKind());
        streamWriter.writeObject(instance.getParent());
        streamWriter.writeLong(instance.getId());
        streamWriter.writeString(instance.getName());
        streamWriter.writeObject(instance.getAppIdNamespace());
    }
}