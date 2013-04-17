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

import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.slim3.util.AppEngineUtil;

import com.google.apphosting.api.ApiProxy;

/**
 * user login test.
 * this rule required {@link RuleChain}.
 * please use {@link AppEngineResource} or {@link ControllerResource}.
 * <pre>
 * public class SomeServiceTest{
 *     &#064;Rule
 *     public RuleChain ruleChain = RuleChain.outerRule(new AppEngineResource()).around(new UserLogin(TEST_EMAIL_ADDRESS));
 *
 *     &#064;Test
 *     public void test(){
 *     }
 * }
 * </pre>
 * @author sue445
 * @since 1.0.17
 */
public class UserLogin extends ExternalResource {
    private final String email;
    private final String authDomain;
    private final boolean admin;


    /**
     * login with anonimous user (not admin)
     * @param email		login mail address
     */
    public UserLogin(String email){
        this(email, null, false);
    }

    /**
     * login with anonimous user
     * @param email         login mail address
     * @param authDomain    The authority domain.(if specified null, use default(gmail.com))
     * @param admin         whether admin user
     */
    public UserLogin(String email, String authDomain, boolean admin){
        this.email = email;
        this.authDomain = authDomain;
        this.admin = admin;
    }

    /**
     * setup with spedified user
     */
    @Override
    protected void before() throws Throwable {
        if(AppEngineUtil.isProduction()){
            // not loaded TestEnvironment on production
            return;
        }

        TestEnvironment environment = (TestEnvironment) ApiProxy.getCurrentEnvironment();
        if(environment == null){
            throw new NullPointerException("Not initialized TestEnvironment. please use RuleChain");
        }
        environment.setEmail(email);
        environment.setAdmin(admin);
        if(authDomain != null){
            environment.setAuthDomain(authDomain);
        }
    }

}
