package slim3.demo.controller.performance;

import java.util.Iterator;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.service.PerformanceService;

import com.google.appengine.api.datastore.Entity;

public class GetLLController extends Controller {

    private PerformanceService service = new PerformanceService();

    @Override
    public Navigation run() throws Exception {
        long start = System.currentTimeMillis();
        for (Iterator<Entity> i = service.getBarsUsingLL(); i.hasNext();) {
            Entity e = i.next();
            e.getKey();
            e.getProperty("sortValue");
        }
        sessionScope("getLL", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
