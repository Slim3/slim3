package slim3.demo.controller.blog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.RequestMap;

import slim3.demo.service.BlogService;

public class UpdateController extends Controller {

    private BlogService service = new BlogService();

    @Override
    public Navigation run() {
        if (!validate()) {
            return forward("edit.jsp");
        }
        service
            .update(asKey("key"), asLong("version"), new RequestMap(request));
        return redirect(basePath);
    }

    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("title", v.required());
        v.add("content", v.required());
        return v.validate();
    }
}