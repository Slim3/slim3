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

import java.io.Serializable;

import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * A class to define sort order.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class Sort implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The property name.
     */
    protected String propertyName;

    /**
     * The sort direction.
     */
    protected SortDirection direction;

    /**
     * Constructor.
     */
    protected Sort() {
    }

    /**
     * Constructor. The default direction is ascending.
     * 
     * @param propertyName
     *            the property name
     * @throws NullPointerException
     *             if the propertyName parameter is null
     */
    public Sort(CharSequence propertyName) throws NullPointerException {
        this(propertyName, SortDirection.ASCENDING);
    }

    /**
     * Constructor.
     * 
     * @param propertyName
     *            the property name
     * @param direction
     *            the sort direction
     * @throws NullPointerException
     *             if the propertyName parameter is null or if the direction
     *             parameter is null
     */
    public Sort(CharSequence propertyName, SortDirection direction)
            throws NullPointerException {
        if (propertyName == null) {
            throw new NullPointerException(
                "The propertyName parameter must not be null.");
        }
        if (direction == null) {
            throw new NullPointerException(
                "The direction parameter must not be null.");
        }
        this.propertyName = propertyName.toString();
        this.direction = direction;
    }

    /**
     * Returns the property name.
     * 
     * @return the property name
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Returns the sort direction.
     * 
     * @return the sort direction
     */
    public SortDirection getDirection() {
        return direction;
    }
}