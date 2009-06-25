package slim3.it.controller.upload;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.it.dao.UploadDao;

public class IndexController extends Controller {

    private UploadDao dao = new UploadDao();

    @Override
    public Navigation run() {
        requestScope("uploadList", dao.findAll());
        return forward("index.jsp");
    }
}
