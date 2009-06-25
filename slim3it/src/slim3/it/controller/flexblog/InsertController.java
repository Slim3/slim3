package slim3.it.controller.flexblog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import slim3.it.dao.BlogDao;
import slim3.it.model.Blog;

public class InsertController extends Controller {

    private BlogDao dao = new BlogDao();

    @Override
    public Navigation run() {
        Blog blog = new Blog();
        BeanUtil.copy(request, blog);
        dao.makePersistentInTx(blog);
        return null;
    }
}
