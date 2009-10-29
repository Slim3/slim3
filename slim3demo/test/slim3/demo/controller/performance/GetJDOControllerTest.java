package slim3.demo.controller.performance;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class GetJDOControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/performance/getJDO");
        GetJDOController controller = tester.getController();
        assertThat(controller, is(not(nullValue())));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/performance/"));
        assertThat(tester.sessionScope("getJDO"), is(not(nullValue())));
        System.out.println("getJDO:" + tester.sessionScope("getJDO"));
    }
}
