package slim3.it.model;

import org.slim3.tester.JDOTestCase;

public class BarTest extends JDOTestCase {

    public void test() throws Exception {
        Bar bar = new Bar();
        assertNotNull(bar);
    }
}
