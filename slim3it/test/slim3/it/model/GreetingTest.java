package slim3.it.model;

import org.slim3.tester.JDOTestCase;

public class GreetingTest extends JDOTestCase {

    public void test() throws Exception {
        Greeting greeting = new Greeting();
        assertNotNull(greeting);
    }
}
