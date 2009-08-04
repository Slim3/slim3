package slim3.it.controller.blog;

import org.slim3.tester.JDOControllerTestCase;

public class IndexControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        start("/blog/");
        IndexController controller = getController();
        assertNotNull(requestScope("blogList"));
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/blog/index.jsp", getDestinationPath());
    }
}