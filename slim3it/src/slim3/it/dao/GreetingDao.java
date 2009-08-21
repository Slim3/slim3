package slim3.it.dao;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.slim3.jdo.GenericDao;
import org.slim3.jdo.SelectQuery;

import slim3.it.meta.GreetingMeta;
import slim3.it.model.Greeting;

public class GreetingDao extends GenericDao<Greeting> {

    private static final GreetingMeta m = new GreetingMeta();

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(GreetingDao.class.getName());

    public GreetingDao() {
        super(Greeting.class);
    }

    public GreetingDao(PersistenceManager pm) {
        super(Greeting.class, pm);
    }

    @Override
    protected SelectQuery<Greeting> from() {
        return new SelectQuery<Greeting>(pm, m.getModelClass());
    }

}
