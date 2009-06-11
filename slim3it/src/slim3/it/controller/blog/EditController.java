package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

public class EditController extends JDOController {

    @Override
    public Navigation run() {
        pm.evictAll();
        Object blog = pm.getObjectById(Blog2.class, requestScope("key"));
        System.out.println(blog.getClass().getClassLoader());
        System.out.println(Blog2.class.getClassLoader());
        BeanUtil.copy(blog, request);
        return forward("/blog/edit.jsp");
    }
}