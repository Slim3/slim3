package org.slim3.tester;

import java.util.ArrayList;
import java.util.List;

import slim3.it.model.Bar;
import slim3.it.model.Foo;

public class SpikeTest extends JDOTestCase {

    public void test() throws Exception {
        Foo foo = new Foo();
        Bar bar = new Bar();
        List<Bar> barList = new ArrayList<Bar>();
        barList.add(bar);
        foo.setBarList(barList);
        makePersistentInTx(foo);
        // foo = pm.getObjectById(Foo.class, foo.getKey());
        System.out.println(bar.getFoo());
        System.out.println(foo.getKey());
        System.out.println(bar.getKey());
    }
}