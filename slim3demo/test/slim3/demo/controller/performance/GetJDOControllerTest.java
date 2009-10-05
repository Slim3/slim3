package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class GetJDOControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/getJDO");
        GetJDOController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/performance/", getDestinationPath());
        assertNotNull(sessionScope("getJDO"));
        System.out.println("getJDO:" + sessionScope("getJDO"));
    }
}
