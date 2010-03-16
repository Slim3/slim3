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
package org.slim3.datastore.model;

import java.io.Serializable;

/**
 * @author higa
 * 
 */
public class MySerializable implements Serializable {

    private static final long serialVersionUID = 1L;

    private String aaa;

    /**
     * 
     */
    public MySerializable() {
        super();
    }

    /**
     * @param aaa
     */
    public MySerializable(String aaa) {
        super();
        this.aaa = aaa;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((aaa == null) ? 0 : aaa.hashCode());
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
        MySerializable other = (MySerializable) obj;
        if (aaa == null) {
            if (other.aaa != null)
                return false;
        } else if (!aaa.equals(other.aaa))
            return false;
        return true;
    }
}
