package slim3.demo.controller.performance;

import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.service.PerformanceService;

public class GetJDOController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(GetJDOController.class.getName());

    private PerformanceService service = new PerformanceService();

    @Override
    public Navigation run() {
        long start = System.currentTimeMillis();
        service.getBarListUsingJDO();
        sessionScope("getJDO", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
