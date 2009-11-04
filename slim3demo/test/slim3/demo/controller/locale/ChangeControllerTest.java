package slim3.demo.controller.locale;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.controller.ControllerConstants;
import org.slim3.tester.ControllerTestCase;

public class ChangeControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.param("locale", "en");
        tester.start("/locale/change");
        ChangeController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/locale/"));
        assertThat(
            (String) tester.sessionScope(ControllerConstants.LOCALE_KEY),
            is("en"));
    }
}
