package slim3.demo.controller.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.jdo.PMF;
import slim3.demo.cool.model.ChildJDO2;
import slim3.demo.cool.model.ParentJDO2;

public class DeleteEGJDO2Controller extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(DeleteEGJDO2Controller.class.getName());

    @SuppressWarnings("unchecked")
    @Override
    public Navigation run() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        long start = System.currentTimeMillis();
        Query query = pm.newQuery(ParentJDO2.class);
        List<ParentJDO2> list = (List<ParentJDO2>) query.execute();
        for (ParentJDO2 parent : list) {
            List<Object> models = new ArrayList<Object>();
            models.add(parent);
            Query query2 =
                pm.newQuery(ChildJDO2.class, "parentKey == :parentKey");
            models.addAll((List<ChildJDO2>) query2.execute(parent.getKey()));
            query2.closeAll();
            pm.currentTransaction().begin();
            pm.deletePersistentAll(models);
            pm.currentTransaction().commit();
        }
        query.closeAll();
        pm.close();
        sessionScope("deleteEGJDO2", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
