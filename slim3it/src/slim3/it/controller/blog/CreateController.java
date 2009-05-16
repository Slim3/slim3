package slim3.it.controller.blog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class CreateController extends Controller {

    @Override
    public Navigation execute() {
        return forward("/blog/create.jsp");
    }
}
