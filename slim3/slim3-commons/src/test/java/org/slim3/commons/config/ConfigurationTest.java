/*
 * Copyright 2004-2009 the original author or authors.
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
package org.slim3.commons.config;

import org.slim3.commons.unit.CleanableTestCase;

/**
 * @author higa
 * 
 */
public class ConfigurationTest extends CleanableTestCase {

    private static final String PACKAGE = "org/slim3/commons/config/";

    private static final String CONFIG_PATH =
        PACKAGE + "slim3_configuration.properties";

    private static final String HOT_CONFIG_PATH =
        PACKAGE + "slim3_configuration_hot.properties";

    /**
     * 
     */
    public void testInitializeForBadPath() {
        try {
            Configuration.initialize("bad path");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     */
    public void testIsHotForDefault() {
        Configuration.initialize(CONFIG_PATH);
        assertFalse(Configuration.getInstance().isHot());
    }

    /**
     * 
     */
    public void testIsHotSpecified() {
        Configuration.initialize(HOT_CONFIG_PATH);
        assertTrue(Configuration.getInstance().isHot());
    }

    /**
     * 
     */
    public void testGetValue() {
        Configuration.initialize(CONFIG_PATH);
        assertEquals("1", Configuration.getInstance().getValue("aaa"));
    }

    /**
     * 
     */
    public void testGetValueForSystemProperty() {
        String previousValue = System.getProperty("aaa");
        try {
            System.setProperty("aaa", "2");
            Configuration.initialize(CONFIG_PATH);
            assertEquals("2", Configuration.getInstance().getValue("aaa"));
        } finally {
            if (previousValue != null) {
                System.setProperty("aaa", previousValue);
            } else {
                System.clearProperty("aaa");
            }
        }
    }
}