package slim3.demo.controller.blobstore;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import slim3.demo.model.Blobstore;

public class IndexController extends Controller {

    @Override
    public Navigation run() throws Exception {
        requestScope("dataList", Datastore.query(Blobstore.class).asList());
        return forward("index.jsp");
    }
}
