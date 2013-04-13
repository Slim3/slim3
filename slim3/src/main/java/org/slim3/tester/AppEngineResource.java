package org.slim3.tester;

import org.junit.rules.ExternalResource;
import org.slim3.util.WrapRuntimeException;

/**
 * initialize appengine resource at test class field.
 * you don't need extends {@link AppEngineTestCase}<br>
 *
 * <pre>
 * public class SomeServiceTest{
 *     &#064;Rule
 *     public AppEngineResource resource = new AppEngineResource();
 *
 *     &#064;Test
 *     public void test(){
 *     }
 * }
 * </pre>
 * @author sue445
 * @since 1.0.17
 */
public class AppEngineResource extends ExternalResource{
	public AppEngineTester tester = new AppEngineTester();

	@Override
	protected void before() throws Throwable {
		tester.setUp();
	}

	@Override
	protected void after() {
		try {
			tester.tearDown();
		} catch (Exception e) {
			throw new WrapRuntimeException(e);
		}
	}
}
