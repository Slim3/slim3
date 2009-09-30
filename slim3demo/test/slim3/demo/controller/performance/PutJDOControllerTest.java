package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class PutJDOControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/putJDO");
        PutJDOController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("putJDO"));
        System.out.println("putJDO:" + sessionScope("putJDO"));
    }
}
