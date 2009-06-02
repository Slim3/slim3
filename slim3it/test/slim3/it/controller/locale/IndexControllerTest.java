package slim3.it.controller.locale;

import org.slim3.tester.JDOControllerTestCase;

public class IndexControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        start("/locale/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/locale/index.jsp", getNextPath());
    }
}
