package slim3.it.controller.flexblog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;

import slim3.it.dao.BlogDao;

public class ListController extends JDOController {

    @Override
    public Navigation run() {
        BlogDao dao = new BlogDao(pm);
        requestScope("blogList", dao.findAllAsMapList());
        return forward("/flexblog/list.jsp");
    }
}
