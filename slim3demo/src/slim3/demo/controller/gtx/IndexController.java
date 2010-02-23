package slim3.demo.controller.gtx;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class IndexController extends Controller {

    @Override
    public Navigation run() {
        Datastore.delete(Datastore.query("Hoge").asKeyList());
        int count = 10;
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Transaction tx = Datastore.beginTransaction();
            Key key = Datastore.createKey("Hoge", i + 1);
            Datastore.put(tx, new Entity(key));
            tx.commit();
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("tx time:" + time);
        requestScope("txtime", time);
        Datastore.delete(Datastore.query("Hoge").asKeyList());

        start = System.currentTimeMillis();
        GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        for (int i = 0; i < count; i++) {
            Key key = Datastore.createKey("Hoge", i + 1);
            gtx.put(new Entity(key));
        }
        gtx.commit();
        time = System.currentTimeMillis() - start;
        System.out.println("gtx time:" + time);
        requestScope("gtxtime", time);
        return forward("index.jsp");
    }
}