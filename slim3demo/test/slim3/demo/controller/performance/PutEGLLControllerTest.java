package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class PutEGLLControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/putEGLL");
        PutEGLLController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("putEGLL"));
        System.out.println("putEGLL:" + sessionScope("putEGLL"));
    }
}
