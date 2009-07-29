package slim3.it.dao;

import org.slim3.tester.JDOTestCase;

public class GreetingDaoTest extends JDOTestCase {

    public void test() throws Exception {
        GreetingDao greetingDao = new GreetingDao();
        assertNotNull(greetingDao);
    }
}
