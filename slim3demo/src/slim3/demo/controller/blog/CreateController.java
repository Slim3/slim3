package slim3.demo.controller.blog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class CreateController extends Controller {

    @Override
    public Navigation run() throws Exception {
        return forward("/blog/create.jsp");
    }
}