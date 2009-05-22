package slim3.it.controller.flexblog;

import java.util.ArrayList;
import java.util.List;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanMap;
import org.slim3.util.BeanUtil;

import slim3.it.model.Blog;
import slim3.it.model.BlogMeta;

public class ListController extends JDOController {

    @Override
    public Navigation run() {
        BlogMeta meta = new BlogMeta();
        List<Blog> list = from(meta).getResultList();
        List<BeanMap> blogList = new ArrayList<BeanMap>(list.size());
        for (Blog b : list) {
            BeanMap m = new BeanMap();
            BeanUtil.copy(b, m);
            m.put("id", b.getKey().getId());
            blogList.add(m);
        }
        requestScope("blogList", blogList);
        return forward("/flexblog/list.jsp");
    }
}
