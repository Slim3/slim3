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

import java.util.Properties;

import org.slim3.commons.message.MessageResourceBundle;


import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class MessageResourceBundleTest extends TestCase {

    /**
     * 
     */
    public void testGetForKeyIsNull() {
        MessageResourceBundle mrb = new MessageResourceBundle(null);
        assertNull(mrb.get(null));
    }

    /**
     * 
     */
    public void testGetForKeyExists() {
        Properties props = new Properties();
        props.put("aaa", "hoge");
        MessageResourceBundle mrb = new MessageResourceBundle(props);
        assertEquals("hoge", mrb.get("aaa"));
    }

    /**
     * 
     */
    public void testGetForKeyDoesNotExist() {
        Properties props = new Properties();
        props.put("aaa", "hoge");
        MessageResourceBundle mrb = new MessageResourceBundle(props);
        assertNull(mrb.get("bbb"));
    }

    /**
     * 
     */
    public void testGetForParentHasSameKey() {
        Properties parentProps = new Properties();
        parentProps.put("aaa", "parent");
        MessageResourceBundle parent = new MessageResourceBundle(parentProps);

        Properties selfProps = new Properties();
        selfProps.put("aaa", "self");
        MessageResourceBundle self = new MessageResourceBundle(selfProps,
                parent);
        assertEquals("self", self.get("aaa"));
    }

    /**
     * 
     */
    public void testGetForParentHasKeyAndSelfDoesnotHaveKey() {
        Properties parentProps = new Properties();
        parentProps.put("aaa", "parent");
        MessageResourceBundle parent = new MessageResourceBundle(parentProps);
        Properties selfProps = new Properties();
        selfProps.put("bbb", "self");
        MessageResourceBundle self = new MessageResourceBundle(selfProps,
                parent);
        assertEquals("parent", self.get("aaa"));
    }
}