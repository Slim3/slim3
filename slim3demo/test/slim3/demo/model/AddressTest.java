package slim3.demo.model;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class AddressTest {

    private Address model = new Address();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
