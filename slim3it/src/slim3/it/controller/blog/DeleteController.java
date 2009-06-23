package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;

import slim3.it.dao.BlogDao;
import slim3.it.model.Blog;

public class DeleteController extends JDOController {

    @Override
    public Navigation run() {
        BlogDao dao = new BlogDao(pm);
        Blog blog = dao.find(asString("key"));
        dao.delete(blog);
        return redirect(basePath);
    }
}