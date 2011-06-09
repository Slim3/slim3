package slim3.demo.controller.performance;

import java.util.Iterator;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.model.BarObjectify;
import slim3.demo.cool.service.PerformanceService;

public class GetObjectifyController extends Controller {

    private PerformanceService service = new PerformanceService();

    @Override
    public Navigation run() throws Exception {
        long start = System.currentTimeMillis();
        for (Iterator<BarObjectify> i = service.getBarsUsingObjectify(); i
            .hasNext();) {
            BarObjectify bar = i.next();
            bar.getKey();
            bar.getSortValue();
        }
        sessionScope("getObjectify", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
