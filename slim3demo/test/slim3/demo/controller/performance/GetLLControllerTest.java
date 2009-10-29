package slim3.demo.controller.performance;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class GetLLControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/performance/getLL");
        GetLLController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/performance/"));
        assertThat(tester.sessionScope("getLL"), is(notNullValue()));
        System.out.println("getLL:" + tester.sessionScope("getLL"));
    }
}
