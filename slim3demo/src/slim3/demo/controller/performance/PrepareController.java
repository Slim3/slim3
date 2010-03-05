package slim3.demo.controller.performance;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class PrepareController extends Controller {

    @Override
    public Navigation run() throws Exception {
        for (int j = 0; j < 5; j++) {
            List<Entity> list = new ArrayList<Entity>();
            Iterator<Key> keys = Datastore.allocateIds("Bar", 500).iterator();
            for (int i = 0; i < 500; i++) {
                Entity entity = new Entity(keys.next());
                entity.setProperty("sortValue", String.format(
                    "%019d",
                    new Date().getTime())
                    + KeyFactory.keyToString(entity.getKey()));
                list.add(entity);
            }
            Datastore.put(list);
        }
        return forward(basePath);
    }
}
