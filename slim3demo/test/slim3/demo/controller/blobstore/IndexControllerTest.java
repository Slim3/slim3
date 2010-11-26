package slim3.demo.controller.blobstore;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import slim3.demo.model.Blobstore;

public class IndexControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/blobstore/");
        IndexController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/blobstore/index.jsp"));
    }

    @Test
    public void runAfterUpload() throws Exception {
        String keyStr = "hoge";
        Blobstore blobstore = new Blobstore();
        blobstore.setKey(Datastore.createKey(Blobstore.class, keyStr));
        Datastore.put(blobstore);
        tester.start("/blobstore/");
        IndexController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/blobstore/index.jsp"));
        assertThat(tester.requestScope("dataList"), is(notNullValue()));
    }
}
