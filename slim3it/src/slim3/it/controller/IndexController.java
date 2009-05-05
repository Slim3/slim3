package slim3.it.controller;

import org.slim3.mvc.controller.Controller;
import org.slim3.mvc.controller.Navigation;

public class IndexController extends Controller {

    public String getAaa() {
        return "123";
    }

    @Override
    public Navigation execute() {
        return forward();
    }
}