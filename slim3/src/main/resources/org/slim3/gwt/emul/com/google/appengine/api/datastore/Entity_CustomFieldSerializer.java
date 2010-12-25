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

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.User_CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author galdolber
 * 
 */
public final class Entity_CustomFieldSerializer {

    /**
     * Types.
     */
    public static enum TYPES {
        /**
         * Boolean.
         */
        BOOLEAN,

        /**
         * Byte.
         */
        BYTE,

        /**
         * Char.
         */
        CHAR,

        /**
         * Double.
         */
        DOUBLE,

        /**
         * Float.
         */
        FLOAT,

        /**
         * Integer.
         */
        INT,

        /**
         * Long.
         */
        LONG,

        /**
         * Object.
         */
        OBJECT,

        /**
         * Short.
         */
        SHORT,

        /**
         * String.
         */
        STRING,

        /**
         * Date.
         */
        DATE,

        /**
         * Blob.
         */
        BLOB,

        /**
         * Short blob.
         */
        SHORTBLOB,
        
        /**
         * User.
         */
        USER,
        
        /**
         * Category.
         */
        CATEGORY,
        
        /**
         * Email.
         */
        EMAIL,
        
        /**
         * GeoPt.
         */
        GEOPT,
        
        /**
         * Link.
         */
        LINK,
        
        /**
         * Phone number.
         */
        PHONENUMBER,
        
        /**
         * Postal address.
         */
        POSTALADDRESS,
        
        /**
         * Rating.
         */
        RATING
    }

    /**
     * @param streamReader
     * @param instance
     * @throws SerializationException
     */
    public static void deserialize(SerializationStreamReader streamReader,
            Entity instance) throws SerializationException {
    }

    /**
     * @param streamReader
     * @return entity
     * @throws SerializationException
     */
    public static Entity instantiate(SerializationStreamReader streamReader)
            throws SerializationException {

        // Key
        Entity entity = new Entity((Key) streamReader.readObject());

        // Count
        int propertiesCount = streamReader.readInt();

        for (int n = 0; n < propertiesCount; n++) {
            // Name
            String propertyName = streamReader.readString();

            // Kind
            TYPES typeKind = TYPES.values()[streamReader.readInt()];

            // Value
            Object value = null;
            switch (typeKind) {
            case BOOLEAN:
                value = streamReader.readBoolean();
                break;
            case BYTE:
                value = streamReader.readByte();
                break;
            case CHAR:
                value = streamReader.readChar();
                break;
            case DOUBLE:
                value = streamReader.readDouble();
                break;
            case FLOAT:
                value = streamReader.readFloat();
                break;
            case INT:
                value = streamReader.readInt();
                break;
            case LONG:
                value = streamReader.readLong();
                break;
            case OBJECT:
                value = streamReader.readObject();
                break;
            case SHORT:
                value = streamReader.readShort();
                break;
            case STRING:
                value = streamReader.readString();
                break;
            case DATE:
                value = new Date(streamReader.readLong());
                break;
            case BLOB:
                value = Blob_CustomFieldSerializer.instantiate(streamReader);
                break;
            case SHORTBLOB:
                value = ShortBlob_CustomFieldSerializer.instantiate(streamReader);
                break;
            case USER:
                value = User_CustomFieldSerializer.instantiate(streamReader);
                break;
            case CATEGORY:
                value = Category_CustomFieldSerializer.instantiate(streamReader);
                break;
            case EMAIL:
                value = Email_CustomFieldSerializer.instantiate(streamReader);
                break;
            case GEOPT:
                value = GeoPt_CustomFieldSerializer.instantiate(streamReader);
                break;
            case LINK:
                value = Link_CustomFieldSerializer.instantiate(streamReader);
                break;
            case PHONENUMBER:
                value = PhoneNumber_CustomFieldSerializer.instantiate(streamReader);
                break;
            case POSTALADDRESS:
                value = PostalAddress_CustomFieldSerializer.instantiate(streamReader);
                break;
            case RATING:
                value = Rating_CustomFieldSerializer.instantiate(streamReader);
                break;
            }

            // Unindexed
            if (streamReader.readBoolean()) {
                entity.setUnindexedProperty(propertyName, value);
            } else {
                entity.setProperty(propertyName, value);
            }
        }
        return entity;
    }

    /**
     * @param streamWriter
     * @param instance
     * @throws SerializationException
     */
    public static void serialize(SerializationStreamWriter streamWriter,
            Entity instance) throws SerializationException {

        // Key
        streamWriter.writeObject(instance.getKey());
        Map<String, Object> properties = instance.getProperties();

        // Number of properties
        streamWriter.writeInt(properties.size());

        for (Entry<String, Object> property : properties.entrySet()) {
            String propertyName = property.getKey();

            // Property name
            streamWriter.writeString(propertyName);

            Object value = property.getValue();
            TYPES type = TYPES.OBJECT;
            if (value instanceof Boolean) {
                type = TYPES.BOOLEAN;
            } else if (value instanceof Byte) {
                type = TYPES.BYTE;
            } else if (value instanceof Character) {
                type = TYPES.CHAR;
            } else if (value instanceof Double) {
                type = TYPES.DOUBLE;
            } else if (value instanceof Float) {
                type = TYPES.FLOAT;
            } else if (value instanceof Integer) {
                type = TYPES.INT;
            } else if (value instanceof Long) {
                type = TYPES.LONG;
            } else if (value instanceof Short) {
                type = TYPES.SHORT;
            } else if (value instanceof String) {
                type = TYPES.STRING;
            } else if (value instanceof Date) {
                type = TYPES.DATE;
            } else if (value instanceof Blob) {
                type = TYPES.BLOB;
            } else if (value instanceof ShortBlob) {
                type = TYPES.SHORTBLOB;
            } else if (value instanceof User) {
                type = TYPES.USER;
            } else if (value instanceof Category) {
                type = TYPES.CATEGORY;
            } else if (value instanceof Email) {
                type = TYPES.EMAIL;
            } else if (value instanceof GeoPt) {
                type = TYPES.GEOPT;
            } else if (value instanceof Link) {
                type = TYPES.LINK;
            } else if (value instanceof PhoneNumber) {
                type = TYPES.PHONENUMBER;
            } else if (value instanceof PostalAddress) {
                type = TYPES.POSTALADDRESS;
            } else if (value instanceof Rating) {
                type = TYPES.RATING;
            }

            // Property kind
            streamWriter.writeInt(type.ordinal());

            // Value
            switch (type) {
            case BOOLEAN:
                streamWriter.writeBoolean((Boolean) value);
                break;
            case BYTE:
                streamWriter.writeByte((Byte) value);
                break;
            case CHAR:
                streamWriter.writeChar((Character) value);
                break;
            case DOUBLE:
                streamWriter.writeDouble((Double) value);
                break;
            case FLOAT:
                streamWriter.writeFloat((Float) value);
                break;
            case INT:
                streamWriter.writeInt((Integer) value);
                break;
            case LONG:
                streamWriter.writeLong((Long) value);
                break;
            case OBJECT:
                streamWriter.writeObject(value);
                break;
            case SHORT:
                streamWriter.writeShort((Short) value);
                break;
            case STRING:
                streamWriter.writeString((String) value);
                break;
            case DATE:
                streamWriter.writeLong(((Date) value).getTime());
                break;
            case BLOB:
                Blob_CustomFieldSerializer
                    .serialize(streamWriter, (Blob) value);
                break;
            case SHORTBLOB:
                ShortBlob_CustomFieldSerializer.serialize(
                    streamWriter,
                    (ShortBlob) value);
                break;
            case USER:
                User_CustomFieldSerializer.serialize(streamWriter, (User) value);
                break;
            case CATEGORY:
                Category_CustomFieldSerializer.serialize(streamWriter, (Category) value);
                break;
            case EMAIL:
                Email_CustomFieldSerializer.serialize(streamWriter, (Email) value);
                break;
            case GEOPT:
                GeoPt_CustomFieldSerializer.serialize(streamWriter, (GeoPt) value);
                break;
            case LINK:
                Link_CustomFieldSerializer.serialize(streamWriter, (Link) value);
                break;
            case PHONENUMBER:
                PhoneNumber_CustomFieldSerializer.serialize(streamWriter, (PhoneNumber) value);
                break;
            case POSTALADDRESS:
                PostalAddress_CustomFieldSerializer.serialize(streamWriter, (PostalAddress) value);
                break;
            case RATING:
                Rating_CustomFieldSerializer.serialize(streamWriter, (Rating) value);
                break;
            } 

            // Unindexed
            streamWriter.writeBoolean(instance
                .isUnindexedProperty(propertyName));
        }
    }
}