package slim3.it.controller.blog;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Blog;

import com.google.appengine.api.datastore.Key;

public class UpdateControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        pm.makePersistent(blog);
        setSessionAttribute("blog", blog);
        Key key = blog.getKey();
        setParameter("title", "aaa2");
        setParameter("content", "111");
        refreshPersistenceManager();
        start("/blog/update");
        UpdateController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/blog/", getNextPath());
        assertEquals("aaa2", getAttribute("title"));
        assertEquals("111", getAttribute("content"));
        blog = pm.getObjectById(Blog.class, key);
        assertNotNull(blog);
        assertEquals("aaa2", blog.getTitle());
        assertEquals("111", blog.getContent());
    }
}
