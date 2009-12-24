package slim3.demo.model;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class EmployeeTest {

    private Employee model = new Employee();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
