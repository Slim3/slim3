package slim3.it.controller.timezone;

import java.util.Date;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class IndexController extends Controller {

    @Override
    public Navigation run() {
        requestScope("now", new Date());
        return forward("index.jsp");
    }
}
