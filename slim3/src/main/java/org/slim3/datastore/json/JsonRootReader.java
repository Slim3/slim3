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

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * JSON Reader.
 * 
 * @author Takao Nakaguchi
 *
 * @since 1.0.6
 */
public class JsonRootReader {
    private JSONObject jsonObject;
    private ModelReader modelReader;

    /**
     * The constructor.
     * 
     * @param json the JSON string
     * @param modelReader the model reader
     */
    public JsonRootReader(String json, ModelReader modelReader){
        try{
            this.jsonObject = new JSONObject(json);
        } catch(JSONException e){
        }
        this.modelReader = modelReader;
    }

    /**
     * The constructor.
     * 
     * @param jsonObject the JSON Object
     * @param modelReader the model reader
     */
    JsonRootReader(JSONObject jsonObject, ModelReader modelReader){
        this.jsonObject = jsonObject;
        this.modelReader = modelReader;
    }

    /**
     * Gets the model reader.
     * 
     * @return model reader
     */
    public ModelReader getModelReader(){
        return modelReader;
    }

    /**
     * Create the new JsonObjectReader.
     * 
     * @param propertyName the property name
     * @return JsonObjectReader
     */
    public JsonObjectReader newObjectReader(String propertyName){
        return new JsonObjectReader(jsonObject, propertyName, modelReader);
    }

    /**
     * Creates the new JsonArrayReader.
     * 
     * @param propertyName the property name
     * @return JsonArrayReader
     */
    public JsonArrayReader newArrayReader(String propertyName){
        JSONArray array = jsonObject.optJSONArray(propertyName);
        if(array == null) return null;
        return new JsonArrayReader(array, modelReader);
    }
}
