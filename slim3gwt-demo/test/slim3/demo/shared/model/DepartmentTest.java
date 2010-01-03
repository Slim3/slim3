package slim3.demo.shared.model;

import org.slim3.tester.LocalServiceTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class DepartmentTest extends LocalServiceTestCase {

    private Department model = new Department();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
