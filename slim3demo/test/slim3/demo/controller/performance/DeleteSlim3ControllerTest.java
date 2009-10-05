package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class DeleteSlim3ControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/deleteSlim3");
        DeleteSlim3Controller controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("deleteSlim3"));
        System.out.println("deleteSlim3:" + sessionScope("deleteSlim3"));
    }
}
