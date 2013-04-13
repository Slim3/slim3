package org.slim3.tester;

import org.junit.Rule;
import org.junit.Test;
import org.slim3.memcache.Memcache;

public class ControllerResourceTest {
	@Rule
	public ControllerResource resource = new ControllerResource(ControllerResourceTest.class);

	@Test
	public void test() {
		// if not initialized appengine, occured error
		Memcache.put("key", "value");
	}

}
