package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.controller.Validators;
import org.slim3.util.BeanUtil;

import slim3.it.model.Blog;

public class UpdateController extends JDOController {

    @Override
    public Navigation run() {
        if (!validate()) {
            return forward("edit.jsp");
        }
        Blog blog = pm.getObjectById(Blog.class, requestScope("key"));
        tx.begin();
        BeanUtil.copy(request, blog);
        pm.makePersistent(blog);
        tx.commit();
        return redirect(basePath);
    }

    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("title", v.required());
        v.add("content", v.required());
        return v.validate();
    }
}