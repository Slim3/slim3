package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class GetSlim3ControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/getSlim3");
        GetSlim3Controller controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("getSlim3"));
        System.out.println("getSlim3:" + sessionScope("getSlim3"));
    }
}
