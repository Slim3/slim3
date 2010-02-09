package slim3.demo.controller.gtx;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import com.google.appengine.api.datastore.Entity;

public class IndexController extends Controller {

    @Override
    public Navigation run() {
        Datastore.delete(Datastore.query("Hoge").asKeyList());
        int count = 100;

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            GlobalTransaction gtx = Datastore.beginGlobalTransaction();
            gtx.put(new Entity("Hoge"));
            gtx.put(new Entity("Hoge"));
            gtx.commit();
        }
        System.out.println("GlobalTransaction:"
            + (System.currentTimeMillis() - start));
        System.out.println("count:" + Datastore.query("Hoge").count());
        Datastore.delete(Datastore.query("Hoge").asKeyList());
        return forward("index.jsp");
    }
}
