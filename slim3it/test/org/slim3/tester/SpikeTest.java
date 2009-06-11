package org.slim3.tester;

import java.util.HashMap;

import javax.jdo.JDOHelper;

public class SpikeTest extends JDOTestCase {

    public void test() throws Exception {
        assertNotNull(JDOHelper.getPersistenceManagerFactory(new HashMap()));
    }
}