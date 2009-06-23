package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import slim3.it.dao.BlogDao;
import slim3.it.model.Blog;

public class EditController extends JDOController {

    @Override
    public Navigation run() {
        BlogDao dao = new BlogDao(pm);
        Blog blog = dao.find(asString("key"));
        BeanUtil.copy(blog, request);
        return forward("/blog/edit.jsp");
    }
}