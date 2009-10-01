package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class PutEGSlim3ControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/putEGSlim3");
        PutEGSlim3Controller controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("putEGSlim3"));
        System.out.println("putEGSlim3:" + sessionScope("putEGSlim3"));
    }
}
