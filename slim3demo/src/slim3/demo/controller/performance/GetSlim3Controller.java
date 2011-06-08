package slim3.demo.controller.performance;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.model.Bar;
import slim3.demo.cool.service.PerformanceService;

public class GetSlim3Controller extends Controller {

    private PerformanceService service = new PerformanceService();

    @Override
    public Navigation run() throws Exception {
        long start = System.currentTimeMillis();
        for (Bar bar : service.getBarListUsingSlim3()) {
            bar.getKey();
            bar.getSortValue();
        }
        sessionScope("getSlim3", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
