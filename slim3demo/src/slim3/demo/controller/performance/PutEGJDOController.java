package slim3.demo.controller.performance;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.jdo.PMF;
import slim3.demo.cool.model.ChildJDO;
import slim3.demo.cool.model.ParentJDO;

public class PutEGJDOController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(PutEGJDOController.class.getName());

    @Override
    public Navigation run() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            ParentJDO parent = new ParentJDO();
            for (int j = 0; j < 10; j++) {
                parent.getChildren().add(new ChildJDO());
            }
            pm.currentTransaction().begin();
            pm.makePersistent(parent);
            pm.currentTransaction().commit();
        }
        pm.close();
        sessionScope("putEGJDO", System.currentTimeMillis() - start);
        return forward(basePath);
    }
}
