package slim3.it.controller.timezone;

import java.util.Date;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;

public class IndexController extends JDOController {

    @Override
    public Navigation run() {
        requestScope("now", new Date());
        return forward("index.jsp");
    }
}
