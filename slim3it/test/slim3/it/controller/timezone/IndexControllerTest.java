package slim3.it.controller.timezone;

import org.slim3.tester.JDOControllerTestCase;

public class IndexControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        start("/timezone/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/timezone/index.jsp", getDestinationPath());
        assertNotNull(requestScope("now"));
    }
}
