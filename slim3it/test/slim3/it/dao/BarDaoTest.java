package slim3.it.dao;

import org.slim3.tester.JDOTestCase;

public class BarDaoTest extends JDOTestCase {

    public void test() throws Exception {
        BarDao barDao = new BarDao();
        assertNotNull(barDao);
    }
}
