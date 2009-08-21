package slim3.it.dao;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.slim3.jdo.GenericDao;
import org.slim3.jdo.SelectQuery;

import slim3.it.meta.UploadMeta;
import slim3.it.model.Upload;

public class UploadDao extends GenericDao<Upload> {

    private static final UploadMeta m = new UploadMeta();

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(UploadDao.class.getName());

    public UploadDao() {
        super(Upload.class);
    }

    public UploadDao(PersistenceManager pm) {
        super(Upload.class, pm);
    }

    @Override
    protected SelectQuery<Upload> from() {
        return new SelectQuery<Upload>(pm, m.getModelClass());
    }
}
