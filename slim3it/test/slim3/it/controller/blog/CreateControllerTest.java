package slim3.it.controller.blog;

import org.slim3.tester.ControllerDatastoreTestCase;

public class CreateControllerTest extends ControllerDatastoreTestCase {

    public void testExecute() throws Exception {
        start("/blog/create");
        CreateController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/blog/create.jsp", getNextPath());
    }
}
