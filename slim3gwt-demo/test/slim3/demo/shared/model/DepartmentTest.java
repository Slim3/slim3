package slim3.demo.shared.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class DepartmentTest extends AppEngineTestCase {

    private Department model = new Department();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
