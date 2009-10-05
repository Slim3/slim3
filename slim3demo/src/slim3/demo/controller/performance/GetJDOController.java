package slim3.demo.controller.performance;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.jdo.PMF;
import slim3.demo.cool.model.FooJDO;

public class GetJDOController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(GetJDOController.class.getName());

    @SuppressWarnings("unchecked")
    @Override
    public Navigation run() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        long start = System.currentTimeMillis();
        Query query = pm.newQuery(FooJDO.class);
        List<FooJDO> list = (List<FooJDO>) query.execute();
        for (FooJDO foo : list) {
            foo.getKey();
        }
        query.closeAll();
        pm.close();
        sessionScope("getJDO", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
