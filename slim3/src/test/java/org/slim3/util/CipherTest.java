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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

/**
 * @author oyama1102
 * 
 */
public class CipherTest extends AppEngineTestCase {

    private static final String TEXT = "abcABC123\n";
    private static final String KEY128BIT = "1234567890ABCDEF";

    /**
     * @throws Exception
     */
    @Test
    public void encryptForLimitedKey() throws Exception {
        CipherFactory factory = CipherFactory.getFactory();
        factory.setLimitedKey(KEY128BIT);
        Cipher c = factory.createCipher();
        assertThat(c.encrypt(TEXT), not(TEXT));
        assertThat(c.decrypt(c.encrypt(TEXT)), is(TEXT));
    }

    /**
     * @throws Exception
     */
    @Test
    public void encryptForGlobalKey() throws Exception {
        CipherFactory factory = CipherFactory.getFactory();
        factory.setGlobalKey(KEY128BIT);
        factory.clearLimitedKey();
        Cipher c = factory.createCipher();
        assertThat(c.encrypt(TEXT), not(TEXT));
        assertThat(c.decrypt(c.encrypt(TEXT)), is(TEXT));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void setLimitedKey() throws Exception {
        CipherFactory factory = CipherFactory.getFactory();
        factory.setLimitedKey(KEY128BIT + "1");
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void setGlobalKey() throws Exception {
        CipherFactory factory = CipherFactory.getFactory();
        factory.setGlobalKey(KEY128BIT + "1");
    }
}