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
package org.slim3.jdo;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * @author higa
 * 
 */
@PersistenceCapable
@EmbeddedOnly
public class Hoge {

    @Persistent
    private String name;

    @Persistent
    private Long[] aaaArray;

    /**
     * @return the aaaArray
     */
    public Long[] getAaaArray() {
        return aaaArray;
    }

    /**
     * @param aaaArray
     *            the aaaArray to set
     */
    public void setAaaArray(Long[] aaaArray) {
        this.aaaArray = aaaArray;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}