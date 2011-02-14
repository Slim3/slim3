package org.slim3.json.test;

import org.junit.Assert;
import org.junit.Test;

public class PrimitiveAttrsModelArrayTest {
    @Test
    public void modelsToJson() throws Exception {
        PrimitiveAttrsModel[] m = {
            new PrimitiveAttrsModel(){{
                setBooleanAttr(true);
                setShortAttr((short) 100);
            }}
            , new PrimitiveAttrsModel(){{
                setIntAttr(1000);
                setLongAttr(10000);
            }}
            , new PrimitiveAttrsModel(){{
                setFloatAttr(1.1f);
                setDoubleAttr(2.2);
            }}
        };
        String json = meta.modelsToJson(m);
       Assert.assertEquals(
            "[{\"booleanAttr\":true,\"doubleAttr\":0.0"
            + ",\"floatAttr\":0.0,\"intAttr\":0"
            + ",\"longAttr\":0,\"shortAttr\":100},"
            +"{\"booleanAttr\":false,\"doubleAttr\":0.0"
            + ",\"floatAttr\":0.0,\"intAttr\":1000"
            + ",\"longAttr\":10000,\"shortAttr\":0},"
            +"{\"booleanAttr\":false,\"doubleAttr\":2.2"
            + ",\"floatAttr\":1.1,\"intAttr\":0"
            + ",\"longAttr\":0,\"shortAttr\":0}]"
            , json);
    }

    @Test
    public void jsonToModels() throws Exception {
        PrimitiveAttrsModel[] ms = meta.jsonToModels(
                "[{\"booleanAttr\":true,\"doubleAttr\":0.0"
                + ",\"floatAttr\":0.0,\"intAttr\":0"
                + ",\"longAttr\":0,\"shortAttr\":100},"
                +"{\"booleanAttr\":false,\"doubleAttr\":0.0"
                + ",\"floatAttr\":0.0,\"intAttr\":1000"
                + ",\"longAttr\":10000,\"shortAttr\":0},"
                +"{\"booleanAttr\":false,\"doubleAttr\":2.2"
                + ",\"floatAttr\":1.1,\"intAttr\":0"
                + ",\"longAttr\":0,\"shortAttr\":0}]"
                );
        Assert.assertEquals(3, ms.length);
        PrimitiveAttrsModel m = ms[0];
        Assert.assertEquals(true, m.isBooleanAttr());
        Assert.assertEquals(100, m.getShortAttr());
        Assert.assertEquals(0, m.getIntAttr());
        Assert.assertEquals(0, m.getLongAttr());
        Assert.assertEquals(0, m.getDoubleAttr(), 0.1);
        Assert.assertEquals(0, m.getFloatAttr(), 0.1);
        m = ms[1];
        Assert.assertEquals(false, m.isBooleanAttr());
        Assert.assertEquals(0, m.getShortAttr());
        Assert.assertEquals(1000, m.getIntAttr());
        Assert.assertEquals(10000, m.getLongAttr());
        Assert.assertEquals(0, m.getDoubleAttr(), 0.1);
        Assert.assertEquals(0, m.getFloatAttr(), 0.1);
        m = ms[2];
        Assert.assertEquals(false, m.isBooleanAttr());
        Assert.assertEquals(0, m.getShortAttr());
        Assert.assertEquals(0, m.getIntAttr());
        Assert.assertEquals(0, m.getLongAttr());
        Assert.assertEquals(2.2, m.getDoubleAttr(), 0.1);
        Assert.assertEquals(1.1, m.getFloatAttr(), 0.1);
    }

    private PrimitiveAttrsModelMeta meta = PrimitiveAttrsModelMeta.get();
}
