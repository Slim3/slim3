package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class DeleteEGJDO2ControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/deleteEGJDO2");
        DeleteEGJDO2Controller controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("deleteEGJDO2"));
        System.out.println("deleteEGJDO2:" + sessionScope("deleteEGJDO2"));
    }
}
