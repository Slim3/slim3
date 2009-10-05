package slim3.demo.controller.performance;

import java.util.List;
import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

public class GetLLController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(GetLLController.class.getName());

    @Override
    public Navigation run() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        long start = System.currentTimeMillis();
        List<Entity> entities =
            ds.prepare(new Query("Foo")).asList(
                FetchOptions.Builder.withOffset(0));
        for (Entity entity : entities) {
            entity.getKey();
        }
        sessionScope("getLL", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
