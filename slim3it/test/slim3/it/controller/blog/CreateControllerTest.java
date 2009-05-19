package slim3.it.controller.blog;

import org.slim3.tester.JDOControllerTestCase;

public class CreateControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        start("/blog/create");
        CreateController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/blog/create.jsp", getNextPath());
    }
}
