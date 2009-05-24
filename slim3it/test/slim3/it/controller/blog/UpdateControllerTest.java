package slim3.it.controller.blog;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Blog;

public class UpdateControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        makePersistentInTx(blog);
        sessionScope("blog", blog);
        param("key", blog.getKey());
        param("title", "aaa2");
        param("content", "222");
        start("/blog/update");
        UpdateController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/blog/", getNextPath());
        blog = pm.getObjectById(Blog.class, blog.getKey());
        assertNotNull(blog);
        assertEquals("aaa2", blog.getTitle());
        assertEquals("222", blog.getContent());
    }
}
