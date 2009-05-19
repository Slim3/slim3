package slim3.it.controller.blog;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Blog;
import slim3.it.model.BlogMeta;

public class EditControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        pm.makePersistent(blog);
        setParameter("id", String.valueOf(blog.getKey().getId()));
        start("/blog/edit");
        EditController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/blog/edit.jsp", getNextPath());
        assertEquals(String.valueOf(blog.getKey().getId()), getAttribute("id"));
        assertEquals(blog.getTitle(), getAttribute("title"));
        assertEquals(blog.getContent(), getAttribute("content"));
        assertNotNull(getSessionAttribute("blog"));
        BlogMeta m = new BlogMeta();
        assertEquals(1, from(m).getResultList().size());
    }
}