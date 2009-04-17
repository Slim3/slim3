/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.gae.jdo;

/**
 * A meta data for attribute.
 * 
 * @author higa
 * @param <T>
 *            the attribute type
 * @since 3.0
 * 
 */
public class AttributeMeta<T> {

    /**
     * The name.
     */
    protected String name;

    /**
     * Constructor.
     * 
     * @param name
     *            the name
     */
    public AttributeMeta(String name) {
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        this.name = name;
    }

    /**
     * Returns the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the "equals" filter criterion.
     * 
     * @param parameter
     * @return the "equals" filter criterion
     */
    public EqCriterion<T> eq(T parameter) {
        if (parameter == null) {
            return null;
        }
        return new EqCriterion<T>(name, name + "Param", parameter);
    }
}
