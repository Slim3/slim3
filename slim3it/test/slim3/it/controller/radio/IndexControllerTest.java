package slim3.it.controller.radio;

import org.slim3.tester.JDOControllerTestCase;

public class IndexControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        start("/radio/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/radio/index.jsp", getNextPath());
    }
}
