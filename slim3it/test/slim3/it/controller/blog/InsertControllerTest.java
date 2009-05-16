package slim3.it.controller.blog;

import org.slim3.jdo.JDOTemplate;
import org.slim3.tester.ControllerDatastoreTestCase;

import slim3.it.model.BlogMeta;

public class InsertControllerTest extends ControllerDatastoreTestCase {

    public void testExecute() throws Exception {
        setParameter("title", "aaa");
        setParameter("content", "bbb");
        start("/blog/insert");
        InsertController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/blog/", getNextPath());
        assertEquals("aaa", controller.getTitle());
        assertEquals("bbb", controller.getContent());
        new JDOTemplate() {
            @Override
            public void doRun() {
                BlogMeta m = new BlogMeta();
                assertEquals(1, from(m).getResultList().size());
            }
        }.run();
    }
}
