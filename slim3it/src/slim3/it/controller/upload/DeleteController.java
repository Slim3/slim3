package slim3.it.controller.upload;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.it.dao.UploadDao;
import slim3.it.model.Upload;

public class DeleteController extends Controller {

    private UploadDao dao = new UploadDao();

    @Override
    public Navigation run() {
        Upload upload = dao.getObjectById(key(), version());
        dao.deletePersistentInTx(upload);
        return redirect(basePath);
    }
}
