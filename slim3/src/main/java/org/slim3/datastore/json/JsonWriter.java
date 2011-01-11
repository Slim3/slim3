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

import java.util.Stack;

import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * The JSON Writer.
 * 
 * @author Takao Nakaguchi
 *
 * @since 1.0.6
 */
public class JsonWriter {
    /**
     * The constructor.
     * @param builder the builder
     * @param modelWriter the model writer
     */
    public JsonWriter(StringBuilder builder, ModelWriter modelWriter){
        this.builder = builder;
        this.modelWriter = modelWriter;
    }

    /**
     * Begins the array mode.
     */
    public void beginArray(){
        checkPropertyName();
        writeCammaForValue();
        contexts.push(new Context(Context.Mode.ARRAY));
        builder.append("[");
    }

    private void checkPropertyName(){
        if(nextPropertyName != null){
            writeCammaForPropertyName();
            builder.append("\"").append(nextPropertyName).append("\":");
            nextPropertyName = null;
        }
    }

    /**
     * Ends the array mode.
     */
    public void endArray(){
        builder.append("]");
        contexts.pop();
    }

    /**
     * Begins the object mode.
     */
    public void beginObject(){
        checkPropertyName();
        writeCammaForValue();
        contexts.push(new Context(Context.Mode.OBJECT));
        builder.append("{");
    }

    /**
     * Ends the object mode.
     */
    public void endObject(){
        builder.append("}");
        contexts.pop();
        nextPropertyName = null;
    }

    /**
     * Sets a next property name.
     * @param name property name
     */
    public void setNextPropertyName(String name){
        this.nextPropertyName = name;
    }

    /**
     * Writes a string property
     * @param name property name
     * @param value value
     */
    public void writeStringProperty(String name, String value) {
        setNextPropertyName(name);
        if(value != null){
            writeString(value);
        } else{
            writeNull();
        }
    }

    /**
     * Writes a value property.
     * @param name property name
     * @param value value
     */
    public void writeValueProperty(String name, Object value) {
        setNextPropertyName(name);
        if(value != null){
            writeValue(value);
        } else{
            writeNull();
        }
    }
    
    /**
     * Writes a string value.
     * @param value value
     */
    public void writeString(String value){
        if(value == null){
            writeNull();
            return;
        }
        checkPropertyName();
        writeCammaForValue();
        builder.append(JSONObject.quote(value));
    }
    
    /**
     * Writes a value.
     * @param value value.
     */
    public void writeValue(Object value){
        if(value == null){
            writeNull();
            return;
        }
        checkPropertyName();
        writeCammaForValue();
        builder.append(value);
    }
    
    /**
     * Writes a null.
     */
    public void writeNull(){
        checkPropertyName();
        writeCammaForValue();
        builder.append("null");
    }

    /**
     * Writes a model.
     * @param model the model
     * @param maxDepth the max depth
     * @param currentDepth the current depth
     */
    public void writeModel(Object model, int maxDepth, int currentDepth){
        modelWriter.write(this, model, maxDepth, currentDepth);
    }

    private void writeCammaForValue(){
        if(contexts.size() > 0){
            contexts.peek().writeForValue(builder);
        }
    }
    private void writeCammaForPropertyName(){
        if(contexts.size() > 0){
            contexts.peek().writeForPropertyName(builder);
        }
    }

    private StringBuilder builder;
    private ModelWriter modelWriter;
    private String nextPropertyName;
    private Stack<Context> contexts = new Stack<Context>();

    static class Context{
        enum Mode{OBJECT, ARRAY}
        Mode mode;
        boolean first = true;

        private Context(Mode mode){
            this.mode = mode;
        }
        private void writeForValue(StringBuilder builder){
            if(!mode.equals(Mode.ARRAY)) return;
            if(first){
                first = false;
            } else {
                builder.append(",");
            }
        }
        private void writeForPropertyName(StringBuilder builder){
            if(!mode.equals(Mode.OBJECT)) return;
            if(first){
                first = false;
            } else {
                builder.append(",");
            }
        }
    }
}
