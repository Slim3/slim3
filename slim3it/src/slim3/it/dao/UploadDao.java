package slim3.it.dao;

import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import slim3.it.model.Upload;
import slim3.it.model.UploadMeta;
import org.slim3.jdo.GenericDao;

public class UploadDao extends GenericDao<Upload> {

    @SuppressWarnings("unused")
    private static final UploadMeta m = new UploadMeta();

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(UploadDao.class.getName());

    public UploadDao() {
        super(Upload.class);
    }


    public UploadDao(PersistenceManager pm) {
        super(Upload.class, pm);
    }

}
