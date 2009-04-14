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
package org.slim3.gae.unit;

import com.google.apphosting.api.ApiProxy;

/**
 * The test environment.
 * 
 * @author higa
 * @since 3.0
 * 
 */
class TestEnvironment implements ApiProxy.Environment {

    public String getAppId() {
        return "Unit Tests";
    }

    public String getVersionId() {
        return "1.0";
    }

    public void setDefaultNamespace(String s) {
    }

    public String getRequestNamespace() {
        return null;
    }

    public String getDefaultNamespace() {
        return null;
    }

    public String getAuthDomain() {
        return null;
    }

    public boolean isLoggedIn() {
        return false;
    }

    public String getEmail() {
        return null;
    }

    public boolean isAdmin() {
        return false;
    }
}