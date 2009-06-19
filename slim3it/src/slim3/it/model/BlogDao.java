package slim3.it.model;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.slim3.jdo.GenericDao;

public class BlogDao extends GenericDao<Blog> {

    private static final BlogMeta m = new BlogMeta();

    private static final Logger logger =
        Logger.getLogger(BlogDao.class.getName());

    public BlogDao(PersistenceManager pm) {
        super(pm, Blog.class);
    }

    public List<Blog> findAll() {
        return from().getResultList();
    }
}