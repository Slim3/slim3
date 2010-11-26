package slim3.demo.controller.blobstore;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class ShowControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        String keyName = "hoge";
        tester.param("keyName", keyName);
        tester.start("/blobstore/show");
        ShowController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }
}
