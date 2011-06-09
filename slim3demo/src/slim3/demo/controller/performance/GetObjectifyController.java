package slim3.demo.controller.performance;

import java.util.Iterator;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.model.BarObjectify;
import slim3.demo.cool.service.PerformanceService;

public class GetObjectifyController extends Controller {

    private static final int COUNT = 5;

    private PerformanceService service = new PerformanceService();

    @Override
    public Navigation run() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            for (Iterator<BarObjectify> ite = service.getBarsUsingObjectify(); ite
                .hasNext();) {
                BarObjectify bar = ite.next();
                bar.getKey();
                bar.getSortValue();
            }
        }
        sessionScope("getObjectify", (System.currentTimeMillis() - start)
            / COUNT);
        return redirect(basePath);
    }
}
