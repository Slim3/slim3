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
import static org.junit.Assume.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.slim3.util.AppEngineUtil;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 *
 * @author sue445
 *
 */
@RunWith(Enclosed.class)
public class UserLoginTest{
    private static final String TEST_EMAIL_ADDRESS = "hoge@foo.com";
    private static final String TEST_AUTH_DOMAIN = "foo.com";

    /**
     *
     * @author sue445
     *
     */
    public static class WhenUserLogin{
        /**
         *
         */
        @Rule
        public RuleChain ruleChain = RuleChain.outerRule(new AppEngineResource()).around(new UserLogin(TEST_EMAIL_ADDRESS));

        /**
         *
         */
        @Test
        public void isLogin() {
            assumeThat(AppEngineUtil.isProduction(), is(false));

            UserService userService = UserServiceFactory.getUserService();
            assertThat(userService.isUserLoggedIn(), is(true));
            assertThat(userService.getCurrentUser().getEmail(), is(TEST_EMAIL_ADDRESS));
            assertThat(userService.getCurrentUser().getAuthDomain(), is("gmail.com"));
            assertThat(userService.isUserAdmin(), is(false));
        }
    }

    /**
     *
     * @author sue445
     *
     */
    public static class WhenAdminUserLogin{
        /**
         *
         */
        @Rule
        public RuleChain ruleChain = RuleChain.outerRule(new AppEngineResource()).around(new UserLogin(TEST_EMAIL_ADDRESS, TEST_AUTH_DOMAIN, true));

        /**
         *
         */
        @Test
        public void isLogin() {
            assumeThat(AppEngineUtil.isProduction(), is(false));

            UserService userService = UserServiceFactory.getUserService();
            assertThat(userService.isUserLoggedIn(), is(true));
            assertThat(userService.getCurrentUser().getEmail(), is(TEST_EMAIL_ADDRESS));
            assertThat(userService.getCurrentUser().getAuthDomain(), is(TEST_AUTH_DOMAIN));
            assertThat(userService.isUserAdmin(), is(true));
        }
    }

    /**
     *
     * @author sue445
     *
     */
    public static class WhenNotLogined{
        /**
         *
         */
        @Rule
        public AppEngineResource resource = new AppEngineResource();

        /**
         *
         */
        @Test
        public void isLogin() {
            UserService userService = UserServiceFactory.getUserService();
            assertThat(userService.isUserLoggedIn(), is(false));
        }
    }

}
