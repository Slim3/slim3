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
package com.google.appengine.api.datastore;

import java.io.Serializable;

public class Key implements Serializable {

    private Key parentKey;

    public Key getParentKey() {
        return parentKey;
    }

    public void setParentKey(Key parentKey) {
        this.parentKey = parentKey;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAppIdNamespace(AppIdNamespace appIdNamespace) {
        this.appIdNamespace = appIdNamespace;
    }

    private String kind;

    private String appId;

    private long id;

    private String name;

    private AppIdNamespace appIdNamespace;

    public Key() {
    }
    
    Key(String kind) {
        this(kind, null, 0L);
    }

    Key(String kind, String name) {
        this(kind, null, name);
    }

    Key(String kind, Key parentKey) {
        this(kind, parentKey, 0L);
    }

    Key(String kind, Key parentKey, long id) {
        this(kind, parentKey, id, null, (AppIdNamespace) null);
    }

    Key(String kind, Key parentKey, String name) {
        this(kind, parentKey, 0L, name, (AppIdNamespace) null);
    }

    Key(String kind, Key parentKey, long id, String name,
            AppIdNamespace appIdNamespace) {
        if (kind == null || kind.length() == 0)
            throw new IllegalArgumentException("No kind specified.");
        if (name != null) {
            if (name.length() == 0)
                throw new IllegalArgumentException("Name may not be empty.");
            if (id != 0L)
                throw new IllegalArgumentException(
                    "Id and name may not both be specified at once.");
        }
        this.name = name;
        this.id = id;
        this.parentKey = parentKey;
        this.kind = kind.intern();
        this.appIdNamespace = appIdNamespace;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(30);
        appendToString(buffer);
        return buffer.toString();
    }

    private void appendToString(StringBuffer buffer) {
        if (parentKey != null) {
            parentKey.appendToString(buffer);
            buffer.append("/");
        }
        buffer.append(kind);
        buffer.append("(");
        if (name != null) {
            buffer.append("\"").append(name).append("\"");
        } else {
            buffer.append(String.valueOf(id));
        }
        buffer.append(")");
    }
    
    public String getAppId() {
        return appId;
    }

    public String getKind() {
        return kind;
    }

    public Key getParent() {
        return parentKey;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AppIdNamespace getAppIdNamespace() {
        return appIdNamespace;
    }
    
    public String getNamespace() {
        throw new IllegalStateException("getNamespace is not supported on gwt");
    }
    
    public Key getChild(String kind, long id) {
        if(!isComplete())
            throw new IllegalStateException("Cannot get a child of an incomplete key.");
        else
            return new Key(kind, this, id);
    }

    public Key getChild(String kind, String name) {
        if(!isComplete())
            throw new IllegalStateException("Cannot get a child of an incomplete key.");
        else
            return new Key(kind, this, name);
    }
    
    public boolean isComplete() {
        return id != 0L || name != null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((kind == null) ? 0 : kind.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result =
            prime * result + ((parentKey == null) ? 0 : parentKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Key other = (Key) obj;
        if (id != other.id)
            return false;
        if (kind == null) {
            if (other.kind != null)
                return false;
        } else if (!kind.equals(other.kind))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (parentKey == null) {
            if (other.parentKey != null)
                return false;
        } else if (!parentKey.equals(other.parentKey))
            return false;
        return true;
    }
}