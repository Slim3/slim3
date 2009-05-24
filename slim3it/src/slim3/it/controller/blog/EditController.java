package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import slim3.it.model.Blog;

public class EditController extends JDOController {

    @Override
    public Navigation run() {
        Blog blog = pm.getObjectById(Blog.class, requestScope("key"));
        BeanUtil.copy(blog, request);
        return forward("/blog/edit.jsp");
    }
}