package slim3.it.dao;

import org.slim3.tester.JDOTestCase;

public class FooDaoTest extends JDOTestCase {

    public void test() throws Exception {
        FooDao fooDao = new FooDao();
        assertNotNull(fooDao);
    }
}
