package slim3.it.controller.flexblog;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Blog;

public class UpdateControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        makePersistentInTx(blog);
        param("id", String.valueOf(blog.getKey().getId()));
        param("title", "bbb");
        param("content", "222");
        start("/flexblog/update");
        UpdateController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertNull(getNextPath());
        Blog blog2 = pm.getObjectById(Blog.class, blog.getKey());
        assertEquals("bbb", blog2.getTitle());
        assertEquals("222", blog2.getContent());
    }
}
