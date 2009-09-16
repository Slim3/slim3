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
package org.slim3.datastore.model;

import org.slim3.datastore.Blob;
import org.slim3.datastore.Model;
import org.slim3.datastore.PrimaryKey;
import org.slim3.datastore.Text;
import org.slim3.datastore.Version;

import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
@Model
public class Hoge {

    @PrimaryKey
    private Key key;

    private String aaa;

    @Blob
    private byte[] bbb;

    @Text
    private String ccc;

    @Version
    private Long version;

    /**
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * @return the aaa
     */
    public String getAaa() {
        return aaa;
    }

    /**
     * @param aaa
     *            the aaa to set
     */
    public void setAaa(String aaa) {
        this.aaa = aaa;
    }

    /**
     * @return the bbb
     */
    public byte[] getBbb() {
        return bbb;
    }

    /**
     * @param bbb
     *            the bbb to set
     */
    public void setBbb(byte[] bbb) {
        this.bbb = bbb;
    }

    /**
     * @return the ccc
     */
    public String getCcc() {
        return ccc;
    }

    /**
     * @param ccc
     *            the ccc to set
     */
    public void setCcc(String ccc) {
        this.ccc = ccc;
    }

    /**
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}
