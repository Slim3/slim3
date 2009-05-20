package slim3.it.controller.blog;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Blog;
import slim3.it.model.BlogMeta;

import com.google.appengine.api.datastore.Key;

public class DeleteControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        pm.makePersistent(blog);
        refreshPersistenceManager();
        Key key = blog.getKey();
        param("id", String.valueOf(key.getId()));
        start("/blog/delete");
        DeleteController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/blog/", getNextPath());
        BlogMeta meta = new BlogMeta();
        assertNull(from(meta).getSingleResult());
    }
}
