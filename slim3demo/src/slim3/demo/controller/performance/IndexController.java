package slim3.demo.controller.performance;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

public class IndexController extends Controller {

    @Override
    public Navigation run() throws Exception {
        requestScope("count", Datastore.query("Bar").count());
        return forward("index.jsp");
    }
}