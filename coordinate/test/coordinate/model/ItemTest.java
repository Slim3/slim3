package coordinate.model;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ItemTest extends AppEngineTestCase {

    private Item model = new Item();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
