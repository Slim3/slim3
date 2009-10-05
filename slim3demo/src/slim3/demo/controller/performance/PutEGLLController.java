package slim3.demo.controller.performance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class PutEGLLController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(PutEGLLController.class.getName());

    @Override
    public Navigation run() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            List<Entity> entities = new ArrayList<Entity>();
            Key parentKey = ds.allocateIds("Parent", 1).iterator().next();
            entities.add(new Entity(parentKey));
            Iterator<Key> ids =
                ds.allocateIds(parentKey, "Child", 10).iterator();
            for (int j = 0; j < 10; j++) {
                entities.add(new Entity(ids.next()));
            }
            Transaction tx = ds.beginTransaction();
            ds.put(tx, entities);
            tx.commit();
        }
        sessionScope("putEGLL", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
