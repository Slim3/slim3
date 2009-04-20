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
package demo.model;

import java.util.Date;

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
    private Boolean enabled;

    @Persistent
    private Byte myByte;

    @Persistent
    private Short myShort;

    @Persistent
    private Integer myInteger;

    @Persistent
    private Long myLong;

    @Persistent
    private Float myFloat;

    @Persistent
    private Double myDouble;

    @Persistent
    private Date myDate;

    @Persistent
    private IdentityType myEnum;

    /**
     * @return the myEnum
     */
    public IdentityType getMyEnum() {
        return myEnum;
    }

    /**
     * @param myEnum
     *            the myEnum to set
     */
    public void setMyEnum(IdentityType myEnum) {
        this.myEnum = myEnum;
    }

    /**
     * @return the myDate
     */
    public Date getMyDate() {
        return myDate;
    }

    /**
     * @param myDate
     *            the myDate to set
     */
    public void setMyDate(Date myDate) {
        this.myDate = myDate;
    }

    /**
     * @return the myDouble
     */
    public Double getMyDouble() {
        return myDouble;
    }

    /**
     * @param myDouble
     *            the myDouble to set
     */
    public void setMyDouble(Double myDouble) {
        this.myDouble = myDouble;
    }

    /**
     * @return the myFloat
     */
    public Float getMyFloat() {
        return myFloat;
    }

    /**
     * @param myFloat
     *            the myFloat to set
     */
    public void setMyFloat(Float myFloat) {
        this.myFloat = myFloat;
    }

    /**
     * @return the myLong
     */
    public Long getMyLong() {
        return myLong;
    }

    /**
     * @param myLong
     *            the myLong to set
     */
    public void setMyLong(Long myLong) {
        this.myLong = myLong;
    }

    /**
     * @return the myInteger
     */
    public Integer getMyInteger() {
        return myInteger;
    }

    /**
     * @param myInteger
     *            the myInteger to set
     */
    public void setMyInteger(Integer myInteger) {
        this.myInteger = myInteger;
    }

    /**
     * @return the myShort
     */
    public Short getMyShort() {
        return myShort;
    }

    /**
     * @param myShort
     *            the myShort to set
     */
    public void setMyShort(Short myShort) {
        this.myShort = myShort;
    }

    /**
     * @return the byte
     */
    public Byte getMyByte() {
        return myByte;
    }

    /**
     * @param byte_
     *            the byte_ to set
     */
    public void setMyByte(Byte byte_) {
        this.myByte = byte_;
    }

    /**
     * @return the enabled
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * @param enabled
     *            the enabled to set
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
        return "id:" + id + ", name:" + name + ", enabled:" + enabled;
    }
}