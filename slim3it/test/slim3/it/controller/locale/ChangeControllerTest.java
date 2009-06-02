package slim3.it.controller.locale;

import org.slim3.controller.ControllerConstants;
import org.slim3.tester.JDOControllerTestCase;

public class ChangeControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        param("locale", "en");
        start("/locale/change");
        ChangeController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/locale/", getNextPath());
        assertEquals("en", sessionScope(ControllerConstants.LOCALE_KEY));
    }
}
