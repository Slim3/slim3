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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.slim3.datastore.json.Default;
import org.slim3.datastore.json.Json;

/**
 * An annotation for property of entity.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Attribute {

    /**
     * The property name. The default value is a field name.
     */
    String name() default "";

    /**
     * Whether this property is persisted.
     */
    boolean persistent() default true;

    /**
     * Whether this property is a key.
     */
    boolean primaryKey() default false;

    /**
     * Whether this property is for version check.
     */
    boolean version() default false;

    /**
     * Whether this property is for a large object.
     */
    boolean lob() default false;

    /**
     * Whether this property is unindexed.
     */
    boolean unindexed() default false;

    /**
     * Whether this property is cipher.
     * 
     * @since 1.0.6
     */
    boolean cipher() default false;

    /**
     * The attribute listener.
     */
    @SuppressWarnings("unchecked")
    Class<? extends AttributeListener> listener() default AttributeListener.class;

    /**
     * The json attribute which controls the json input and output.
     * 
     * @since 1.0.6
     */
    Json json() default @Json(ignore = false, ignoreNull = true, alias = "", coder = Default.class);
}
