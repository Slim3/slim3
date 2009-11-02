package slim3.demo.controller.blog;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class CreateControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/blog/create");
        CreateController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/blog/create.jsp"));
    }
}
