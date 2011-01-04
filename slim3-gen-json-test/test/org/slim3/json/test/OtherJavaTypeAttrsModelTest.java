package org.slim3.json.test;

import java.text.SimpleDateFormat;

import net.arnx.jsonic.JSON;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.json.test.OtherJavaTypeAttrsModel.WeekDay;

public class OtherJavaTypeAttrsModelTest {
    @Before
    public void setUp() {
        Datastore.setGlobalCipherKey("0654813216578941");
    }

    @Test
    public void modelToJson() throws Exception {
        OtherJavaTypeAttrsModel m = new OtherJavaTypeAttrsModel();
        m.setStringAttr("hello");
        m.setEncryptedStringAttr("world");
        m.setDateAttr(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            .parse("2010-11-11 11:11:11"));
        m.setEnumAttr(WeekDay.Sun);

        String json = meta.modelToJson(m);
        System.out.println(json);
        JSON j = new JSON();
        j.setSuppressNull(true);
        System.out.println(j.format(m));

        Assert
            .assertEquals(
                "{\"dateAttr\":1289441471000,\"encryptedStringAttr\":\"mMB4qZAgtBKJq0d1LBGTCA==\""
                    + ",\"enumAttr\":\"Sun\",\"stringAttr\":\"hello\"}",
                json);
    }

    @Test
    public void modelsToJson() throws Exception {
        Object[] models = new Object[]{
            new OtherJavaTypeAttrsModel(){{
                setStringAttr("hello");
                setEncryptedStringAttr("world");
                setDateAttr(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                    .parse("2010-11-11 11:11:11"));
                setEnumAttr(WeekDay.Sun);
            }}
            , new OtherJavaTypeAttrsModel(){{
                setStringAttr("hi");
                setEncryptedStringAttr("there");
                setEnumAttr(WeekDay.Fri);
            }}
        };
        String json = meta.modelsToJson(models);
        System.out.println(json);
        Assert.assertEquals(
                "[{\"dateAttr\":1289441471000,\"encryptedStringAttr\":\"mMB4qZAgtBKJq0d1LBGTCA==\"," +
                "\"enumAttr\":\"Sun\",\"stringAttr\":\"hello\"}," +
                "{\"encryptedStringAttr\":\"73qsqhU1P90KYsgZSQnedg==\",\"enumAttr\":\"Fri\"," +
                "\"stringAttr\":\"hi\"}]",
                json);
    }

    @Test
    public void modelToJson_null() {
        OtherJavaTypeAttrsModel m = new OtherJavaTypeAttrsModel();
        String json = meta.modelToJson(m);
        Assert.assertEquals("{}", json);
    }

    @Test
    public void jsonToModel() {
        OtherJavaTypeAttrsModel m = meta.jsonToModel(
                    "{\"dateAttr\":1289441471000,\"encryptedStringAttr\":\"mMB4qZAgtBKJq0d1LBGTCA==\""
                        + ",\"enumAttr\":\"Sun\",\"stringAttr\":\"hello\"}");
        Assert.assertEquals("world", m.getEncryptedStringAttr());
        Assert.assertEquals("hello", m.getStringAttr());
        Assert.assertEquals(WeekDay.Sun, m.getEnumAttr());
        Assert.assertEquals(1289441471000L, m.getDateAttr().getTime());
    }

    @Test
    public void modelToJsonToModel() throws Exception {
        String json = null;
        {
            OtherJavaTypeAttrsModel m = new OtherJavaTypeAttrsModel();
            m.setStringAttr("hello");
            m.setEncryptedStringAttr("world");
            m.setDateAttr(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                .parse("2010-11-11 11:11:11"));
            m.setEnumAttr(WeekDay.Sun);
            json = meta.modelToJson(m);
        }

        OtherJavaTypeAttrsModel m = meta.jsonToModel(json);
        Assert.assertEquals("world", m.getEncryptedStringAttr());
        Assert.assertEquals("hello", m.getStringAttr());
        Assert.assertEquals(WeekDay.Sun, m.getEnumAttr());
        Assert.assertEquals(1289441471000L, m.getDateAttr().getTime());
    }

    private OtherJavaTypeAttrsModelMeta meta = OtherJavaTypeAttrsModelMeta.get();
}
