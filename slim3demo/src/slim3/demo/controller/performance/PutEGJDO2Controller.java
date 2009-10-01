package slim3.demo.controller.performance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.jdo.PMF;
import slim3.demo.cool.model.ChildJDO2;
import slim3.demo.cool.model.ParentJDO;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;

public class PutEGJDO2Controller extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(PutEGJDO2Controller.class.getName());

    @Override
    public Navigation run() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            List<Object> models = new ArrayList<Object>();
            Key parentKey = ds.allocateIds("Parent", 1).iterator().next();
            ParentJDO parent = new ParentJDO();
            parent.setKey(parentKey);
            models.add(parent);
            Iterator<Key> ids =
                ds.allocateIds(parentKey, "Child", 10).iterator();
            for (int j = 0; j < 10; j++) {
                ChildJDO2 child = new ChildJDO2();
                child.setKey(ids.next());
                models.add(child);
            }
            pm.currentTransaction().begin();
            pm.makePersistentAll(models);
            pm.currentTransaction().commit();
        }
        pm.close();
        sessionScope("putEGJDO2", System.currentTimeMillis() - start);
        return forward(basePath);
    }
}
