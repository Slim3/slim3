package slim3.demo.controller.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import slim3.demo.meta.ChildMeta;
import slim3.demo.meta.ParentMeta;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class DeleteEGSlim3Controller extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(DeleteEGSlim3Controller.class.getName());

    @Override
    public Navigation run() {
        ParentMeta p = new ParentMeta();
        ChildMeta c = new ChildMeta();
        long start = System.currentTimeMillis();
        List<Key> parentKeys = Datastore.query(p).asKeyList();
        for (Key key : parentKeys) {
            List<Key> keys = new ArrayList<Key>();
            keys.add(key);
            List<Key> childKeys = Datastore.query(c, key).asKeyList();
            keys.addAll(childKeys);
            Transaction tx = Datastore.beginTransaction();
            Datastore.delete(tx, keys);
            Datastore.commit(tx);
        }
        sessionScope("deleteEGSlim3", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
