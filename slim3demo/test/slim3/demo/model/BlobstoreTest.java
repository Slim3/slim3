package slim3.demo.model;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class BlobstoreTest extends AppEngineTestCase {

    private Blobstore model = new Blobstore();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
