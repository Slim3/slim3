package slim3.it.controller.blog;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Blog;

public class EditControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        makePersistentInTx(blog);
        param("id", String.valueOf(blog.getKey().getId()));
        start("/blog/edit");
        EditController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/blog/edit.jsp", getNextPath());
        assertEquals(String.valueOf(blog.getKey().getId()), requestScope("id"));
        assertEquals(blog.getTitle(), requestScope("title"));
        assertEquals(blog.getContent(), requestScope("content"));
        assertEquals(1, count(Blog.class));
    }
}