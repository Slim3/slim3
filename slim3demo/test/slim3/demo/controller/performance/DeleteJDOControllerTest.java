package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class DeleteJDOControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/deleteJDO");
        DeleteJDOController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("deleteJDO"));
        System.out.println("deleteJDO:" + sessionScope("deleteJDO"));
    }
}
