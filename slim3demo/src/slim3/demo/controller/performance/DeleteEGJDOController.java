package slim3.demo.controller.performance;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.jdo.PMF;
import slim3.demo.cool.model.ParentJDO;

public class DeleteEGJDOController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(DeleteEGJDOController.class.getName());

    @SuppressWarnings("unchecked")
    @Override
    public Navigation run() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        long start = System.currentTimeMillis();
        Query query = pm.newQuery(ParentJDO.class);
        List<ParentJDO> list = (List<ParentJDO>) query.execute();
        for (ParentJDO parent : list) {
            pm.currentTransaction().begin();
            pm.deletePersistent(parent);
            pm.currentTransaction().commit();
        }
        query.closeAll();
        pm.close();
        sessionScope("deleteEGJDO", System.currentTimeMillis() - start);
        return forward(basePath);
    }
}
