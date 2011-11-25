package org.slim3.json.test.issue103;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;

public class InverseModelRefAttrModelTest extends AppEngineTestCase{
    @Test
    public void modelToJson() throws Exception {
        ChildModel child1 = new ChildModel();
        ChildModel child2 = new ChildModel();
        ParentModel parent = new ParentModel();
        child1.setName("child1");
        child1.getParent().setModel(parent);
        child2.setName("child2");
        child2.getParent().setModel(parent);
        parent.setName("parent");
        Datastore.put(child1, child2, parent);

        ParentModel parent2 = new ParentModel();
        parent2.setKey(parent.getKey());
        String json = ParentModelMeta.get().modelToJson(parent2, 1);
        assertEquals(
            "{\"children\":[{\"name\":\"child1\"},{\"name\":\"child2\"}]}",
            json);
    }
}
