package slim3.demo.controller.gtx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

public class IndexController extends Controller {

    private static int COUNT = 5;

    @Override
    public Navigation run() throws Exception {
        List<Map<String, Long>> list = new ArrayList<Map<String, Long>>(COUNT);
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        TransactionOptions xgops = TransactionOptions.Builder.withXG(true);
        for (int entityGroups = 1; entityGroups <= COUNT; entityGroups++) {
            Map<String, Long> map = new HashMap<String, Long>();
            long start = System.currentTimeMillis();
            for (int i = 0; i < entityGroups; i++) {
                Transaction tx = ds.beginTransaction();
                ds.put(new Entity("Hoge"));
                tx.commit();
            }
            long time = System.currentTimeMillis() - start;
            map.put("tx", time);

            start = System.currentTimeMillis();
            Transaction xg = ds.beginTransaction(xgops);
            for (int i = 0; i < entityGroups; i++) {
                ds.put(new Entity("Hoge"));
            }
            xg.commit();
            time = System.currentTimeMillis() - start;
            map.put("xg", time);

            start = System.currentTimeMillis();
            GlobalTransaction gtx = Datastore.beginGlobalTransaction();
            for (int i = 0; i < entityGroups; i++) {
                gtx.put(new Entity("Hoge"));
            }
            gtx.commit();
            time = System.currentTimeMillis() - start;
            map.put("gtx", time);
            list.add(map);
        }
        requestScope("gtxResultList", list);
        Datastore.delete(Datastore.query("Hoge").asKeyList());
        return forward("index.jsp");
    }
}