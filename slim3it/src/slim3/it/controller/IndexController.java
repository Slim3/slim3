package slim3.it.controller;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;

public class IndexController extends JDOController {

    @Override
    public Navigation run() {
        return forward("index.jsp");
    }
}