package slim3.it.controller.flexblog;

import org.slim3.tester.JDOControllerTestCase;

public class IndexControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        start("/flexblog/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/flexblog/index.html", getNextPath());
    }
}
