package slim3.it.controller;

import org.slim3.mvc.controller.Controller;
import org.slim3.mvc.controller.Navigation;

public class IndexController extends Controller {

    @Override
    public Navigation execute() {
        return forward("/hello/index");
    }
}