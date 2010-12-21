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
 * The Model writer.
 * 
 * @author Takao Nakaguchi
 *
 * @since 1.0.6
 */
public interface ModelWriter {
    /**
     * Write the model.
     * @param writer the writer
     * @param model the model
     * @param maxDepth the max depth
     * @param currentDepth the current depth
     */
    void write(JsonWriter writer, Object model, int maxDepth, int currentDepth);
}
