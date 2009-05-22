package org.slim3.tester;

import java.util.ArrayList;
import java.util.List;

import slim3.it.model.Sample;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;

public class SpikeTest extends JDOTestCase {

    public void test() throws Exception {
        Sample s = new Sample();
        Sample s2 = new Sample();
        makePersistentInTx(s);
        makePersistentInTx(s2);
        List<Key> keys = new ArrayList<Key>();
        keys.add(s.getKey());
        keys.add(s2.getKey());
        // System.out.println(q.execute(keys));
        System.out.println(DatastoreServiceFactory.getDatastoreService().get(
            keys));
        // ParentMeta meta = new ParentMeta();
        // System.out.println(from(meta).where(meta.key.eq(keys)).getResultList());
    }
}