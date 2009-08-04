package slim3.it.controller.upload;

import org.slim3.tester.JDOControllerTestCase;

public class IndexControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        start("/upload/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/upload/index.jsp", getDestinationPath());
        assertNotNull(requestScope("uploadList"));
    }
}
