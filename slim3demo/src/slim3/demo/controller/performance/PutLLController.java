package slim3.demo.controller.performance;

import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class PutLLController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(PutLLController.class.getName());

    @Override
    public Navigation run() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Entity entity = new Entity("FooLL");
            entity.setProperty("string01", "string" + i);
            entity.setProperty("string02", "string" + i);
            entity.setProperty("string03", "string" + i);
            entity.setProperty("string04", "string" + i);
            entity.setProperty("string05", "string" + i);
            entity.setProperty("string06", "string" + i);
            entity.setProperty("string07", "string" + i);
            entity.setProperty("string08", "string" + i);
            entity.setProperty("string09", "string" + i);
            entity.setProperty("string10", "string" + i);
            ds.put(entity);
        }
        sessionScope("putLL", System.currentTimeMillis() - start);
        return forward(basePath);
    }
}
