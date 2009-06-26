package slim3.it.controller.blog;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Blog;

public class EditControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        makePersistentInTx(blog);
        param("key", blog.getKey());
        param("version", blog.getVersion());
        start("/blog/edit");
        EditController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/blog/edit.jsp", getNextPath());
        assertEquals(blog.getKey(), key());
        assertEquals(blog.getTitle(), asString("title"));
        assertEquals(blog.getContent(), requestScope("content"));
        assertEquals(blog.getVersion(), version());
        assertEquals(1, count(Blog.class));
    }
}