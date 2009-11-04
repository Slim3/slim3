package slim3.demo.controller.upload;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.service.UploadService;

public class DeleteController extends Controller {

    private UploadService service = new UploadService();

    @Override
    public Navigation run() {
        service.delete(asKey("key"));
        return redirect(basePath);
    }
}
