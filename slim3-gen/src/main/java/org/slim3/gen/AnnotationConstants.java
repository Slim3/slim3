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
 * @author taedium
 * 
 */
public class AnnotationConstants {

    /** {@code javax.jdo.annotations.PersistenceCapable} */
    public static final String PersistenceCapable =
        "javax.jdo.annotations.PersistenceCapable";

    /** {@code javax.jdo.annotations.Persistent} */
    public static final String Persistent = "javax.jdo.annotations.Persistent";

    /** {@code javax.jdo.annotations.NotPersistent} */
    public static final String NotPersistent =
        "javax.jdo.annotations.NotPersistent";

    /** {@code javax.jdo.annotations.Embedded} */
    public static final String Embedded = "javax.jdo.annotations.Embedded";

    /** {@code com.google.gwt.user.client.rpc.RemoteServiceRelativePath} */
    public static final String RemoteServiceRelativePath =
        "com.google.gwt.user.client.rpc.RemoteServiceRelativePath";

    public static final String Blob = "org.slim3.datastore.Blob";
    public static final String Text = "org.slim3.datastore.Text";
    public static final String PrimaryKey = " org.slim3.datastore.PrimaryKey";
    public static final String Version = "org.slim3.datastore.Version";
    public static final String Impermanent = "org.slim3.datastore.Impermanent";
}
