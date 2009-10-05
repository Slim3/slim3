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

/**
 * @author higa
 * 
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, table = "Foo")
public class FooJDO {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String key;

    private String string01;

    private String string02;

    private String string03;

    private String string04;

    private String string05;

    private String string06;

    private String string07;

    private String string08;

    private String string09;

    private String string10;

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

    /**
     * @return the string01
     */
    public String getString01() {
        return string01;
    }

    /**
     * @param string01
     *            the string01 to set
     */
    public void setString01(String string01) {
        this.string01 = string01;
    }

    /**
     * @return the string02
     */
    public String getString02() {
        return string02;
    }

    /**
     * @param string02
     *            the string02 to set
     */
    public void setString02(String string02) {
        this.string02 = string02;
    }

    /**
     * @return the string03
     */
    public String getString03() {
        return string03;
    }

    /**
     * @param string03
     *            the string03 to set
     */
    public void setString03(String string03) {
        this.string03 = string03;
    }

    /**
     * @return the string04
     */
    public String getString04() {
        return string04;
    }

    /**
     * @param string04
     *            the string04 to set
     */
    public void setString04(String string04) {
        this.string04 = string04;
    }

    /**
     * @return the string05
     */
    public String getString05() {
        return string05;
    }

    /**
     * @param string05
     *            the string05 to set
     */
    public void setString05(String string05) {
        this.string05 = string05;
    }

    /**
     * @return the string06
     */
    public String getString06() {
        return string06;
    }

    /**
     * @param string06
     *            the string06 to set
     */
    public void setString06(String string06) {
        this.string06 = string06;
    }

    /**
     * @return the string07
     */
    public String getString07() {
        return string07;
    }

    /**
     * @param string07
     *            the string07 to set
     */
    public void setString07(String string07) {
        this.string07 = string07;
    }

    /**
     * @return the string08
     */
    public String getString08() {
        return string08;
    }

    /**
     * @param string08
     *            the string08 to set
     */
    public void setString08(String string08) {
        this.string08 = string08;
    }

    /**
     * @return the string09
     */
    public String getString09() {
        return string09;
    }

    /**
     * @param string09
     *            the string09 to set
     */
    public void setString09(String string09) {
        this.string09 = string09;
    }

    /**
     * @return the string10
     */
    public String getString10() {
        return string10;
    }

    /**
     * @param string10
     *            the string10 to set
     */
    public void setString10(String string10) {
        this.string10 = string10;
    }

}