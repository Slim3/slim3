package ${package}.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slim3.tester.AppEngineTester;

public class Slim3ServiceTest {

	@Test
	public void slim3test1() {
		Slim3Service.newAndPut("abc");
		assertThat(Slim3Service.queryAll().size(), is(equalTo(1)));
	}

	private static AppEngineTester testHelper;

	@Before
	public void setUp() throws Exception {
		testHelper = new AppEngineTester();
		testHelper.setUp();
	}

	@After
	public void tearDown() throws Exception {
		testHelper.tearDown();
	}
}
