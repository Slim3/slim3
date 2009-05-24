package slim3.it.controller.flexblog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import slim3.it.model.Blog;

public class UpdateController extends JDOController {

    @Override
    public Navigation run() {
        tx.begin();
        Blog blog = pm.getObjectById(Blog.class, requestScope("key"));
        BeanUtil.copy(request, blog);
        pm.makePersistent(blog);
        tx.commit();
        return null;
    }
}
