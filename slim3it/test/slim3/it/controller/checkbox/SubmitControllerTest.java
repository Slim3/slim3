package slim3.it.controller.checkbox;

import org.slim3.tester.JDOControllerTestCase;

public class SubmitControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        param("aaa", "on");
        start("/checkbox/submit");
        SubmitController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/checkbox/", getNextPath());
        assertEquals(Boolean.TRUE, asBoolean("aaa"));
    }
}
