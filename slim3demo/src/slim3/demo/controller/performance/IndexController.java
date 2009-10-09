package slim3.demo.controller.performance;

import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

public class IndexController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(IndexController.class.getName());

    @Override
    public Navigation run() {
        requestScope("count", Datastore.query("Bar").count());
        return forward("index.jsp");
    }
}
