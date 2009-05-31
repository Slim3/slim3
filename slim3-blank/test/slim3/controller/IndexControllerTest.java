package slim3.controller;

import org.slim3.tester.JDOControllerTestCase;

import slim3.controller.IndexController;

public class IndexControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        start("/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/index.jsp", getNextPath());
    }
}
