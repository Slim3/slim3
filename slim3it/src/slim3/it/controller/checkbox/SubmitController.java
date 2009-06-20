package slim3.it.controller.checkbox;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;

public class SubmitController extends JDOController {

    @Override
    public Navigation run() {
        return forward(basePath);
    }
}