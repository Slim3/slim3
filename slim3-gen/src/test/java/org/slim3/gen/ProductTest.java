package org.slim3.gen;

import junit.framework.TestCase;

public class ProductTest extends TestCase {

    public void test() throws Exception {
        assertEquals("Slim3-Gen", ProductInfo.getName());
        assertEquals("@VERSION@", ProductInfo.getVersion());
    }
}
