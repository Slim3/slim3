package slim3.demo.controller.performance;

import java.util.List;
import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import slim3.demo.meta.FooMeta;

import com.google.appengine.api.datastore.Key;

public class DeleteSlim3Controller extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(DeleteSlim3Controller.class.getName());

    @Override
    public Navigation run() {
        FooMeta f = new FooMeta();
        List<Key> keys = Datastore.query(f).asKeyList();
        long start = System.currentTimeMillis();
        for (Key key : keys) {
            Datastore.delete(key);
        }
        sessionScope("deleteSlim3", System.currentTimeMillis() - start);
        return forward(basePath);
    }
}
