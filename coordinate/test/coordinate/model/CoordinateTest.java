package coordinate.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

/**
 * @author higayasuo
 * 
 */
public class CoordinateTest extends AppEngineTestCase {

    private Coordinate model = new Coordinate();

    /**
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
