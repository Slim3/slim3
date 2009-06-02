package slim3.it.controller.timezone;

import org.slim3.controller.ControllerConstants;
import org.slim3.tester.JDOControllerTestCase;

public class ChangeControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        param("timeZone", "PST");
        start("/timezone/change");
        ChangeController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/timezone/", getNextPath());
        assertEquals("PST", sessionScope(ControllerConstants.TIME_ZONE_KEY));
    }
}
