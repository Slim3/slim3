package slim3.demo.controller.performance;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class PrepareControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/performance/prepare");
        PrepareController controller = tester.getController();
        assertThat(controller, is(not(nullValue())));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/performance/"));
        assertThat(tester.count("Bar"), is(2500));
    }
}
