/*
 * Copyright 2004-2010 the original author or authors.
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
package org.slim3.datastore.json;

import java.util.Date;

import org.slim3.datastore.ModelRef;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.IMHandle;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;
import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

/**
 * The interface of JSON encoder and decoder.
 * 
 * @author Takao Nakaguchi
 *
 * @since 1.0.6
 */
public interface JsonCoder{
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Boolean value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Short value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Integer value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Long value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Float value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Double value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, String value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Date value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Enum<?> value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Blob value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, BlobKey value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Category value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Email value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, IMHandle value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, GeoPt value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Key value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Link value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, PhoneNumber value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, PostalAddress value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Rating value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, ShortBlob value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Text value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, User value);
    
    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     * @param maxDepth the max depth
     * @param currentDepth the current depth
     */
    void encode(JsonWriter writer, ModelRef<?> value, int maxDepth, int currentDepth);

    /**
     * Encode value to JSON.
     * @param writer the writer
     * @param value the value
     */
    void encode(JsonWriter writer, Object value);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Boolean decode(JsonReader reader, Boolean defaultValue);

    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Short decode(JsonReader reader, Short defaultValue);

    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Integer decode(JsonReader reader, Integer defaultValue);

    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Long decode(JsonReader reader, Long defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Float decode(JsonReader reader, Float defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Double decode(JsonReader reader, Double defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    String decode(JsonReader reader, String defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Date decode(JsonReader reader, Date defaultValue);
    
    /**
     * Decode json object.
     * @param <T> type of Enum
     * @param reader the reader
     * @param defaultValue the default value
     * @param clazz the class
     * @return the decoded Object
     */
    <T extends Enum<T>> T decode(JsonReader reader,
        T defaultValue, Class<T> clazz);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Blob decode(JsonReader reader, Blob defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    BlobKey decode(JsonReader reader, BlobKey defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Category decode(JsonReader reader, Category defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Email decode(JsonReader reader, Email defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    GeoPt decode(JsonReader reader, GeoPt defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    IMHandle decode(JsonReader reader, IMHandle defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Key decode(JsonReader reader, Key defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Link decode(JsonReader reader, Link defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    PhoneNumber decode(JsonReader reader, PhoneNumber defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    PostalAddress decode(JsonReader reader, PostalAddress defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Rating decode(JsonReader reader, Rating defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    ShortBlob decode(JsonReader reader, ShortBlob defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    Text decode(JsonReader reader, Text defaultValue);
    
    /**
     * Decode json object.
     * @param reader the reader
     * @param defaultValue the default value
     * @return the decoded Object
     */
    User decode(JsonReader reader, User defaultValue);
    
    /**
     * Decode json object.
     * @param <T> the type of the referenced model
     * @param reader the reader
     * @param modelRef the model reference
     * @param maxDepth the max depth
     * @param currentDepth the current depth
     */
    <T> void decode(JsonReader reader, ModelRef<T> modelRef, int maxDepth, int currentDepth);
    
    /**
     * Decode json object.
     * @param <T> the type of the object
     * @param reader the reader
     * @param defaultValue the default value
     * @param clazz the class
     * @return the decoded Object
     */
    <T> T decode(JsonReader reader, T defaultValue, Class<T> clazz);
}
