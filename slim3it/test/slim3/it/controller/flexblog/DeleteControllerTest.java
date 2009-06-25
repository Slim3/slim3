package slim3.it.controller.flexblog;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Blog;

public class DeleteControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        makePersistentInTx(blog);
        param("key", blog.getKey());
        param("version", blog.getVersion());
        start("/flexblog/delete");
        DeleteController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertNull(getNextPath());
        assertEquals(0, count(Blog.class));
    }
}
