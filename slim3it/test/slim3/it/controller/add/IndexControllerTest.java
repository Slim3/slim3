package slim3.it.controller.add;

import org.slim3.tester.JDOControllerTestCase;

public class IndexControllerTest extends JDOControllerTestCase {

    public void testGet() throws Exception {
        start("/add/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/add/index.jsp", getDestinationPath());
        assertNull(getErrors().get("arg1"));
        assertNull(getErrors().get("arg2"));
    }

    public void testPost() throws Exception {
        request.setMethod("post");
        param("arg1", "1");
        param("arg2", "2");
        start("/add/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/add/index.jsp", getDestinationPath());
        assertEquals(3, requestScope("result"));
    }

    public void testPostForNoParam() throws Exception {
        request.setMethod("post");
        start("/add/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/add/index.jsp", getDestinationPath());
        assertNotNull(getErrors().get("arg1"));
        assertNotNull(getErrors().get("arg2"));
    }
}