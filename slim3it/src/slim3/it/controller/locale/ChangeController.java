package slim3.it.controller.locale;

import org.slim3.controller.ControllerConstants;
import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;

public class ChangeController extends JDOController {

    @Override
    public Navigation run() {
        sessionScope(ControllerConstants.LOCALE_KEY, requestScope("locale"));
        return redirect(basePath);
    }
}
