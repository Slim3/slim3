package slim3.demo.shared.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class EmployeeTest extends AppEngineTestCase {

    private Employee model = new Employee();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
