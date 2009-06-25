package slim3.it.controller.flexblog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import slim3.it.dao.BlogDao;
import slim3.it.model.Blog;

public class UpdateController extends Controller {

    private BlogDao dao = new BlogDao();

    @Override
    public Navigation run() {
        dao.begin();
        Blog blog = dao.find(key(), version());
        BeanUtil.copy(request, blog);
        dao.commit();
        return null;
    }
}
