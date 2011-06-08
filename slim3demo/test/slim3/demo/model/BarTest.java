package slim3.demo.model;

import org.junit.Test;

import slim3.demo.cool.model.Bar;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class BarTest {

    private Bar model = new Bar();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
