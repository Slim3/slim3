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
package slim3.it.model;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

/**
 * @author higa
 * 
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Version(strategy = VersionStrategy.VERSION_NUMBER)
public class Foo {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String key;

    @Persistent
    private List<String> barKeyList;

    /**
     * @return the barKeyList
     */
    public List<String> getBarKeyList() {
        return barKeyList;
    }

    /**
     * @param barKeyList
     *            the barKeyList to set
     */
    public void setBarKeyList(List<String> barKeyList) {
        this.barKeyList = barKeyList;
    }

    @SuppressWarnings("unchecked")
    public List<Bar> getBarList() {
        PersistenceManager pm = JDOHelper.getPersistenceManager(this);
        return (List<Bar>) pm.newQuery(Bar.class, "key == :key").execute(
            barKeyList);
    }

    public void setBarList(List<Bar> barList) {
        List<String> list = new ArrayList<String>(barList.size());
        for (Bar bar : barList) {
            list.add(bar.getKey());
        }
        setBarKeyList(list);
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }
}