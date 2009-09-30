package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class PutSlim3ControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/putSlim3");
        PutSlim3Controller controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("putSlim3"));
        System.out.println("putSlim3:" + sessionScope("putSlim3"));
    }
}
