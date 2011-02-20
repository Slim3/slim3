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

/**
 * JSON Reader.
 * 
 * @author Takao Nakaguchi
 *
 * @since 1.0.6
 */
public abstract class JsonReader {
    private ModelReader modelReader;

    /**
     * The constructor.
     * 
     * @param modelReader the model reader
     */
    public JsonReader(ModelReader modelReader){
        this.modelReader = modelReader;
    }

    /**
     * Read the text.
     * 
     * @return text
     */
    public abstract String read();

    /**
     * Reade the property value of object.
     * 
     * @param name the name
     * @return property value
     */
    public abstract String readProperty(String name);

    /**
     * Get the model reader
     * 
     * @return model reader
     */
    ModelReader getModelReader(){
        return modelReader;
    }

    /**
     * Read the model.
     * 
     * @param <T> the type of model
     * @param clazz the class
     * @param maxDepth the max depth
     * @param currentDepth the current depth
     * @return model
     */
    <T> T readModel(Class<T> clazz, int maxDepth, int currentDepth){
        return modelReader.read(this, clazz, maxDepth, currentDepth);
    }
}
