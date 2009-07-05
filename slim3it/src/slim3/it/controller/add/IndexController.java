package slim3.it.controller.add;

import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

public class IndexController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(IndexController.class.getName());

    @Override
    public Navigation run() {
        if (isPost() && validate()) {
            requestScope("result", asInteger("arg1") + asInteger("arg2"));
        }
        return forward("index.jsp");
    }

    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("arg1", v.required(), v.integerType());
        v.add("arg2", v.required(), v.integerType());
        return v.validate();
    }
}
