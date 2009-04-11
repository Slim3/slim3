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
package org.slim3.struts.validator;

import java.io.UnsupportedEncodingException;

import org.slim3.commons.util.StringUtil;

/**
 * The generic validator for Slim3.
 * 
 * @author Satoshi Kimura
 * @author higa
 * @since 3.0
 */
public class S3GenericValidator {

    private S3GenericValidator() {
    }

    /**
     * Checks if the byte length is greater than or equal to the minimum length.
     * 
     * @param value
     *            the value
     * @param min
     *            the minimum length
     * @param charset
     *            the character set
     * @return the checked result
     */
    public static boolean minByteLength(String value, int min, String charset) {
        return (getBytes(value, charset).length >= min);
    }

    /**
     * Checks if the byte length is less than or equal to the maximum length.
     * 
     * @param value
     *            the value
     * @param max
     *            the maximum length
     * @param charset
     *            the character set
     * @return the checked result
     */
    public static boolean maxByteLength(String value, int max, String charset) {
        return (getBytes(value, charset).length <= max);
    }

    private static byte[] getBytes(String str, String charset) {
        if (StringUtil.isEmpty(charset)) {
            return str.getBytes();
        }
        try {
            return str.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}