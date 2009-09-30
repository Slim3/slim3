package slim3.demo.controller.performance;

import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import slim3.demo.model.Foo;

public class PutSlim3Controller extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(PutSlim3Controller.class.getName());

    private static final int COUNT = 100;

    @Override
    public Navigation run() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            Foo foo = new Foo();
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
            Datastore.put(foo);
        }
        sessionScope("putSlim3", System.currentTimeMillis() - start);
        return forward(basePath);
    }
}
