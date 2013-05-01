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
package org.slim3.test.model;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * @author vvakame
 */
@Model
public class AttributeParameterSampleModel {
    @Attribute(primaryKey = true)
    Key primaryKey;

    @Attribute(lob = true)
    String text;

    @Attribute(cipher = true)
    String chiper;

    @Attribute(name = "hoge")
    String foo;

    @Attribute(unindexed = true)
    String unindexted;

    @Attribute(listener = SampleListener.class)
    String listener1;

    @Attribute(listener = SampleInheritListener.class)
    String listener2;

    @Attribute(version = true)
    long version;

    /**
     * @return the primaryKey
     */
    public Key getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey
     *            the primaryKey to set
     */
    public void setPrimaryKey(Key primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the chiper
     */
    public String getChiper() {
        return chiper;
    }

    /**
     * @param chiper
     *            the chiper to set
     */
    public void setChiper(String chiper) {
        this.chiper = chiper;
    }

    /**
     * @return the foo
     */
    public String getFoo() {
        return foo;
    }

    /**
     * @param foo
     *            the foo to set
     */
    public void setFoo(String foo) {
        this.foo = foo;
    }

    /**
     * @return the unindexted
     */
    public String getUnindexted() {
        return unindexted;
    }

    /**
     * @param unindexted
     *            the unindexted to set
     */
    public void setUnindexted(String unindexted) {
        this.unindexted = unindexted;
    }

    /**
     * @return the listener1
     */
    public String getListener1() {
        return listener1;
    }

    /**
     * @param listener1
     *            the listener1 to set
     */
    public void setListener1(String listener1) {
        this.listener1 = listener1;
    }

    /**
     * @return the listener2
     */
    public String getListener2() {
        return listener2;
    }

    /**
     * @param listener2
     *            the listener2 to set
     */
    public void setListener2(String listener2) {
        this.listener2 = listener2;
    }

    /**
     * @return the version
     */
    public long getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(long version) {
        this.version = version;
    }
}
