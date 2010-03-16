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

import java.util.TimeZone;

import org.junit.After;
import org.junit.Test;

/**
 * @author higa
 * 
 */
public class TimeZoneLocatorTest {

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        TimeZoneLocator.set(null);
    }

    /**
     * @throws Exception
     */
    @Test
    public void setAndGet() throws Exception {
        TimeZone tz = TimeZone.getTimeZone("PST");
        TimeZoneLocator.set(tz);
        assertThat(TimeZoneLocator.get(), is(sameInstance(tz)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getForNoSetting() throws Exception {
        assertThat(TimeZoneLocator.get(), is(TimeZone.getDefault()));
    }
}
