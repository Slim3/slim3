package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class IndexControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/performance/index.jsp", getDestinationPath());
    }
}
