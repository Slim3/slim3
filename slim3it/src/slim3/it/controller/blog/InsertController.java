package slim3.it.controller.blog;

import java.util.Date;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

public class InsertController extends JDOController {

    @Override
    public Navigation run() {
        if (!validate()) {
            return forward("create");
        }
        Blog2 blog = new Blog2();
        BeanUtil.copy(request, blog);
        blog.setCreatedAt(new Date());
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