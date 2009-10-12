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
package org.slim3.gen;

/**
 * The collection of constants of annotation.
 * 
 * @author taedium
 * @author higa
 * @since 3.0
 * 
 */
public interface AnnotationConstants {

    /** {@code com.google.gwt.user.client.rpc.RemoteServiceRelativePath} */
    String RemoteServiceRelativePath =
        "com.google.gwt.user.client.rpc.RemoteServiceRelativePath";

    /** {@code org.slim3.datastore.Model} */
    String Model = "org.slim3.datastore.Model";

    /** {@code org.slim3.datastore.Attribute} */
    String Attribute = "org.slim3.datastore.Attribute";

    /** {@code org.slim3.datastore.Unindexed} */
    String Unindexed = "org.slim3.datastore.Unindexed";

    /** {@code org.slim3.datastore.Blob} */
    String Blob = "org.slim3.datastore.Blob";

    /** {@code org.slim3.datastore.Text} */
    String Text = "org.slim3.datastore.Text";

    /** {@code org.slim3.datastore.Impermanent} */
    String Impermanent = "org.slim3.datastore.Impermanent";

    /** the name of kind element */
    String kind = "kind";

    /** the name of name element */
    String name = "name";

    /** the name of primaryKey element */
    String primaryKey = "primaryKey";

    /** the name of version element */
    String version = "version";

    /** the name of lob element */
    String lob = "lob";

    /** the name of unindexed element */
    String unindexed = "unindexed";

    /** the name of persistent element */
    String persistent = "persistent";
}
