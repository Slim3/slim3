package slim3.demo.controller.select;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class IndexController extends Controller {

    @Override
    public Navigation run() throws Exception {
        if (isGet()) {
            requestScope("aaa", "2");
        }
        return forward("index.jsp");
    }
}
