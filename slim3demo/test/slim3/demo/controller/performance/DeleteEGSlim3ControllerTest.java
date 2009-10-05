package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class DeleteEGSlim3ControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/deleteEGSlim3");
        DeleteEGSlim3Controller controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("deleteEGSlim3"));
        System.out.println("deleteEGSlim3:" + sessionScope("deleteEGSlim3"));
    }
}
