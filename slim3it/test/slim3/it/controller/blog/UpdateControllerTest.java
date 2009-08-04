package slim3.it.controller.blog;

import org.slim3.controller.validator.Errors;
import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Blog;

public class UpdateControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        makePersistentInTx(blog);
        param("key", blog.getKey());
        param("title", "aaa2");
        param("content", "222");
        param("version", blog.getVersion());
        start("/blog/update");
        UpdateController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/blog/", getDestinationPath());
        blog = pm.getObjectById(Blog.class, blog.getKey());
        assertNotNull(blog);
        assertEquals("aaa2", blog.getTitle());
        assertEquals("222", blog.getContent());
    }

    public void testValidate() throws Exception {
        start("/blog/update");
        UpdateController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/blog/edit.jsp", getDestinationPath());
        Errors errors = getErrors();
        assertNotNull(errors.get("title"));
        assertNotNull(errors.get("content"));
    }
}
