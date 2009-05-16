package slim3.it.controller.blog;

import org.slim3.jdo.JDOTemplate;
import org.slim3.tester.ControllerDatastoreTestCase;

import slim3.it.model.Blog;
import slim3.it.model.BlogMeta;

public class EditControllerTest extends ControllerDatastoreTestCase {

    private Blog blog;

    public void testExecute() throws Exception {
        new JDOTemplate() {
            @Override
            public void doRun() {
                blog = new Blog();
                blog.setTitle("aaa");
                blog.setContent("111");
                pm.makePersistent(blog);
            }
        }.run();
        setParameter("id", String.valueOf(blog.getKey().getId()));
        start("/blog/edit");
        EditController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/blog/edit.jsp", getNextPath());
        assertEquals(String.valueOf(blog.getKey().getId()), controller.getId());
        assertEquals(blog.getTitle(), controller.getTitle());
        assertEquals(blog.getContent(), controller.getContent());
        assertNotNull(getSessionAttribute("blog"));
        new JDOTemplate() {
            @Override
            public void doRun() {
                BlogMeta m = new BlogMeta();
                assertEquals(1, from(m).getResultList().size());
            }
        }.run();
    }
}