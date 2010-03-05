package slim3.demo.controller.upload;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.meta.UploadedDataMeta;
import slim3.demo.service.UploadService;

public class DeleteController extends Controller {

    private UploadService service = new UploadService();

    private UploadedDataMeta meta = UploadedDataMeta.get();

    @Override
    public Navigation run() throws Exception {
        service.delete(asKey(meta.key));
        return redirect(basePath);
    }
}
