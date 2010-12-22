package org.slim3.json.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slim3.datastore.Datastore;

public class LobAttrsModelTest {
    @Before
    public void setUp() {
        Datastore.setGlobalCipherKey("0654813216578941");
    }

    @Test
    public void modelToJson() throws Exception {
        LobAttrModel m = new LobAttrModel();
        m.setBytes("hello".getBytes());
        Assert.assertEquals("{\"bytes\":\"aGVsbG8=\"}", meta.modelToJson(m));
    }

    @Test
    public void modelToJson_null() throws Exception {
        Assert.assertEquals("{}", meta.modelToJson(new LobAttrModel()));
    }

    private LobAttrModelMeta meta = LobAttrModelMeta.get();
}
