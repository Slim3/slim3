package slim3.it.controller.blog;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Blog;

import com.google.appengine.api.datastore.Key;

public class UpdateControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        makePersistentInTx(blog);
        sessionScope("blog", blog);
        Key key = blog.getKey();
        param("id", String.valueOf(key.getId()));
        param("title", "aaa2");
        param("content", "222");
        refreshPersistenceManager();
        start("/blog/update");
        UpdateController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/blog/", getNextPath());
        blog = pm.getObjectById(Blog.class, key);
        assertNotNull(blog);
        assertEquals("aaa2", blog.getTitle());
        assertEquals("222", blog.getContent());
    }
}
