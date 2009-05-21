package slim3.it.controller.blog;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Blog;

import com.google.appengine.api.datastore.Key;

public class DeleteControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        makePersistentInTx(blog);
        Key key = blog.getKey();
        param("id", String.valueOf(key.getId()));
        start("/blog/delete");
        DeleteController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/blog/", getNextPath());
        assertEquals(0, count(Blog.class));
    }
}
