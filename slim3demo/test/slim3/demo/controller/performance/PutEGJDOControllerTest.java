package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class PutEGJDOControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/putEGJDO");
        PutEGJDOController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("putEGJDO"));
        System.out.println("putEGJDO:" + sessionScope("putEGJDO"));
    }
}
