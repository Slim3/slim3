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

@RunWith(Enclosed.class)
public class UserLoginTest{
	private static final String TEST_EMAIL_ADDRESS = "hoge@foo.com";

	public static class WhenUserLogin{
		@Rule
		public RuleChain ruleChain = RuleChain.outerRule(new AppEngineResource()).around(new UserLogin(TEST_EMAIL_ADDRESS));

		@Test
		public void isLogin() {
			assumeThat(AppEngineUtil.isProduction(), is(false));

			UserService userService = UserServiceFactory.getUserService();
			assertThat(userService.isUserLoggedIn(), is(true));
			assertThat(userService.getCurrentUser().getEmail(), is(TEST_EMAIL_ADDRESS));
			assertThat(userService.isUserAdmin(), is(false));
		}
	}

	public static class WhenAdminUserLogin{
		@Rule
		public RuleChain ruleChain = RuleChain.outerRule(new AppEngineResource()).around(new UserLogin(TEST_EMAIL_ADDRESS, true));

		@Test
		public void isLogin() {
			assumeThat(AppEngineUtil.isProduction(), is(false));

			UserService userService = UserServiceFactory.getUserService();
			assertThat(userService.isUserLoggedIn(), is(true));
			assertThat(userService.getCurrentUser().getEmail(), is(TEST_EMAIL_ADDRESS));
			assertThat(userService.isUserAdmin(), is(true));
		}
	}

	public static class WhenNotLogined{
		@Rule
		public AppEngineResource resource = new AppEngineResource();

		@Test
		public void isLogin() {
			UserService userService = UserServiceFactory.getUserService();
			assertThat(userService.isUserLoggedIn(), is(false));
		}
	}

}
