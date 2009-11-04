package slim3.demo.controller.upload;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.model.UploadedData;
import slim3.demo.service.UploadService;

public class DownloadController extends Controller {

    private UploadService service = new UploadService();

    @Override
    public Navigation run() {
        UploadedData data = service.getData(asKey("key"), asLong("version"));
        byte[] bytes = service.getBytes(data);
        download(data.getFileName(), bytes);
        return null;
    }
}