package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;

public class DeleteController extends JDOController {

    @Override
    public Navigation run() {
        Blog2 blog = pm.getObjectById(Blog2.class, requestScope("key"));
        pm.deletePersistent(blog);
        return redirect(basePath);
    }
}