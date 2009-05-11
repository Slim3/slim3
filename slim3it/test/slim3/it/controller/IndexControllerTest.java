package slim3.it.controller;

import org.slim3.tester.ControllerDatastoreTestCase;

public class IndexControllerTest extends ControllerDatastoreTestCase {

    public void testExecute() throws Exception {
        start("/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/index.jsp", getNextPath());
    }
}