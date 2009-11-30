package com.google.appengine.api.datastore;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Custom field serializer for {@link AppIdNamespace}.
 * 
 * @author bufferings
 * @author higa
 * @since 3.0
 * 
 */
public final class AppIdNamespace_CustomFieldSerializer {

    /**
     * Deserializes the {@link AppIdNamespace}.
     * 
     * @param streamReader
     *            the stream reader
     * @param instance
     *            the {@link AppIdNamespace}
     * @throws SerializationException
     *             if an exception occurred when deserializing
     */
    public static void deserialize(SerializationStreamReader streamReader,
            AppIdNamespace instance) throws SerializationException {
    }

    /**
     * Instantiates an {@link AppIdNamespace}.
     * 
     * @param streamReader
     *            the stream reader
     * @return an {@link AppIdNamespace}
     * @throws SerializationException
     *             if an exception occurred when instantiating.
     */
    public static AppIdNamespace instantiate(
            SerializationStreamReader streamReader)
            throws SerializationException {
        String appId = streamReader.readString();
        String namespace = streamReader.readString();
        return new AppIdNamespace(appId, namespace);
    }

    /**
     * Serializes the {@link AppIdNamespace}.
     * 
     * @param streamWriter
     *            the stream writer
     * @param instance
     *            the {@link AppIdNamespace}
     * @throws SerializationException
     *             if an exception occurred when serializing
     */
    public static void serialize(SerializationStreamWriter streamWriter,
            AppIdNamespace instance) throws SerializationException {
        streamWriter.writeString(instance.getAppId());
        streamWriter.writeString(instance.getNamespace());
    }
}