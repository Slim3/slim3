package slim3.demo.controller.performance;

import java.util.Iterator;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.service.PerformanceService;

import com.google.appengine.api.datastore.Entity;

public class GetLLController extends Controller {

    private static final int COUNT = 5;

    private PerformanceService service = new PerformanceService();

    @Override
    public Navigation run() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            for (Iterator<Entity> ite = service.getBarsUsingLL(); ite.hasNext();) {
                Entity e = ite.next();
                e.getKey();
                e.getProperty("sortValue");
            }
        }
        sessionScope("getLL", (System.currentTimeMillis() - start) / COUNT);
        return redirect(basePath);
    }
}
