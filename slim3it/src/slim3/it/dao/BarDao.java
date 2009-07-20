package slim3.it.dao;

import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import org.slim3.jdo.SelectQuery;
import slim3.it.model.Bar;
import slim3.it.model.BarMeta;
import org.slim3.jdo.GenericDao;

public class BarDao extends GenericDao<Bar> {

    private static final BarMeta m = new BarMeta();

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(BarDao.class.getName());

    public BarDao() {
        super(Bar.class);
    }


    public BarDao(PersistenceManager pm) {
        super(Bar.class, pm);
    }

    @Override
    protected SelectQuery<Bar> from() {
        return new SelectQuery<Bar>(pm, m.getModelClass());
    }

}
