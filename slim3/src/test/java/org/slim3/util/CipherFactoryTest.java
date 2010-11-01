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
public class CipherFactoryTest {

    /**
     * 
     */
    @After
    public void tearDown() {
        System.clearProperty(CipherFactory.CIPHER_FACTORY_KEY);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getFactory() throws Exception {
        CipherFactory factory = CipherFactory.getFactory();
        assertThat(factory, is(notNullValue()));
        assertThat(factory, is(AesCipherFactory.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getCustomizedFactory() throws Exception {
        System.setProperty(
            CipherFactory.CIPHER_FACTORY_KEY,
            MyCipherFactory.class.getName());
        CipherFactory factory = CipherFactory.getFactory();
        assertThat(factory, is(notNullValue()));
        assertThat(factory, is(MyCipherFactory.class));
    }

    /**
     *
     */
    public static class MyCipherFactory extends CipherFactory {

        @Override
        public void clearLimitedKey() {
        }

        @Override
        public Cipher createCipher() {
            return null;
        }

        @Override
        public void setGlobalKey(String key) {
        }

        @Override
        public void setLimitedKey(String key) {
        }

        @Override
        public void clearGlobalKey() {
        }

    }
}