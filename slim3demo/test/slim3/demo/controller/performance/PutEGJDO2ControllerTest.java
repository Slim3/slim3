package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class PutEGJDO2ControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/putEGJDO2");
        PutEGJDO2Controller controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("putEGJDO2"));
        System.out.println("putEGJDO2:" + sessionScope("putEGJDO2"));
    }
}
