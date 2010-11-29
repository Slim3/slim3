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

import java.util.Locale;
import java.util.MissingResourceException;

import org.junit.After;
import org.junit.Test;

/**
 * @author higa
 * 
 */
public class ResourceBundleApplicationMessageDelegateTest {

    private ResourceBundleApplicationMessageDelegate delegate =
        new ResourceBundleApplicationMessageDelegate();

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        delegate.clearBundle();
    }

    /**
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        delegate.setBundle("test", Locale.ENGLISH);
        assertThat(delegate.get("aaa", "hoge"), is("hoge is required."));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getForUnknownLocale() throws Exception {
        delegate.setBundle("test", Locale.FRANCE);
        assertThat(delegate.get("aaa", "hoge"), is("hoge is required."));
    }

    /**
     * @throws Exception
     */
    @Test(expected = MissingResourceException.class)
    public void getWhenMessageIsNotFound() throws Exception {
        delegate.setBundle("test", Locale.ENGLISH);
        delegate.get("xxx");
    }
}
