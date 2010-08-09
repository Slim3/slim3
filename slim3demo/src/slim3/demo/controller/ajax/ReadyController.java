package slim3.demo.controller.ajax;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class ReadyController extends Controller {

    @Override
    public Navigation run() throws Exception {
        response.getWriter().write("Ready");
        return null;
    }
}
