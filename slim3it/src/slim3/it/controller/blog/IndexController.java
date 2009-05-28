package slim3.it.controller.blog;

import java.util.ArrayList;
import java.util.List;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanMap;
import org.slim3.util.BeanUtil;

import slim3.it.model.Blog;

public class IndexController extends JDOController {

    @Override
    public Navigation run() {
        List<Blog> list = from(Blog.class).range(0, 10).getResultList();
        List<BeanMap> blogList = new ArrayList<BeanMap>(list.size());
        for (Blog b : list) {
            BeanMap m = new BeanMap();
            BeanUtil.copy(b, m);
            blogList.add(m);
        }
        requestScope("blogList", blogList);
        return forward("/blog/index.jsp");
    }
}