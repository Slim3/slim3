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

import org.slim3.datastore.ModelRef;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * The Expanded JSON coder.
 * 
 * @author Takao Nakaguchi
 *
 * @since 1.0.6
 */
public class Expanded extends Default{
    @Override
    public void encode(JsonWriter writer, ModelRef<?> value, int maxDepth, int currentDepth) {
        if(currentDepth == maxDepth){
            super.encode(writer, value, maxDepth, currentDepth);
            return;
        }
        Object model = value.getModel();
        if(model != null){
            writer.writeModel(model, maxDepth, currentDepth);
            return;
        }
        Key key = value.getKey();
        if(key != null){
            writer.writeString(KeyFactory.keyToString(key));
            return;
        }
        writer.writeNull();
    }

    @Override
    public <T> void decode(JsonReader reader, ModelRef<T> modelRef, int maxDepth, int currentDepth) {
        if(currentDepth == maxDepth){
            super.decode(reader, modelRef, maxDepth, currentDepth);
            return;
        }
        T model = reader.readModel(modelRef.getModelClass(), maxDepth, currentDepth);
        if(model != null){
            modelRef.setModel(model);
            return;
        }
        super.decode(reader, modelRef, maxDepth, currentDepth);
    }
}
