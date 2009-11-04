package slim3.demo.controller.performance;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.service.PerformanceService;

public class GetSlim3Controller extends Controller {

    private PerformanceService service = new PerformanceService();

    @Override
    public Navigation run() {
        long start = System.currentTimeMillis();
        service.getBarListUsingSlim3();
        sessionScope("getSlim3", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
