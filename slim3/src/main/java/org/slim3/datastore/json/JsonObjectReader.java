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

import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * JSON object reader.
 * 
 * @author Takao Nakaguchi
 *
 * @since 1.0.6
 */
public class JsonObjectReader extends JsonReader {
    /**
     * The constructor.
     * 
     * @param object the object
     * @param name the name
     * @param modelReader the model reader
     */
    public JsonObjectReader(JSONObject object, String name,
            ModelReader modelReader){
        super(modelReader);
        this.object = object;
        this.name = name;
    }

    @Override
    public String read(){
        return object.optString(name, null);
    }

    @Override
    public String readProperty(String name){
        JSONObject o = object.optJSONObject(this.name);
        if(o == null) return null;
        return o.optString(name, null);
    }

    private JSONObject object;
    private String name;
}
