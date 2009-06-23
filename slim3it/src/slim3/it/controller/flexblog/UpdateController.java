package slim3.it.controller.flexblog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import slim3.it.dao.BlogDao;
import slim3.it.model.Blog;

public class UpdateController extends JDOController {

    @Override
    public Navigation run() {
        BlogDao dao = new BlogDao(pm);
        Blog blog = dao.find(asString("key"));
        BeanUtil.copy(request, blog);
        dao.update(blog);
        return null;
    }
}
