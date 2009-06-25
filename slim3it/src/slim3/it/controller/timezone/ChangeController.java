package slim3.it.controller.timezone;

import org.slim3.controller.Controller;
import org.slim3.controller.ControllerConstants;
import org.slim3.controller.Navigation;

public class ChangeController extends Controller {

    @Override
    public Navigation run() {
        sessionScope(
            ControllerConstants.TIME_ZONE_KEY,
            requestScope("timeZone"));
        return redirect(basePath);
    }
}
