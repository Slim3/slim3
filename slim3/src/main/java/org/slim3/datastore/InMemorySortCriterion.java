/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.slim3.datastore;

/**
 * A criterion interface for in-memory sort.
 * 
 * @author higa
 * @since 1.0.1
 * 
 */
public interface InMemorySortCriterion {

    /**
     * Compares its two arguments for order. Returns a negative integer, zero,
     * or a positive integer as the first argument is less than, equal to, or
     * greater than the second.
     * 
     * @param model1
     *            the first model
     * @param model2
     *            the second model
     * @return the compared result
     * @throws IllegalStateException
     *             if the model is embedded or if the attribute is not
     *             comparable
     */
    int compare(Object model1, Object model2) throws IllegalStateException;

}