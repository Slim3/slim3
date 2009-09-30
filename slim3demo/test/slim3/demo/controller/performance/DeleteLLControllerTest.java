package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class DeleteLLControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/deleteLL");
        DeleteLLController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("deleteLL"));
        System.out.println("deleteLL:" + sessionScope("deleteLL"));
    }
}
