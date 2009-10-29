package slim3.demo.controller.performance;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class GetSlim3ControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/performance/getSlim3");
        GetSlim3Controller controller = tester.getController();
        assertThat(controller, is(not(nullValue())));
        assertTrue(tester.isRedirect());
        assertThat(tester.getDestinationPath(), is("/performance/"));
        assertThat(tester.sessionScope("getSlim3"), is(not(nullValue())));
        System.out.println("getSlim3:" + tester.sessionScope("getSlim3"));
    }
}
