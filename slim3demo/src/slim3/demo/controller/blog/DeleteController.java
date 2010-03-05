package slim3.demo.controller.blog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.meta.BlogMeta;
import slim3.demo.service.BlogService;

public class DeleteController extends Controller {

    private BlogService service = new BlogService();

    private BlogMeta meta = BlogMeta.get();

    @Override
    public Navigation run() throws Exception {
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath);
    }
}