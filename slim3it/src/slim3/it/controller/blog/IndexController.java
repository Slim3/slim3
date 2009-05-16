package slim3.it.controller.blog;

import java.util.ArrayList;
import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.jdo.JDOTemplate;
import org.slim3.util.BeanMap;
import org.slim3.util.BeanUtil;

import slim3.it.model.Blog;
import slim3.it.model.BlogMeta;

public class IndexController extends Controller {

    private List<BeanMap> blogList;

    /**
     * @return the blogList
     */
    public List<BeanMap> getBlogList() {
        return blogList;
    }

    @Override
    public Navigation execute() {
        new JDOTemplate() {
            @Override
            public void doRun() {
                BlogMeta meta = new BlogMeta();
                List<Blog> list =
                    from(meta).orderBy(meta.title.asc()).getResultList();
                blogList = new ArrayList<BeanMap>(list.size());
                for (Blog b : list) {
                    BeanMap m = new BeanMap();
                    BeanUtil.copy(b, m);
                    blogList.add(m);
                }
            }
        }.run();
        return forward("/blog/index.jsp");
    }
}
