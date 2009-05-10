package slim3.it.controller.hello;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class IndexController extends Controller {

    @Override
    public Navigation execute() {
        return forward("index.jsp");
    }
}