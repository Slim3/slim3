package slim3.it.controller.flexblog;

import org.slim3.tester.JDOControllerTestCase;

public class ListControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        start("/flexblog/list");
        ListController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/flexblog/list.jsp", getDestinationPath());
        assertNotNull(requestScope("blogList"));
    }
}
