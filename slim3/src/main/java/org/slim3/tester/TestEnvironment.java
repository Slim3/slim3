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

import java.util.HashMap;
import java.util.Map;

import org.slim3.util.AppEngineUtil;
import org.slim3.util.StringUtil;

import com.google.appengine.api.NamespaceManager;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.Environment;

/**
 * The test environment.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class TestEnvironment implements ApiProxy.Environment {

    /**
     * The application identifier.
     */
    protected String appId = "Unit Tests";

    /**
     * The version identifier.
     */
    protected String versionId = "1.0";

    /**
     * The authority domain.
     */
    protected String authDomain = "gmail.com";

    /**
     * The email address.
     */
    protected String email;

    /**
     * Whether the current user is an administrator.
     */
    protected boolean admin = false;

    /**
     * The remaining millisecond.
     */
    protected long remainingMillis;
    
    /**
     * The environment attributes.
     */
    protected Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * Constructor.
     */
    public TestEnvironment() {
        attributes.put("com.google.appengine.server_url_key", "dummy");
    }

    /**
     * Constructor.
     * 
     * @param other
     *            the other environment
     * @throws NullPointerException
     *             if the other parameter is null
     */
    public TestEnvironment(Environment other) throws NullPointerException {
        if (other == null) {
            throw new NullPointerException(
                "The other parameter must not be null.");
        }
        appId = other.getAppId();
        versionId = other.getVersionId();
        authDomain = other.getAuthDomain();
        email = other.getEmail();
        admin = other.isAdmin();
        attributes = other.getAttributes();
    }

    /**
     * Constructor.
     * 
     * @param email
     *            the email address
     */
    public TestEnvironment(String email) {
        this(email, true);
    }

    /**
     * Constructor.
     * 
     * @param email
     *            the email address
     * @param admin
     *            whether the current user is an administrator
     */
    public TestEnvironment(String email, boolean admin) {
        this();
        setEmail(email);
        setAdmin(admin);
    }

    public String getAppId() {
        return appId;
    }

    /**
     * Sets the application identifier.
     * 
     * @param appId
     *            the application identifier
     */
    public void setAppId(String appId) {
        assertNotProduction();
        this.appId = appId;
    }

    public String getVersionId() {
        return versionId;
    }

    /**
     * Sets the version identifier.
     * 
     * @param versionId
     *            the version identifier
     */
    public void setVersionId(String versionId) {
        assertNotProduction();
        this.versionId = versionId;
    }

    public String getRequestNamespace() {
        return NamespaceManager.get();
    }

    public String getAuthDomain() {
        return authDomain;
    }

    /**
     * Sets the authority domain.
     * 
     * @param authDomain
     *            the authority domain
     */
    public void setAuthDomain(String authDomain) {
        assertNotProduction();
        this.authDomain = authDomain;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     * 
     * @param email
     *            the email address
     */
    public void setEmail(String email) {
        assertNotProduction();
        this.email = email;
    }

    public boolean isLoggedIn() {
        return !StringUtil.isEmpty(email);
    }

    public boolean isAdmin() {
        return admin;
    }

    /**
     * Sets whether the current user is an administrator.
     * 
     * @param admin
     *            whether the current user is an administrator
     */
    public void setAdmin(boolean admin) {
        assertNotProduction();
        this.admin = admin;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * Sets the attributes.
     * 
     * @param attributes
     *            the attributes
     */
    public void setAttributes(Map<String, Object> attributes) {
        assertNotProduction();
        this.attributes = attributes;
    }

    /**
     * Asserts that the current environment is not production.
     * 
     * @throws IllegalStateException
     *             if the current environment is production
     */
    protected void assertNotProduction() throws IllegalStateException {
        if (AppEngineUtil.isProduction()) {
            throw new IllegalStateException(
                "This feature is not supported on production server.");
        }
    }

    @Override
    public long getRemainingMillis() {
        return remainingMillis;
    }
    
    /**
     * Sets the remaining millisecond.
     * 
     * @param remainingMillis the remaining millisecond
     */
    public void setRemainingMillis(long remainingMillis) {
        this.remainingMillis = remainingMillis;
    }
}