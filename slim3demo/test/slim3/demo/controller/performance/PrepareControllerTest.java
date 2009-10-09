package slim3.demo.controller.performance;

import org.slim3.tester.ControllerTestCase;

public class PrepareControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        start("/performance/prepare");
        PrepareController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals(null, getDestinationPath());
        assertEquals(500, count("Bar"));
    }
}
