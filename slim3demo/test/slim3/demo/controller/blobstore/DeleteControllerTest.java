package slim3.demo.controller.blobstore;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import slim3.demo.model.Blobstore;

import com.google.appengine.api.datastore.Key;

public class DeleteControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        String keyName = "hoge";
        Blobstore blobstore = new Blobstore();
        Key key = Datastore.createKey(Blobstore.class, keyName);
        blobstore.setKey(key);
        Datastore.put(blobstore);
        tester.param("keyName", keyName);
        tester.start("/blobstore/delete");
        DeleteController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/blobstore/"));
        assertThat(Datastore.getOrNull(key), is(nullValue()));
    }
}
