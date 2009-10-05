package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class GetLLControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/getLL");
        GetLLController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("getLL"));
        System.out.println("getLL:" + sessionScope("getLL"));
    }
}
