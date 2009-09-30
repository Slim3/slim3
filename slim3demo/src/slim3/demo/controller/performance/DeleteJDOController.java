package slim3.demo.controller.performance;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.jdo.PMF;
import slim3.demo.cool.model.FooJDO;

public class DeleteJDOController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(DeleteJDOController.class.getName());

    @SuppressWarnings("unchecked")
    @Override
    public Navigation run() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(FooJDO.class);
        List<FooJDO> list = (List<FooJDO>) query.execute();
        long start = System.currentTimeMillis();
        for (FooJDO foo : list) {
            pm.deletePersistent(foo);
        }
        pm.close();
        sessionScope("deleteJDO", System.currentTimeMillis() - start);
        return forward(basePath);
    }
}
