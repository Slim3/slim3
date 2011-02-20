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
 * JSON array reader.
 * 
 * @author Takao Nakaguchi
 *
 * @since 1.0.6
 */
public class JsonArrayReader extends JsonReader {
    /**
     * The constructor.
     * 
     * @param json the JSON string
     * @param modelReader the model reader
     */
    public JsonArrayReader(String json, ModelReader modelReader){
        super(modelReader);
        try{
            this.array = new JSONArray(json);
        } catch(JSONException e){
        }
    }

    /**
     * The constructor.
     * 
     * @param array the JSON array
     * @param modelReader the model reader
     */
    JsonArrayReader(JSONArray array, ModelReader modelReader){
        super(modelReader);
        this.array = array;
    }

    /**
     * Returns the length.
     *
     * @return length
     */
    public int length(){
        if(array == null) return 0;
        return array.length();
    }

    /**
     * Sets the index.
     * 
     * @param index the index
     */
    public void setIndex(int index){
        this.index = index;
    }

    @Override
    public String read(){
        if(array == null) return null;
        return array.optString(index, null);
    }

    @Override
    public String readProperty(String name){
        if(array == null) return null;
        JSONObject o = array.optJSONObject(index);
        if(o == null) return null;
        return o.optString(name, null);
    }

    /**
     * Create the new JSONRootReader on the current index.
     *
     * @return JSONRootReader
     */
    public JsonRootReader newRootReader(){
        JSONObject o = array.optJSONObject(index);
        if(o == null) return null;
        return new JsonRootReader(o, getModelReader());
    }

    private JSONArray array;
    private int index;
}
