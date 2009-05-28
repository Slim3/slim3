package org.slim3.tester;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;

import slim3.it.model.Bar;
import slim3.it.model.Foo;

public class SpikeTest extends JDOTestCase {

    public void test() throws Exception {
        Foo foo = new Foo();
        Bar bar = new Bar();
        makePersistentInTx(bar);
        List<Bar> barList = new ArrayList<Bar>();
        barList.add(bar);
        foo.setBarList(barList);
        makePersistentInTx(foo);
        System.out.println(JDOHelper.getVersion(foo));
        System.out.println(foo.getKey());
        System.out.println(bar.getKey());
        System.out.println(foo.getBarList());
    }
}