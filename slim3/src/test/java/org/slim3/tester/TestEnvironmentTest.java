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
package org.slim3.tester;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class TestEnvironmentTest {

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructor() throws Exception {
        TestEnvironment env = new TestEnvironment("higayasuo@gmail.com");
        assertThat(env.getEmail(), is("higayasuo@gmail.com"));
        assertThat(env.isLoggedIn(), is(true));
        assertThat(env.isAdmin(), is(true));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructorForOther() throws Exception {
        TestEnvironment other = new TestEnvironment();
        String appId = "aaa";
        String versionId = "bbb";
        String authDomain = "ddd";
        String email = "eee";
        boolean admin = false;
        Map<String, Object> attributes = new HashMap<String, Object>();
        other.setAppId(appId);
        other.setVersionId(versionId);
        other.setAuthDomain(authDomain);
        other.setEmail(email);
        other.setAdmin(admin);
        other.setAttributes(attributes);
        TestEnvironment env = new TestEnvironment(other);
        assertThat(env.getAppId(), is(appId));
        assertThat(env.getVersionId(), is(versionId));
        assertThat(env.getAuthDomain(), is(authDomain));
        assertThat(env.getEmail(), is(email));
        assertThat(env.isAdmin(), is(admin));
        assertThat(env.getAttributes(), is(attributes));
    }
}