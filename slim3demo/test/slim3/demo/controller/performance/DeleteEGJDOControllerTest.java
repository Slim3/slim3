package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class DeleteEGJDOControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/deleteEGJDO");
        DeleteEGJDOController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("deleteEGJDO"));
        System.out.println("deleteEGJDO:" + sessionScope("deleteEGJDO"));
    }
}
