package slim3.demo.controller.blobstore;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class ShowController extends Controller {

    @Override
    public Navigation run() throws Exception {
        String keyName = asString("keyName");
        BlobKey blobKey = new BlobKey(keyName);
        BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
        bs.serve(blobKey, response);
        return null;
    }
}
