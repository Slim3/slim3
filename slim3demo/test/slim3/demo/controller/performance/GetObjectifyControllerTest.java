package slim3.demo.controller.performance;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class GetObjectifyControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/performance/getObjectify");
        GetObjectifyController controller = tester.getController();
        assertThat(controller, is(not(nullValue())));
        assertTrue(tester.isRedirect());
        assertThat(tester.getDestinationPath(), is("/performance/"));
        assertThat(tester.sessionScope("getObjectify"), is(not(nullValue())));
        System.out.println("getObjectify:"
            + tester.sessionScope("getObjectify"));
    }
}
