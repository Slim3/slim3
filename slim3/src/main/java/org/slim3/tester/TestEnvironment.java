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
package org.slim3.tester;

import java.util.HashMap;
import java.util.Map;

import org.slim3.util.StringUtil;

import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.Environment;

/**
 * The test environment.
 * 
 * @author higa
 * @since 3.0
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
     * The request namespace.
     */
    protected String requestNamespace = "";

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
        requestNamespace = other.getRequestNamespace();
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
        this.versionId = versionId;
    }

    public String getRequestNamespace() {
        return requestNamespace;
    }

    /**
     * Sets the request namespace.
     * 
     * @param requestNamespace
     *            the request namespace
     */
    public void setRequestNamespace(String requestNamespace) {
        this.requestNamespace = requestNamespace;
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
        this.attributes = attributes;
    }
}