package slim3.demo.controller.timezone;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.controller.ControllerConstants;
import org.slim3.tester.ControllerTestCase;

public class ChangeControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.param("timeZone", "PST");
        tester.start("/timezone/change");
        ChangeController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/timezone/"));
        assertThat((String) tester
            .sessionScope(ControllerConstants.TIME_ZONE_KEY), is("PST"));
    }
}
