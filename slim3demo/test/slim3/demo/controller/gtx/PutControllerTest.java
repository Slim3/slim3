package slim3.demo.controller.gtx;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class PutControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.requestScope("count", "1");
        tester.start("/gtx/put");
        PutController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/gtx/"));
    }
}
