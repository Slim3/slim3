package slim3.demo.controller.timezone;

import java.util.Date;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.TimeZoneLocator;

public class IndexController extends Controller {

    @Override
    public Navigation run() throws Exception {
        requestScope("now", new Date());
        requestScope("timeZone", TimeZoneLocator.get().getID());
        return forward("index.jsp");
    }
}
