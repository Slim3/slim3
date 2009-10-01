package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class DeleteEGLLControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/deleteEGLL");
        DeleteEGLLController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("deleteEGLL"));
        System.out.println("deleteEGLL:" + sessionScope("deleteEGLL"));
    }
}
