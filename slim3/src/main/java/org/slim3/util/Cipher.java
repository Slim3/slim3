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
 * A interface for the cipher.
 * 
 * @author oyama1102
 * @since 1.0.6
 * 
 */
public interface Cipher {

    /**
     * Encrypt the text.
     * 
     * @param text
     *            the text
     * @return the encrypted text
     * @exception IllegalStateException
     *                if the key is not set to this instance
     * @exception IllegalArgumentException
     *                if the key is not valid
     */
    public String encrypt(String text);

    /**
     * Encrypt the text.
     * 
     * @param key
     *            the key for cipher
     * @param text
     *            the text
     * @return the encrypted text
     * @exception IllegalArgumentException
     *                if the key is not valid
     */
    public String encrypt(String key, String text);

    /**
     * Decrypt the encrypted text.
     * 
     * @param encryptedText
     *            the encrypted text
     * @return the decrypted text
     * @exception IllegalStateException
     *                if the key is not set to this instance
     * @exception IllegalArgumentException
     *                if the key is not valid
     */
    public String decrypt(String encryptedText);

    /**
     * Decrypt the encrypted text.
     * 
     * @param key
     *            the key for cipher
     * @param encryptedText
     *            the encrypted text
     * @return the decrypted text
     * @exception IllegalArgumentException
     *                if the key is not valid
     */
    public String decrypt(String key, String encryptedText);

    /**
     * Sets the Key for cipher.
     * 
     * @param key
     *            the key
     * @exception IllegalArgumentException
     *                if the key is not valid
     */
    public void setKey(String key);
}
