package slim3.demo.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class IndexControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/");
        IndexController controller = tester.getController();
        assertThat(controller, is(not(nullValue())));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/index.jsp"));
    }
}
