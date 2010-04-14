package ${package}.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slim3.tester.ControllerTester;

public class IndexControllerTest {

	@Test
	public void test() throws NullPointerException, IllegalArgumentException,
			IOException, ServletException {
		tester.start("/");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
	}

	ControllerTester tester;

	@Before
	public void setUp() throws Exception {
		tester = new ControllerTester(this.getClass());
		tester.setUp();
	}

	@After
	public void tearDown() throws Exception {
		tester.tearDown();
	}
}
