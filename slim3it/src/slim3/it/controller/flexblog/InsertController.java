package slim3.it.controller.flexblog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import slim3.it.dao.BlogDao;
import slim3.it.model.Blog;

public class InsertController extends JDOController {

    @Override
    public Navigation run() {
        Blog blog = new Blog();
        BeanUtil.copy(request, blog);
        BlogDao dao = new BlogDao(pm);
        dao.insert(blog);
        return null;
    }
}
