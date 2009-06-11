package slim3.it.controller.blog;

import java.util.ArrayList;
import java.util.List;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanMap;
import org.slim3.util.BeanUtil;

public class IndexController extends JDOController {

    @Override
    public Navigation run() {
        Blog2Meta b = new Blog2Meta();
        List<Blog2> list = from(b).range(0, 10).getResultList();
        List<BeanMap> blogList = new ArrayList<BeanMap>(list.size());
        for (Blog2 blog : list) {
            BeanMap m = new BeanMap();
            BeanUtil.copy(blog, m);
            blogList.add(m);
        }
        requestScope("blogList", blogList);
        return forward("/blog/index.jsp");
    }
}