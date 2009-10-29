package slim3.demo.controller.performance;

import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.service.PerformanceService;

public class GetLLController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(GetLLController.class.getName());

    private PerformanceService service = new PerformanceService();

    @Override
    public Navigation run() {
        long start = System.currentTimeMillis();
        service.getBarListUsingLL();
        sessionScope("getLL", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
