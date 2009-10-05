package slim3.demo.controller.performance;

import java.util.List;
import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import slim3.demo.meta.FooMeta;
import slim3.demo.model.Foo;

public class GetSlim3Controller extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(GetSlim3Controller.class.getName());

    @Override
    public Navigation run() {
        FooMeta f = new FooMeta();
        long start = System.currentTimeMillis();
        List<Foo> list = Datastore.query(f).asList();
        for (Foo foo : list) {
            foo.getKey();
        }
        sessionScope("getSlim3", System.currentTimeMillis() - start);
        return redirect(basePath);
    }
}
