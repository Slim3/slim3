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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

/**
 * @author higa
 * 
 */
public class AesCipherFactoryTest {

    /**
     * 
     */
    @After
    public void tearDown() {
        System.clearProperty(AesCipherFactory.GLOBAL_KEY_KEY);
        AesCipherFactory.globalKey = null;
    }

    /**
     * @throws Exception
     */
    @Test
    public void setUpGlobalKey() throws Exception {
        String key = "xxxxxxxxxxxxxxxx";
        System.setProperty(AesCipherFactory.GLOBAL_KEY_KEY, key);
        AesCipherFactory factory = new AesCipherFactory();
        AesCipher cipher = (AesCipher) factory.createCipher();
        assertThat(AesCipherFactory.globalKey, is(key));
        assertThat(cipher.key, is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void globalKey() throws Exception {
        String key = "xxxxxxxxxxxxxxxx";
        AesCipherFactory factory = new AesCipherFactory();
        factory.setGlobalKey(key);
        assertThat(AesCipherFactory.globalKey, is(key));
        factory.clearGlobalKey();
        assertThat(AesCipherFactory.globalKey, is(nullValue()));
    }
}