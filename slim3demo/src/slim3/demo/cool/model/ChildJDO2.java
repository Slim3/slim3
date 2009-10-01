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
package slim3.demo.cool.model;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, table = "Child")
public class ChildJDO2 {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    @Extension(vendorName = "datanucleus", key = "gae.parent-pk", value = "true")
    private Key parentKey;

    /**
     * @return the parentKey
     */
    public Key getParentKey() {
        return parentKey;
    }

    /**
     * @param parentKey
     *            the parentKey to set
     */
    public void setParentKey(Key parentKey) {
        this.parentKey = parentKey;
    }

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
}