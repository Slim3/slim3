package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import slim3.it.model.Blog;

public class UpdateController extends JDOController {

    @Override
    public Navigation run() {
        Blog blog = removeSessionScope("blog");
        if (blog == null) {
            throw new IllegalStateException("The blog instance is not found.");
        }
        BeanUtil.copy(request, blog);
        pm.makePersistent(blog);
        return redirect(basePath);
    }
}