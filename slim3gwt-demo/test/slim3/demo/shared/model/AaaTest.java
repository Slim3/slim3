package slim3.demo.shared.model;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class AaaTest {

    private Aaa model = new Aaa();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
