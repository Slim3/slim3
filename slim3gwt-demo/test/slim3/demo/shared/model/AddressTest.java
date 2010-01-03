package slim3.demo.shared.model;

import org.slim3.tester.LocalServiceTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class AddressTest extends LocalServiceTestCase {

    private Address model = new Address();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
