package slim3.demo.controller.upload;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import slim3.demo.model.UploadedData;

public class DeleteControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.param("key", Datastore.keyToString(Datastore
            .put(new UploadedData())));
        tester.start("/upload/delete");
        DeleteController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/upload/"));
        assertThat(tester.count(UploadedData.class), is(0));
    }
}
