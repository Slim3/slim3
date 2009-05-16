package slim3.it.controller.blog;

import org.slim3.tester.ControllerDatastoreTestCase;

public class IndexControllerTest extends ControllerDatastoreTestCase {

    public void testExecute() throws Exception {
        start("/blog/");
        IndexController controller = getController();
        assertNotNull(controller.getBlogList());
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/blog/index.jsp", getNextPath());
    }
}
