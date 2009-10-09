package slim3.demo.controller.performance;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class PrepareController extends Controller {

    private static final Logger logger =
        Logger.getLogger(PrepareController.class.getName());

    @Override
    public Navigation run() {
        List<Entity> list = new ArrayList<Entity>();
        long start = System.currentTimeMillis();
        Iterator<Key> keys = Datastore.allocateIds("Bar", 500).iterator();
        for (int i = 0; i < 500; i++) {
            Entity entity = new Entity(keys.next());
            entity.setProperty("sortValue", new Date().getTime()
                + KeyFactory.keyToString(entity.getKey()));
            list.add(entity);
        }
        Datastore.put(list);
        logger.log(Level.INFO, "batch put:"
            + (System.currentTimeMillis() - start));
        logger.log(Level.INFO, "Bar count:" + Datastore.query("Bar").count());
        return null;
    }
}
