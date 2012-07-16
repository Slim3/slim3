package slim3.demo.controller.blobstore;

import java.util.List;
import java.util.Map;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import slim3.demo.model.Blobstore;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;

public class UploadController extends Controller {

    @Override
    public Navigation run() throws Exception {
        BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> blobs = bs.getUploads(request);
        List<BlobKey> blobKeyList = blobs.get("formFile");
        if (blobKeyList != null && blobKeyList.size() > 0) {
            Key key =
                Datastore.createKey(Blobstore.class, blobKeyList
                    .get(0)
                    .getKeyString());
            Blobstore blobstore = new Blobstore();
            blobstore.setKey(key);
            Datastore.put(blobstore);
        }
        return redirect(basePath);
    }
}
