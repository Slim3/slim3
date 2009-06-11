package slim3.it.controller.blog;

import java.util.ArrayList;
import java.util.List;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanMap;
import org.slim3.util.BeanUtil;

import slim3.it.model.Blog;
import slim3.it.model.BlogMeta;

public class IndexController extends JDOController {

    @Override
    public Navigation run() {
        BlogMeta b = new BlogMeta();
        List<Blog> list = from(b).range(0, 10).getResultList();
        List<BeanMap> blogList = new ArrayList<BeanMap>(list.size());
        for (Blog blog : list) {
            BeanMap m = new BeanMap();
            BeanUtil.copy(blog, m);
            blogList.add(m);
        }
        requestScope("blogList", blogList);
        return forward("/blog/index.jsp");
    }
}