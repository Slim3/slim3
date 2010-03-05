package slim3.demo.controller.blog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import slim3.demo.meta.BlogMeta;
import slim3.demo.model.Blog;
import slim3.demo.service.BlogService;

public class EditController extends Controller {

    private BlogService service = new BlogService();

    private BlogMeta meta = BlogMeta.get();

    @Override
    public Navigation run() throws Exception {
        Blog blog = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(blog, request);
        return forward("/blog/edit.jsp");
    }
}