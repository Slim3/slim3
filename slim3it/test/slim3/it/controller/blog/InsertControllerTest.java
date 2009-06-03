package slim3.it.controller.blog;

import org.slim3.controller.Errors;
import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Blog;

public class InsertControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        param("title", "aaa");
        param("content", "bbb");
        start("/blog/insert");
        InsertController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/blog/", getNextPath());
        assertEquals(1, count(Blog.class));
    }

    public void testValidate() throws Exception {
        start("/blog/insert");
        InsertController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/blog/create", getNextPath());
        Errors errors = getErrors();
        assertNotNull(errors.get("title"));
        assertNotNull(errors.get("content"));
        assertEquals(0, count(Blog.class));
    }
}
