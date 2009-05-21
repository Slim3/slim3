package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;
import org.slim3.util.LongUtil;

import slim3.it.model.Blog;

import com.google.appengine.api.datastore.Key;

public class UpdateController extends JDOController {

    @Override
    public Navigation run() {
        Key key = key(Blog.class, LongUtil.toLong(requestScope("id")));
        Blog blog = pm.getObjectById(Blog.class, key);
        tx.begin();
        BeanUtil.copy(request, blog);
        pm.makePersistent(blog);
        tx.commit();
        return redirect(basePath);
    }
}