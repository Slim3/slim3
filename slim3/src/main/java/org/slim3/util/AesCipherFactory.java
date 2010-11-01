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
package org.slim3.util;

/**
 * A class to create the cipher of the AES algorithm.
 * 
 * @author oyama1102
 * @since 1.0.6
 * 
 */
public class AesCipherFactory extends CipherFactory {

    /**
     * The key of global key.
     */
    public static final String GLOBAL_KEY_KEY = "slim3.cipherGlobalKey";

    /**
     * The limited keys.
     */
    protected static ThreadLocal<String> keys = new ThreadLocal<String>();

    /**
     * The global key.
     */
    protected static String globalKey;

    @Override
    /**
     * Create the cipher of the AES algorithm.
     * 
     * @return Cipher of the AES algorithm.
     */
    public Cipher createCipher() {
        Cipher c = new AesCipher();
        String key = keys.get();
        if (key == null) {
            if (globalKey == null) {
                setUpGlobalKey();
            }
            key = globalKey;
        }
        if (key == null) {
            throw new IllegalStateException("A cipher key is required.");
        }
        c.setKey(key);
        return c;
    }

    /**
     * Clear the limited key for cipher to the current thread.
     */
    @Override
    public void clearLimitedKey() {
        keys.set(null);
    }

    @Override
    public void clearGlobalKey() {
        globalKey = null;
    }

    /**
     * Sets the limited key for cipher to the current thread.
     * 
     * @param key
     *            the key
     * @exception IllegalArgumentException
     *                if the key parameter is not 128 bits
     */
    @Override
    public void setLimitedKey(String key) {
        AesCipher.validateAesKey(key);
        keys.set(key);
    }

    /**
     * Sets the global key for cipher.
     * 
     * @param key
     *            the key
     * @exception IllegalArgumentException
     *                if the key parameter is not 128 bits
     */
    @Override
    public void setGlobalKey(String key) {
        AesCipher.validateAesKey(key);
        globalKey = key;
    }

    /**
     * Sets up a default global key.
     */
    protected void setUpGlobalKey() {
        String key = System.getProperty(GLOBAL_KEY_KEY);
        if (!StringUtil.isEmpty(key)) {
            setGlobalKey(key);
        }
    }
}