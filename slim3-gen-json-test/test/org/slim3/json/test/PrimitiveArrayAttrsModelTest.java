package org.slim3.json.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slim3.datastore.Datastore;

public class PrimitiveArrayAttrsModelTest {
    @Before
    public void setUp() {
        Datastore.setGlobalCipherKey("0654813216578941");
    }

    @Test
    public void modelToJson() throws Exception {
        PrimitiveArrayAttrsModel m = new PrimitiveArrayAttrsModel();
        m.setBooleans(new boolean[] { true, false, true });
        m.setBytes("hello".getBytes());
        m.setChars(new char[] { 'H', 'e', 'l', 'l', 'o' });
        m.setShorts(new short[] { 1, 2, 3, 4 });
        m.setInts(new int[] { 1, 2, 3, 4 });
        m.setLongs(new long[] { 1, 2, 3, 4 });
        m.setFloats(new float[] { 1.0f, 2.0f, 3.0f, 4.0f });
        m.setDoubles(new double[] { 1.0, 2.0, 3.0, 4.0 });
        Assert.assertEquals("{\"bytes\":\"aGVsbG8=\"}", meta.modelToJson(m));
    }

    @Test
    public void modelToJson_null() throws Exception {
        Assert.assertEquals(
            "{}",
            meta.modelToJson(new PrimitiveArrayAttrsModel()));
    }

    @Test
    public void jsonToModel() throws Exception {
        PrimitiveArrayAttrsModel m =
            meta.jsonToModel("{\"bytes\":\"aGVsbG8=\"}");
        Assert.assertArrayEquals("hello".getBytes(), m.getBytes());
    }

    private PrimitiveArrayAttrsModelMeta meta = PrimitiveArrayAttrsModelMeta
        .get();
}
