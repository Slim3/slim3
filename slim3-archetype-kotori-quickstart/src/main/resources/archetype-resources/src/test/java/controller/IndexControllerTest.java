package ${package}.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class IndexControllerTest extends ControllerTestCase {

  @Test
  public void test() throws NullPointerException, IllegalArgumentException,
      IOException, ServletException {
    tester.start("/");
    assertThat(tester.response.getStatus(),
        is(equalTo(HttpServletResponse.SC_OK)));
  }

  LocalServiceTestHelper helper;

  @Override
  public void setUp() throws Exception {
    LocalDatastoreServiceTestConfig dsConfig = new LocalDatastoreServiceTestConfig();
    dsConfig.setNoStorage(true);
    dsConfig.setNoIndexAutoGen(true);
    helper = new LocalServiceTestHelper(dsConfig);
    helper.setUp();
    super.setUp();
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();
    helper.tearDown();
  }
}
