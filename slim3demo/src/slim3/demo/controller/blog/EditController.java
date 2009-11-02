package slim3.demo.controller.blog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import slim3.demo.model.Blog;
import slim3.demo.service.BlogService;

public class EditController extends Controller {

    private BlogService service = new BlogService();

    @Override
    public Navigation run() {
        Blog blog = service.get(asKey("key"), asLong("version"));
        BeanUtil.copy(blog, request);
        return forward("/blog/edit.jsp");
    }
}