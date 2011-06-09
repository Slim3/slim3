package slim3.demo.controller.performance;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.model.BarJDO;
import slim3.demo.cool.service.PerformanceService;

public class GetJDOController extends Controller {

    private static final int COUNT = 5;

    private PerformanceService service = new PerformanceService();

    @Override
    public Navigation run() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            for (BarJDO bar : service.getBarsUsingJDO()) {
                bar.getKey();
                bar.getSortValue();
            }
        }
        sessionScope("getJDO", (System.currentTimeMillis() - start) / COUNT);
        return redirect(basePath);
    }
}
