package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class PutEGSlim3ControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/putEGSlim3");
        PutEGSlim3Controller controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("putEGSlim3"));
        System.out.println("putEGSlim3:" + sessionScope("putEGSlim3"));
        assertEquals(100, count("Parent"));
        assertEquals(1000, count("Child"));
    }
}
