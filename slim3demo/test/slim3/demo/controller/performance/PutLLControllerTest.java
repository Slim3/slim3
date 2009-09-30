package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class PutLLControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/putLL");
        PutLLController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("putLL"));
        System.out.println("putLL:" + sessionScope("putLL"));
    }
}
