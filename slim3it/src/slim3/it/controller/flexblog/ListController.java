package slim3.it.controller.flexblog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.it.dao.BlogDao;

public class ListController extends Controller {

    private BlogDao dao = new BlogDao();

    @Override
    public Navigation run() {
        requestScope("blogList", dao.findAll());
        return forward("/flexblog/list.jsp");
    }
}
