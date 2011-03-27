package coordinate.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

/**
 * @author higayasuo
 * 
 */
public class ItemTest extends AppEngineTestCase {

    private Item model = new Item();

    /**
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
