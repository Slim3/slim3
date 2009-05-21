package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import slim3.it.model.Blog;

public class InsertController extends JDOController {

    @Override
    public Navigation run() {
        Blog blog = new Blog();
        BeanUtil.copy(request, blog);
        makePersistentInTx(blog);
        return redirect(basePath);
    }
}