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
package org.slim3.datastore;

import com.google.appengine.api.datastore.Entity;

/**
 * An attribute listener interface for receiving put and delete events.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public interface AttributeListener {

    /**
     * This method is invoked before putting the entity.
     * 
     * @param entity
     *            the entity
     * @param attributeMeta
     *            the meta data of attribute
     */
    void prePut(Entity entity, AttributeMeta<?, ?> attributeMeta);

    /**
     * This method is invoked before deleting the entity.
     * 
     * @param entity
     *            the entity
     * @param attributeMeta
     *            the meta data of attribute
     */
    void preDelete(Entity entity, AttributeMeta<?, ?> attributeMeta);
}
