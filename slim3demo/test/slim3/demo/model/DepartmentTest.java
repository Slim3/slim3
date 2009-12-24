package slim3.demo.model;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class DepartmentTest {

    private Department model = new Department();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
