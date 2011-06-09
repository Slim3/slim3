package slim3.demo.controller.performance;

import java.util.Iterator;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.model.Bar;
import slim3.demo.cool.service.PerformanceService;

public class GetSlim3Controller extends Controller {

    private PerformanceService service = new PerformanceService();

    @Override
    public Navigation run() throws Exception {
        long start = System.currentTimeMillis();
        for (Iterator<Bar> i = service.getBarsUsingSlim3(); i.hasNext();) {
            Bar bar = i.next();
            bar.getKey();
            bar.getSortValue();
        }
        sessionScope("getSlim3", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
