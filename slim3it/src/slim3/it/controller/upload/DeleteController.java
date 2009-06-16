package slim3.it.controller.upload;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;

import slim3.it.model.Upload;

public class DeleteController extends JDOController {

    @Override
    public Navigation run() {
        Upload upload = pm.getObjectById(Upload.class, requestScope("key"));
        pm.deletePersistent(upload);
        return redirect(basePath);
    }
}
