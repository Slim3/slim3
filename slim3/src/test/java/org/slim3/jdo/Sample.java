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

import java.util.List;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author higa
 * 
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Sample {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String name;

    @Persistent
    @Embedded
    private Hoge hoge;

    @Persistent
    private Long[] aaaArray;

    @Persistent
    private int[] intArray;

    @Persistent
    private List<Long> aaaList;

    /**
     * @return the aaaList
     */
    public List<Long> getAaaList() {
        return aaaList;
    }

    /**
     * @param aaaList
     *            the aaaList to set
     */
    public void setAaaList(List<Long> aaaList) {
        this.aaaList = aaaList;
    }

    /**
     * @return the intArray
     */
    public int[] getIntArray() {
        return intArray;
    }

    /**
     * @param intArray
     *            the intArray to set
     */
    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }

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
     * @return the hoge
     */
    public Hoge getHoge() {
        return hoge;
    }

    /**
     * @param hoge
     *            the hoge to set
     */
    public void setHoge(Hoge hoge) {
        this.hoge = hoge;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
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

    @Override
    public String toString() {
        return "id:" + id + ", name:" + name;
    }
}