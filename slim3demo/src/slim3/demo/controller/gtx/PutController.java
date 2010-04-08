package slim3.demo.controller.gtx;

import java.util.List;
import java.util.Map;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Transaction;

public class PutController extends Controller {

    @Override
    public Navigation run() throws Exception {
        int count = asInteger("count");
        List<Map<String, Long>> list = sessionScope("gtxResultList");
        Map<String, Long> map = list.get(count - 1);
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Transaction tx = Datastore.beginTransaction();
            Datastore.put(tx, new Entity("Hoge"));
            tx.commit();
        }
        long time = System.currentTimeMillis() - start;
        map.put("tx", time);

        start = System.currentTimeMillis();
        GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        for (int i = 0; i < count; i++) {
            gtx.put(new Entity("Hoge"));
        }
        gtx.commit();
        time = System.currentTimeMillis() - start;
        map.put("gtx", time);
        Datastore.delete(Datastore.query("Hoge").asKeyList());
        return redirect(basePath);
    }
}
