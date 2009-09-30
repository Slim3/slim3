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

public class DeleteLLController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(DeleteLLController.class.getName());

    @Override
    public Navigation run() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        List<Entity> entities =
            ds.prepare(new Query("FooLL").setKeysOnly()).asList(
                FetchOptions.Builder.withOffset(0));
        long start = System.currentTimeMillis();
        for (Entity entity : entities) {
            ds.delete(entity.getKey());
        }
        sessionScope("deleteLL", System.currentTimeMillis() - start);
        return forward(basePath);
    }
}
