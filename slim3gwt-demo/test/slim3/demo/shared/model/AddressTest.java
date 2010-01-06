package slim3.demo.shared.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class AddressTest extends AppEngineTestCase {

    private Address model = new Address();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
