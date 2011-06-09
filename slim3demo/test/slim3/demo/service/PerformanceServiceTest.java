package slim3.demo.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;

import slim3.demo.cool.model.BarJDO;
import slim3.demo.cool.service.PerformanceService;

import com.google.appengine.api.datastore.Entity;

public class PerformanceServiceTest extends AppEngineTestCase {

    private PerformanceService service = new PerformanceService();

    @Test
    public void getBarsUsingLL() throws Exception {
        Datastore.put(new Entity("Bar"));
        assertThat(service.getBarsUsingLL().hasNext(), is(true));
    }

    @Test
    public void getBarsUsingSlim3() throws Exception {
        Datastore.put(new Entity("Bar"));
        assertThat(service.getBarsUsingSlim3().hasNext(), is(true));
    }

    @Test
    public void getBarsUsingObjectify() throws Exception {
        Datastore.put(new Entity("Bar"));
        assertThat(service.getBarsUsingObjectify().hasNext(), is(true));
    }

    @Test
    public void getBarListUsingJDO() throws Exception {
        int count = Datastore.query("Bar").count();
        Datastore.put(new Entity("Bar"));
        List<BarJDO> list = service.getBarsUsingJDO();
        assertThat(list.size(), is(count + 1));
    }
}
