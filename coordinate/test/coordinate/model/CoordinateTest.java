package coordinate.model;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CoordinateTest extends AppEngineTestCase {

    private Coordinate model = new Coordinate();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
