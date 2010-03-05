package slim3.demo.controller.blog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import slim3.demo.meta.BlogMeta;
import slim3.demo.model.Blog;
import slim3.demo.service.BlogService;

public class InsertController extends Controller {

    private BlogService service = new BlogService();

    private BlogMeta meta = BlogMeta.get();

    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            return forward("create");
        }
        Blog blog = new Blog();
        BeanUtil.copy(request, blog);
        service.insert(blog);
        return redirect(basePath);
    }

    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.title, v.required());
        v.add(meta.content, v.required());
        return v.validate();
    }
}