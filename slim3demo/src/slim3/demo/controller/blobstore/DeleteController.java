package slim3.demo.controller.blobstore;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import slim3.demo.model.Blobstore;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class DeleteController extends Controller {

    @Override
    public Navigation run() throws Exception {
        String keyName = asString("keyName");
        BlobKey blobKey = new BlobKey(keyName);
        BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
        bs.delete(blobKey);
        Datastore.delete(Datastore.createKey(Blobstore.class, keyName));
        return redirect(basePath);
    }
}
