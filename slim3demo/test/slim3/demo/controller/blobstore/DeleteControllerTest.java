package slim3.demo.controller.blobstore;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import slim3.demo.model.Blobstore;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;

public class DeleteControllerTest extends ControllerTestCase {

    @Test
    @Ignore
    public void run() throws Exception {
        String keyString = "hoge";
        tester.addBlobKey("formFile", keyString);
        BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> blobs = bs.getUploads(tester.request);
        List<BlobKey> blobKeyList = blobs.get("formFile");
        Key key = null;
        if (blobKeyList != null && blobKeyList.size() > 0) {
            key =
                Datastore.createKey(Blobstore.class, blobKeyList
                    .get(0)
                    .getKeyString());
            Blobstore blobstore = new Blobstore();
            blobstore.setKey(key);
            Datastore.put(blobstore);
        }
        tester.param("keyName", keyString);
        tester.start("/blobstore/delete");
        DeleteController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/blobstore/"));
        assertThat(Datastore.getOrNull(key), is(nullValue()));
    }
}
