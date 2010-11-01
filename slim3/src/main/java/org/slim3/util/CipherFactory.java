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
 * A class to create the Cipher.
 * 
 * @author oyama1102
 * @since 1.0.6
 * 
 */
public abstract class CipherFactory {

    /**
     * The key of CipherFactory.
     */
    public static final String CIPHER_FACTORY_KEY = "slim3.cipherFactory";

    /**
     * Create the cipher. If the global key or the limited key of the current
     * thread is set, The key is set to the cipher. The key of the current
     * thread is given to priority more than a global key.
     * 
     * @return the cipher
     */
    public static CipherFactory getFactory() {
        String className =
            System.getProperty(CIPHER_FACTORY_KEY, AesCipherFactory.class
                .getName());
        return ClassUtil.newInstance(className);
    }

    /**
     * create cipher.
     * 
     * @return cipher
     */
    public abstract Cipher createCipher();

    /**
     * Clear the limited key for cipher to the current thread.
     */
    public abstract void clearLimitedKey();

    /**
     * Clear the global key for cipher.
     */
    public abstract void clearGlobalKey();

    /**
     * Sets the limited key for cipher to the current thread.
     * 
     * @param key
     *            the key
     * @exception IllegalArgumentException
     *                if the key parameter is not valid
     */
    public abstract void setLimitedKey(String key);

    /**
     * Sets the global key for cipher.
     * 
     * @param key
     *            the key
     * @exception IllegalArgumentException
     *                if the key parameter is not valid
     */
    public abstract void setGlobalKey(String key);

}
