package slim3.it.controller.checkbox;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class IndexController extends Controller {

    @Override
    public Navigation run() {
        if (isGet()) {
            requestScope("aaa", "on");
        }
        return forward("index.jsp");
    }
}
