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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.appengine.repackaged.com.google.common.util.Base64;

/**
 * A class to cipher using the AES algorithm.
 * 
 * @author oyama1102
 * @since 1.0.6
 */
public class AesCipher implements Cipher {

    /**
     * The algorithm.
     */
    protected static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * The key of algorithm.
     */
    protected static final String ALGORITHM_KEY = "AES";

    /**
     * CBC IV.
     */
    protected static final byte[] CBC_IV =
        {
            0x01,
            0x09,
            0x07,
            0x08,
            0x01,
            0x01,
            0x00,
            0x02,
            0x01,
            0x09,
            0x08,
            0x03,
            0x00,
            0x06,
            0x02,
            0x07, };

    private static final String ENCODING = "UTF-8";

    private static final Logger logger =
        Logger.getLogger(AesCipher.class.getName());

    /**
     * The key.
     */
    protected String key;

    /**
     * Encrypt the text.
     * 
     * @param text
     *            the text
     * @return the encrypted text
     * @exception IllegalStateException
     *                if the key for cipher is not set
     * @exception IllegalArgumentException
     *                if the key parameter is not 128 bits
     */
    public String encrypt(String text) {
        if (StringUtil.isEmpty(text)) {
            return text;
        }
        if (key == null) {
            throw new IllegalStateException("The key for cipher is not set.");
        }
        return encrypt(key, text);
    }

    /**
     * Encrypt the text.
     * 
     * @param key
     *            the key for cipher
     * @param text
     *            the text
     * @return the encrypted text
     * @exception IllegalArgumentException
     *                if the key parameter is not 128 bits
     */
    public String encrypt(String key, String text) {
        if (StringUtil.isEmpty(text)) {
            return text;
        }
        validateAesKey(key);
        String result = null;
        try {
            SecretKeySpec spec =
                new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM_KEY);
            javax.crypto.Cipher cipher =
                javax.crypto.Cipher.getInstance(ALGORITHM);
            cipher.init(
                javax.crypto.Cipher.ENCRYPT_MODE,
                spec,
                new IvParameterSpec(CBC_IV));
            byte[] encText = cipher.doFinal(text.getBytes(ENCODING));
            result = Base64.encode(encText);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return result;
    }

    /**
     * Decrypt the encrypted text.
     * 
     * @param encryptedText
     *            the encrypted text
     * @return the decrypted text
     * @exception IllegalStateException
     *                if the key for cipher is not set
     * @exception IllegalArgumentException
     *                if the key parameter is not 128 bits
     */
    public String decrypt(String encryptedText) {
        if (StringUtil.isEmpty(encryptedText)) {
            return encryptedText;
        }
        if (key == null) {
            throw new IllegalStateException("The key for cipher is not set.");
        }
        return decrypt(key, encryptedText);
    }

    /**
     * Decrypt the encrypted text.
     * 
     * @param key
     *            the key for cipher
     * @param encryptedText
     *            the encrypted text
     * @return the decrypted text
     * @exception IllegalArgumentException
     *                if the key parameter is not 128 bits
     */
    public String decrypt(String key, String encryptedText) {
        if (StringUtil.isEmpty(encryptedText)) {
            return encryptedText;
        }
        validateAesKey(key);
        String result = null;
        try {
            SecretKeySpec spec =
                new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM_KEY);
            javax.crypto.Cipher cipher =
                javax.crypto.Cipher.getInstance(ALGORITHM);
            cipher.init(
                javax.crypto.Cipher.DECRYPT_MODE,
                spec,
                new IvParameterSpec(CBC_IV));
            byte[] bytes = Base64.decode(encryptedText);
            result = new String(cipher.doFinal(bytes), ENCODING);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return result;
    }

    /**
     * Sets the Key for cipher.
     * 
     * @param key
     *            the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalArgumentException
     *             if the key parameter is not 128 bits
     */
    public void setKey(String key) throws NullPointerException,
            IllegalArgumentException {
        validateAesKey(key);
        this.key = key;
    }

    /**
     * Validates the AES key.
     * 
     * @param key
     *            the AES key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalArgumentException
     *             if the key parameter is not 128 bits
     */
    public static void validateAesKey(String key) throws NullPointerException,
            IllegalArgumentException {
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        int len = key.getBytes().length;
        if (len != 16) {
            throw new IllegalArgumentException(
                "The key for cipher must be 128 bits.");
        }
    }
}
