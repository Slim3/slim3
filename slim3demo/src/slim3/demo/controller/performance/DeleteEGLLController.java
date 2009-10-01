package slim3.demo.controller.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

public class DeleteEGLLController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(DeleteEGLLController.class.getName());

    @Override
    public Navigation run() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        long start = System.currentTimeMillis();
        List<Entity> entities =
            ds.prepare(new Query("Parent").setKeysOnly()).asList(
                FetchOptions.Builder.withOffset(0));
        for (Entity entity : entities) {
            List<Key> keys = new ArrayList<Key>();
            keys.add(entity.getKey());
            List<Entity> entities2 =
                ds
                    .prepare(new Query("Child", entity.getKey()).setKeysOnly())
                    .asList(FetchOptions.Builder.withOffset(0));
            for (Entity entity2 : entities2) {
                keys.add(entity2.getKey());
            }
            Transaction tx = ds.beginTransaction();
            ds.delete(tx, keys);
            tx.commit();
        }
        sessionScope("deleteEGLL", System.currentTimeMillis() - start);
        return forward(basePath);
    }
}
