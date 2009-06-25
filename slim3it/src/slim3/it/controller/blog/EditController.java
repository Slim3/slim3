package slim3.it.controller.blog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import slim3.it.dao.BlogDao;
import slim3.it.model.Blog;

public class EditController extends Controller {

    private BlogDao dao = new BlogDao();

    @Override
    public Navigation run() {
        Blog blog = dao.find(key(), version());
        BeanUtil.copy(blog, request);
        return forward("/blog/edit.jsp");
    }
}