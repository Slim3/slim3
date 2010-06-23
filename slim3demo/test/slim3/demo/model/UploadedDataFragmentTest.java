package slim3.demo.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.ShortBlob;

public class UploadedDataFragmentTest extends AppEngineTestCase {

    private UploadedDataFragment model = new UploadedDataFragment();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }

    @Test
    public void spike() throws Exception {
        model.setBytes2(new ShortBlob(new byte[] { '1' }));
        Datastore.put(model);
        // assertThat(model, is(notNullValue()));
    }
}
