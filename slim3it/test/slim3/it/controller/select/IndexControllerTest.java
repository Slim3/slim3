package slim3.it.controller.select;

import org.slim3.tester.JDOControllerTestCase;

public class IndexControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        start("/select/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/select/index.jsp", getNextPath());
    }
}
