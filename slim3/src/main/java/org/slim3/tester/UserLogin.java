package org.slim3.tester;

import org.junit.rules.ExternalResource;
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
	private final boolean admin;


	/**
	 * login with anonimous user (not admin)
	 * @param email		login mail address
	 */
	public UserLogin(String email){
		this(email, false);
	}

	/**
	 * login with anonimous user
	 * @param email		login mail address
	 * @param admin		whether admin user
	 */
	public UserLogin(String email, boolean admin){
		this.email = email;
		this.admin = admin;
	}

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
	}

}
