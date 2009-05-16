package slim3.it.controller.blog;

import org.slim3.jdo.JDOTemplate;
import org.slim3.tester.ControllerDatastoreTestCase;

import slim3.it.model.Blog;

import com.google.appengine.api.datastore.Key;

public class UpdateControllerTest extends ControllerDatastoreTestCase {

    private Key key;

    public void testExecute() throws Exception {
        new JDOTemplate() {
            @Override
            public void doRun() {
                Blog blog = new Blog();
                blog.setTitle("aaa");
                blog.setContent("111");
                pm.makePersistent(blog);
                setSessionAttribute("blog", blog);
                key = blog.getKey();
            }
        }.run();
        setParameter("title", "aaa2");
        setParameter("content", "111");
        start("/blog/update");
        UpdateController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/blog/", getNextPath());
        assertEquals("aaa2", controller.getTitle());
        assertEquals("111", controller.getContent());
        new JDOTemplate() {
            @Override
            public void doRun() {
                Blog blog = pm.getObjectById(Blog.class, key);
                assertNotNull(blog);
                assertEquals("aaa2", blog.getTitle());
                assertEquals("111", blog.getContent());
            }
        }.run();
    }
}
