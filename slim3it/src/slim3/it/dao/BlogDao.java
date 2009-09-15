package slim3.it.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.slim3.jdo.GenericDao;
import org.slim3.jdo.SelectQuery;

import slim3.it.meta.BlogMeta;
import slim3.it.model.Blog;

public class BlogDao extends GenericDao<Blog> {

    private static final BlogMeta m = new BlogMeta();

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(BlogDao.class.getName());

    public BlogDao() {
        super(Blog.class);
    }

    public BlogDao(PersistenceManager pm) {
        super(Blog.class, pm);
    }

    public List<Blog> findTopTen() {
        return from().range(0, 10).getResultList();
    }

    public List<Blog> findByContent(String content) {
        return from().where(
            m.content.ge(content),
            m.content.lt(content + "\ufffd")).getResultList();
    }

    @Override
    protected SelectQuery<Blog> from() {
        return new SelectQuery<Blog>(pm, m.getModelClass());
    }
}