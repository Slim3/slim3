package slim3.demo.controller.performance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import slim3.demo.model.Child;
import slim3.demo.model.Parent;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class PutEGSlim3Controller extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(PutEGSlim3Controller.class.getName());

    @Override
    public Navigation run() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            List<Object> models = new ArrayList<Object>();
            Key parentKey = ds.allocateIds("Parent", 1).iterator().next();
            Parent parent = new Parent();
            parent.setKey(parentKey);
            models.add(parent);
            Iterator<Key> ids =
                ds.allocateIds(parentKey, "Child", 10).iterator();
            for (int j = 0; j < 10; j++) {
                Child child = new Child();
                child.setKey(ids.next());
                models.add(child);
            }
            Transaction tx = ds.beginTransaction();
            Datastore.put(tx, models);
            tx.commit();
        }
        sessionScope("putEGSlim3", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
