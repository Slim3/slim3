package slim3.demo.controller.upload;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.meta.UploadedDataMeta;
import slim3.demo.model.UploadedData;
import slim3.demo.service.UploadService;

public class ShowController extends Controller {

    private UploadService service = new UploadService();

    private UploadedDataMeta meta = UploadedDataMeta.get();

    @Override
    public Navigation run() {
        UploadedData data =
            service.getData(asKey(meta.key), asLong(meta.version));
        byte[] bytes = service.getBytes(data);
        show(data.getFileName(), bytes);
        return null;
    }
}