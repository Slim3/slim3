package org.slim3.json.test;

import static org.junit.Assert.assertEquals;
import net.arnx.jsonic.JSON;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.json.test.issue103.InvRefChildModel;
import org.slim3.json.test.issue103.InvRefChildModelMeta;
import org.slim3.json.test.issue103.InvRefParentModel;
import org.slim3.json.test.issue103.InvRefParentModelMeta;
import org.slim3.tester.AppEngineTestCase;

public class InverseModelRefAttrModelTest extends AppEngineTestCase{
    @Test
    public void modelToJson() throws Exception {
        InvRefChildModel child1 = new InvRefChildModel();
        InvRefChildModel child2 = new InvRefChildModel();
        InvRefParentModel parent = new InvRefParentModel();
        child1.setName("child1");
        child1.getParentRef().setModel(parent);
        child2.setName("child2");
        child2.getParentRef().setModel(parent);
        parent.setName("parent");
        parent.getRef().setModel(child1);
        Datastore.put(child1, child2, parent);

        String json = meta.modelToJson(parent, 1);
        System.out.println(json);
        System.out.println(InvRefChildModelMeta.get().modelToJson(child1, 1));
        JSON.decode(json);

        assertEquals(
            "{\"invListRef\":[{\"name\":\"child1\"},{\"name\":\"child2\"}]" +
            ",\"name\":\"parent\",\"ref\":\"" + Datastore.keyToString(parent.getRef().getKey()) + "\"}",
            json);
    }

    private InvRefParentModelMeta meta = InvRefParentModelMeta.get();
}
