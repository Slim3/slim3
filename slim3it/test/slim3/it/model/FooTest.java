package slim3.it.model;

import org.slim3.tester.JDOTestCase;

public class FooTest extends JDOTestCase {

    public void test() throws Exception {
        Foo foo = new Foo();
        assertNotNull(foo);
    }
}
