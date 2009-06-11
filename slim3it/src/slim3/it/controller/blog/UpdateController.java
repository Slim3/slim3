package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

public class UpdateController extends JDOController {

    @Override
    public Navigation run() {
        if (!validate()) {
            return forward("edit.jsp");
        }
        Blog2 blog = pm.getObjectById(Blog2.class, requestScope("key"));
        BeanUtil.copy(request, blog);
        pm.makePersistent(blog);
        return redirect(basePath);
    }

    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("title", v.required());
        v.add("content", v.required());
        return v.validate();
    }
}