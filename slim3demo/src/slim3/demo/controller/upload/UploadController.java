package slim3.demo.controller.upload;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.upload.FileItem;

import slim3.demo.service.UploadService;

public class UploadController extends Controller {

    private UploadService service = new UploadService();

    @Override
    public Navigation run() {
        FileItem formFile = requestScope("formFile");
        service.upload(formFile);
        return redirect(basePath);
    }
}
