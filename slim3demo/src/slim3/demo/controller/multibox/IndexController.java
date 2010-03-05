package slim3.demo.controller.multibox;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class IndexController extends Controller {

    @Override
    public Navigation run() throws Exception {
        if (isGet()) {
            requestScope("aaaArray", new String[] { "111" });
        }
        return forward("index.jsp");
    }
}
