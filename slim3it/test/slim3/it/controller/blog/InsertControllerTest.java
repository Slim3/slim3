package slim3.it.controller.blog;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.BlogMeta;

public class InsertControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        setParameter("title", "aaa");
        setParameter("content", "bbb");
        start("/blog/insert");
        InsertController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/blog/", getNextPath());
        assertEquals("aaa", getAttribute("title"));
        assertEquals("bbb", getAttribute("content"));
        BlogMeta m = new BlogMeta();
        assertEquals(1, from(m).getResultList().size());
    }
}
