package slim3.demo.controller.blobstore;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class UploadController extends Controller {

    @Override
    public Navigation run() throws Exception {
        return redirect(basePath);
    }
}
