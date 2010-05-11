package slim3.demo.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;

import slim3.demo.cool.model.BarJDO;
import slim3.demo.model.Bar;

import com.google.appengine.api.datastore.Entity;

public class PerformanceServiceTest extends AppEngineTestCase {

    private PerformanceService service = new PerformanceService();

    @Test
    public void getBarListUsingLL() throws Exception {
        int count = Datastore.query("Bar").count();
        Datastore.put(new Entity("Bar"));
        List<Entity> list = service.getBarListUsingLL();
        assertThat(list.size(), is(count + 1));
    }

    @Test
    public void getBarListUsingSlim3() throws Exception {
        int count = Datastore.query("Bar").count();
        Datastore.put(new Entity("Bar"));
        List<Bar> list = service.getBarListUsingSlim3();
        assertThat(list.size(), is(count + 1));
    }

    @Test
    public void getBarListUsingJDO() throws Exception {
        int count = Datastore.query("Bar").count();
        Datastore.put(new Entity("Bar"));
        List<BarJDO> list = service.getBarListUsingJDO();
        assertThat(list.size(), is(count + 1));
    }
}
