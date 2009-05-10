package slim3.it.controller.hello;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class SayHelloController extends Controller {

    protected String name;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Navigation execute() {
        return forward();
    }
}