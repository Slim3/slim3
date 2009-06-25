package slim3.it.controller.blog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.it.dao.BlogDao;

public class IndexController extends Controller {

    private BlogDao dao = new BlogDao();

    @Override
    public Navigation run() {
        requestScope("blogList", dao.findTopTen());
        return forward("/blog/index.jsp");
    }
}