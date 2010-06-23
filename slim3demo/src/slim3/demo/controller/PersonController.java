package slim3.demo.controller;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class PersonController extends Controller {

    @Override
    public Navigation run() throws Exception {
        System.out.println(asString("method"));
        return forward("person.jsp");
    }
}
