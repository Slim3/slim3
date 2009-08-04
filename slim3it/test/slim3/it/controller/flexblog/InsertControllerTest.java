package slim3.it.controller.flexblog;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Blog;

public class InsertControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        param("title", "aaa");
        param("content", "111");
        start("/flexblog/insert");
        InsertController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertNull(getDestinationPath());
        assertEquals(1, count(Blog.class));
    }
}
