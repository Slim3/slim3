package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;
import org.slim3.util.LongUtil;

import slim3.it.model.Blog;

import com.google.appengine.api.datastore.Key;

public class EditController extends JDOController {

    @Override
    public Navigation run() {
        Key key = key(Blog.class, LongUtil.toLong(requestScope("id")));
        Blog blog = pm.getObjectById(Blog.class, key);
        BeanUtil.copy(blog, request);
        return forward("/blog/edit.jsp");
    }
}