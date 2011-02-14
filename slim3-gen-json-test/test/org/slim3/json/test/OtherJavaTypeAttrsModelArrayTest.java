package org.slim3.json.test;

import java.text.SimpleDateFormat;

import net.arnx.jsonic.JSON;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.json.test.OtherJavaTypeAttrsModel.WeekDay;

public class OtherJavaTypeAttrsModelArrayTest {
    @Before
    public void setUp() {
//        Datastore.setGlobalCipherKey("0654813216578941");
    }

    @Test
    public void modelsToJson() throws Exception {
        OtherJavaTypeAttrsModel[] m = {
            new OtherJavaTypeAttrsModel(){{
                setStringAttr("hello");
                setEnumAttr(WeekDay.Mon);
            }},
            new OtherJavaTypeAttrsModel(){{
                setStringAttr("world");
                setEnumAttr(WeekDay.Tue);
            }}
        };
        String json = meta.modelsToJson(m);
        Assert.assertEquals(
            "[{\"enumAttr\":\"Mon\",\"stringAttr\":\"hello\"},"
            + "{\"enumAttr\":\"Tue\",\"stringAttr\":\"world\"}]",
                json);
    }

    @Test
    public void jsonToModels() {
        OtherJavaTypeAttrsModel[] ms = meta.jsonToModels(
            "[{\"enumAttr\":\"Mon\",\"stringAttr\":\"hello\"},"
            + "{\"enumAttr\":\"Tue\",\"stringAttr\":\"world\"}]");
        OtherJavaTypeAttrsModel m = ms[0];
        Assert.assertEquals("hello", m.getStringAttr());
        Assert.assertEquals(WeekDay.Mon, m.getEnumAttr());
        Assert.assertNull(m.getDateAttr());
        m = ms[1];
        Assert.assertEquals("world", m.getStringAttr());
        Assert.assertEquals(WeekDay.Tue, m.getEnumAttr());
        Assert.assertNull(m.getDateAttr());
    }

    private OtherJavaTypeAttrsModelMeta meta = OtherJavaTypeAttrsModelMeta.get();
}
