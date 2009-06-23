package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;

import slim3.it.dao.BlogDao;

public class IndexController extends JDOController {

    @Override
    public Navigation run() {
        BlogDao dao = new BlogDao(pm);
        requestScope("blogList", dao.findTopTenAsMapList());
        return forward("/blog/index.jsp");
    }
}