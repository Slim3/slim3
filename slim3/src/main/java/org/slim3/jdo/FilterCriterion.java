/*
 * Copyright 2004-2009 the original author or authors.
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
package org.slim3.jdo;

/**
 * A criterion interface for filtering.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public interface FilterCriterion {

    /**
     * Returns the query string.
     * 
     * @param parameterName
     *            the parameter name
     * @return the query string
     */
    String getQueryString(String parameterName);

    /**
     * Returns the parameters.
     * 
     * @return the parameters
     */
    Object[] getParameters();

    /**
     * Determines if the model is accepted.
     * 
     * @param model
     *            the model
     * @return whether the model is accepted
     */
    boolean accept(Object model);
}