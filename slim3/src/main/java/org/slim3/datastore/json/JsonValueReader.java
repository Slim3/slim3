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
 * JSON value reader.
 * 
 * @author Takao Nakaguchi
 *
 * @since 1.0.6
 */
public class JsonValueReader extends JsonReader {
    /**
     * The constructor.
     * 
     * @param value the value
     * @param modelReader the model reader
     */
    public JsonValueReader(String value,
            ModelReader modelReader){
        super(modelReader);
        this.value = value;
    }

    @Override
    public String read(){
        return value;
    }

    @Override
    public String readProperty(String name){
        return null;
    }

    private String value;
}
