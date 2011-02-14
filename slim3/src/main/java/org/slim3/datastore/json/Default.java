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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelRef;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.IMHandle;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;
import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.IMHandle.Scheme;
import com.google.appengine.api.users.User;
import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.appengine.repackaged.com.google.common.util.Base64DecoderException;

/**
 * The Default JSON encoder.
 * 
 * @author Takao Nakaguchi
 *
 * @since 1.0.6
 */
public class Default implements JsonCoder{
    @Override
    public void encode(JsonWriter writer, Boolean value) {
        writer.writeValue(value);
    }

    @Override
    public void encode(JsonWriter writer, Short value) {
        writer.writeValue(value);
    }

    @Override
    public void encode(JsonWriter writer, Integer value) {
        writer.writeValue(value);
    }

    @Override
    public void encode(JsonWriter writer, Long value) {
        writer.writeValue(value);
    }

    @Override
    public void encode(JsonWriter writer, Float value) {
        writer.writeValue(value);
    }

    @Override
    public void encode(JsonWriter writer, Double value) {
        writer.writeValue(value);
    }

    @Override
    public void encode(JsonWriter writer, String value) {
        writer.writeString(value);
    }

    @Override
    public void encode(JsonWriter writer, Date value) {
        writer.writeValue(value.getTime());
    }

    @Override
    public void encode(JsonWriter writer, Enum<?> value) {
        writer.writeString(value.name());
    }

    @Override
    public void encode(JsonWriter writer, Blob value) {
        if(value != null && value.getBytes() != null){
            writer.writeString(Base64.encode(value.getBytes()));
        } else{
            writer.writeNull();
        }
    }

    @Override
    public void encode(JsonWriter writer, BlobKey value) {
        writer.writeString(value.getKeyString());        
    }

    @Override
    public void encode(JsonWriter writer, Category value) {
        writer.writeString(value.getCategory());
    }

    @Override
    public void encode(JsonWriter writer, Email value) {
        writer.writeString(value.getEmail());
    }

    @Override
    public void encode(JsonWriter writer, GeoPt value) {
        writer.beginObject();
        writer.writeValueProperty("latitude", value.getLatitude());
        writer.writeValueProperty("longitude", value.getLongitude());
        writer.endObject();
    }

    @Override
    public void encode(JsonWriter writer, IMHandle value) {
        writer.beginObject();
        writer.writeStringProperty("address", value.getAddress());
        writer.writeStringProperty("protocol", value.getProtocol());
        writer.endObject();
    }

    @Override
    public void encode(JsonWriter writer, Key value) {
        writer.writeString(KeyFactory.keyToString(value));
    }

    @Override
    public void encode(JsonWriter writer, Link value) {
        writer.writeString(value.getValue());
    }

    @Override
    public void encode(JsonWriter writer, PhoneNumber value) {
        writer.writeString(value.getNumber());
    }

    @Override
    public void encode(JsonWriter writer, PostalAddress value) {
        writer.writeString(value.getAddress());
    }

    @Override
    public void encode(JsonWriter writer, Rating value) {
        writer.writeValue(value.getRating());
    }

    @Override
    public void encode(JsonWriter writer, ShortBlob value) {
        if(value != null && value.getBytes() != null){
            writer.writeString(Base64.encode(value.getBytes()));
        } else{
            writer.writeNull();
        }
    }

    @Override
    public void encode(JsonWriter writer, Text value) {
        if(value != null && value.getValue() != null){
            writer.writeString(value.getValue());
        } else{
            writer.writeNull();
        }
    }

    @Override
    public void encode(JsonWriter writer, User value) {
        writer.beginObject();
        writer.writeStringProperty("authDomain", value.getAuthDomain());
        writer.writeStringProperty("email", value.getEmail());
        if(value.getFederatedIdentity() != null){
            writer.writeStringProperty("federatedIdentity", value.getFederatedIdentity());
        }
        if(value.getUserId() != null){
            writer.writeStringProperty("userId", value.getUserId());
        }
        writer.endObject();
    }

    @Override
    public void encode(JsonWriter writer, ModelRef<?> value, int maxDepth, int currentDepth) {
        Key key = value.getKey();
        if(key != null){
            writer.writeString(Datastore.keyToString(key));
        } else{
            writer.writeNull();
        }
    }

    @Override
    public void encode(JsonWriter writer, Object value) {
    }

    @Override
    public Boolean decode(JsonReader reader, Boolean defaultValue) {
        String text = reader.read();
        if(text != null){
            return Boolean.valueOf(text);
        }
        return defaultValue;
    }

    @Override
    public Short decode(JsonReader reader, Short defaultValue) {
        String text = reader.read();
        if(text != null){
            try{
                return Short.valueOf(text);
            } catch(NumberFormatException e){
            }
        }
        return defaultValue;
    }

    @Override
    public Integer decode(JsonReader reader, Integer defaultValue) {
        String text = reader.read();
        if(text != null){
            try{
                return Integer.valueOf(text);
            } catch(NumberFormatException e){
            }
        }
        return defaultValue;
    }

    @Override
    public Long decode(JsonReader reader, Long defaultValue) {
        String text = reader.read();
        if(text != null){
            try{
                return Long.parseLong(text);
            } catch(NumberFormatException e){
            }
        }
        return defaultValue;
    }

    @Override
    public Float decode(JsonReader reader, Float defaultValue) {
        String text = reader.read();
        if(text != null){
            try{
                return Float.valueOf(text);
            } catch(NumberFormatException e){
            }
        }
        return defaultValue;
    }

    @Override
    public Double decode(JsonReader reader, Double defaultValue) {
        String text = reader.read();
        if(text != null){
            try{
                return Double.valueOf(text);
            } catch(NumberFormatException e){
            }
        }
        return defaultValue;
    }

    @Override
    public String decode(JsonReader reader, String defaultValue) {
        String text = reader.read();
        if(text != null){
            return text;
        }
        return defaultValue;
    }

    @Override
    public Date decode(JsonReader reader, Date defaultValue) {
        String text = reader.read();
        if(text != null){
            try{
                return new Date(Long.parseLong(text));
            } catch(NumberFormatException e){
            }
        }
        return defaultValue;
    }

    @Override
    public <T extends Enum<T>> T decode(JsonReader reader,
            T defaultValue, Class<T> clazz) {
        String text = reader.read();
        if(text != null){
            try{
                return Enum.valueOf(clazz, text);
            } catch(NumberFormatException e){
            }
        }
        return defaultValue;
    }
    
    @Override
    public Blob decode(JsonReader reader, Blob defaultValue) {
        String text = reader.read();
        if(text != null){
            try{
                return new Blob(Base64.decode(text));
            } catch(Base64DecoderException e){
            }
        }
        return defaultValue;
    }
    
    @Override
    public BlobKey decode(JsonReader reader, BlobKey defaultValue) {
        String text = reader.read();
        if(text != null){
            return new BlobKey(text);
        }
        return defaultValue;
    }

    @Override
    public Category decode(JsonReader reader, Category defaultValue) {
        String text = reader.read();
        if(text != null){
            return new Category(text);
        }
        return defaultValue;
    }

    @Override
    public Email decode(JsonReader reader, Email defaultValue) {
        String text = reader.read();
        if(text != null){
            return new Email(text);
        }
        return defaultValue;
    }

    @Override
    public GeoPt decode(JsonReader reader, GeoPt defaultValue) {
        String latitude = reader.readProperty("latitude");
        String longitude = reader.readProperty("longitude");
        if(latitude != null && longitude != null){
            try{
                return new GeoPt(
                    Float.parseFloat(latitude)
                    , Float.parseFloat(longitude)
                    );
            } catch(NumberFormatException e){
            }
        }
        return defaultValue;
    }

    @Override
    public IMHandle decode(JsonReader reader, IMHandle defaultValue) {
        String address = reader.readProperty("address");
        String protocol = reader.readProperty("protocol");
        if(address != null && protocol != null){
            try{
                return new IMHandle(
                    Scheme.valueOf(protocol)
                    , address);
            } catch(IllegalArgumentException e){
            }
            try{
                return new IMHandle(
                    new URL(protocol)
                    , address);
            } catch(MalformedURLException e){
            }
        }
        return defaultValue;
    }

    @Override
    public Key decode(JsonReader reader, Key defaultValue) {
        String text = reader.read();
        if(text != null){
            return KeyFactory.stringToKey(text);
        }
        return defaultValue;
    }

    @Override
    public Link decode(JsonReader reader, Link defaultValue) {
        String text = reader.read();
        if(text != null){
            return new Link(text);
        }
        return defaultValue;
    }

    @Override
    public PhoneNumber decode(JsonReader reader, PhoneNumber defaultValue) {
        String text = reader.read();
        if(text != null){
            return new PhoneNumber(text);
        }
        return defaultValue;
    }

    @Override
    public PostalAddress decode(JsonReader reader, PostalAddress defaultValue) {
        String text = reader.read();
        if(text != null){
            return new PostalAddress(text);
        }
        return defaultValue;
    }

    @Override
    public Rating decode(JsonReader reader, Rating defaultValue) {
        String text = reader.read();
        if(text != null){
            try{
                return new Rating(Integer.parseInt(text));
            } catch(NumberFormatException e){
            }
        }
        return defaultValue;
    }
    
    @Override
    public ShortBlob decode(JsonReader reader, ShortBlob defaultValue) {
        String text = reader.read();
        if(text != null){
            try{
                return new ShortBlob(Base64.decode(text));
            } catch(Base64DecoderException e){
            }
        }
        return defaultValue;
    }
    
    @Override
    public Text decode(JsonReader reader, Text defaultValue) {
        String text = reader.read();
        if(text != null){
            return new Text(text);
        }
        return defaultValue;
    }

    @Override
    public User decode(JsonReader reader, User defaultValue) {
        String authDomain = reader.readProperty("authDomain");
        String email = reader.readProperty("email");
        if(authDomain != null && email != null){
            String userId = reader.readProperty("userId");
            if(userId != null){
                String federatedIdentity = reader.readProperty("federatedIdentity");
                if(federatedIdentity != null){
                    return new User(email, authDomain, userId, federatedIdentity);
                }
                return new User(email, authDomain, userId);
            }
            return new User(email, authDomain);
        }
        return defaultValue;
    }
    
    @Override
    public <T> void decode(JsonReader reader, ModelRef<T> modelRef, int maxDepth, int currentDepth) {
        String text = reader.read();
        if(text != null){
            try{
                modelRef.setKey(KeyFactory.stringToKey(text));
            } catch(IllegalArgumentException e){
                text = reader.readProperty("key");
                if(text != null){
                    try{
                        modelRef.setKey(KeyFactory.stringToKey(text));
                    } catch(IllegalArgumentException e2){
                    }
                }
            }
        }
    }

    @Override
    public <T> T decode(JsonReader reader, T defaultValue, Class<T> clazz) {
        return defaultValue;
    }
}
