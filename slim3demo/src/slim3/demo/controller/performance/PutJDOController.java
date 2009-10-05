package slim3.demo.controller.performance;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.jdo.PMF;
import slim3.demo.cool.model.FooJDO;

public class PutJDOController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(PutJDOController.class.getName());

    @Override
    public Navigation run() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 300; i++) {
            FooJDO foo = new FooJDO();
            foo.setString01("string" + i);
            foo.setString02("string" + i);
            foo.setString03("string" + i);
            foo.setString04("string" + i);
            foo.setString05("string" + i);
            foo.setString06("string" + i);
            foo.setString07("string" + i);
            foo.setString08("string" + i);
            foo.setString09("string" + i);
            foo.setString10("string" + i);
            pm.makePersistent(foo);
        }
        pm.close();
        sessionScope("putJDO", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
