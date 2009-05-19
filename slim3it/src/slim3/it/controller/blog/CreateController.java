package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;

public class CreateController extends JDOController {

    @Override
    public Navigation run() {
        return forward("/blog/create.jsp");
    }
}
