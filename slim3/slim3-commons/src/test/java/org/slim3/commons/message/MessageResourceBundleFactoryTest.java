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
package org.slim3.commons.message;

import java.io.File;
import java.util.Locale;

import org.slim3.commons.config.Configuration;
import org.slim3.commons.unit.CleanableTestCase;

/**
 * @author higa
 * 
 */
public class MessageResourceBundleFactoryTest extends CleanableTestCase {

    private static final String PACKAGE = "org/slim3/commons/message/";

    private static final String PATH = PACKAGE + "foo.properties";

    private static final String CONFIG_PATH = PACKAGE
            + "slim3_configuration.properties";

    private static final String DEV_CONFIG_PATH = PACKAGE
            + "slim3_configuration_development.properties";

    private static final String BUNDLE_NAME = "org.slim3.commons.message.foo";

    /**
     * 
     */
    public void testCreateBundle() {
        Configuration.initialize(CONFIG_PATH);
        MessageResourceBundle bundle = MessageResourceBundleFactory
                .createBundle(PATH);
        assertNotNull(bundle);
        assertEquals("111", bundle.get("aaa"));
    }

    /**
     * 
     */
    public void testCreateBundleForIllegalPath() {
        Configuration.initialize(CONFIG_PATH);
        MessageResourceBundle bundle = MessageResourceBundleFactory
                .createBundle("illegal path");
        assertNull(bundle);
    }

    /**
     * 
     */
    public void testGetFile() {
        Configuration.initialize(CONFIG_PATH);
        File file = MessageResourceBundleFactory.getFile(PATH);
        assertNull(file);
    }

    /**
     * 
     */
    public void testGetFileForHotDeployment() {
        Configuration.initialize(DEV_CONFIG_PATH);
        File file = MessageResourceBundleFactory.getFile(PATH);
        assertNotNull(file);
        assertTrue(file.exists());
        assertNull(MessageResourceBundleFactory
                .getFile("java/lang/String.class"));
    }

    /**
     * 
     */
    public void testGetBundle() {
        Configuration.initialize(CONFIG_PATH);
        MessageResourceBundle bundle = MessageResourceBundleFactory
                .getBundle(BUNDLE_NAME);
        assertNotNull(bundle);
        assertEquals("111", bundle.get("aaa"));
    }

    /**
     * 
     */
    public void testGetBundleForLocale() {
        Configuration.initialize(CONFIG_PATH);
        MessageResourceBundle bundle = MessageResourceBundleFactory.getBundle(
                Locale.JAPAN, BUNDLE_NAME);
        assertNotNull(bundle);
        assertEquals("111ja", bundle.get("aaa"));
        assertEquals("222", bundle.get("bbb"));
    }
}
