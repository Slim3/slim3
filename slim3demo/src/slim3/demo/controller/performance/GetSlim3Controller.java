package slim3.demo.controller.performance;

import java.util.List;
import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import slim3.demo.meta.BarMeta;
import slim3.demo.model.Bar;

public class GetSlim3Controller extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(GetSlim3Controller.class.getName());

    @Override
    public Navigation run() {
        BarMeta b = new BarMeta();
        long start = System.currentTimeMillis();
        List<Bar> list = Datastore.query(b).asList();
        for (Bar bar : list) {
            bar.getKey();
        }
        sessionScope("getSlim3", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
