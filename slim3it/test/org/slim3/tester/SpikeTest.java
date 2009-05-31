package org.slim3.tester;

import slim3.it.model.Bar;
import slim3.it.model.BarMeta;
import slim3.it.model.Foo;

public class SpikeTest extends JDOTestCase {

    public void test() throws Exception {
        Foo foo = new Foo();
        Bar bar = new Bar();
        bar.setName("aaa");
        foo.setBar(bar);
        makePersistentInTx(foo);
        BarMeta b = new BarMeta();
        System.out.println(from(b).where(b.name.eq("aaa")).getResultList());
        System.out.println(bar.getKey());
    }
}