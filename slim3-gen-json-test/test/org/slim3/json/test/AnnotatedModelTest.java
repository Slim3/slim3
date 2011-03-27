package org.slim3.json.test;

import org.junit.Assert;
import org.junit.Test;

public class AnnotatedModelTest {
    @Test
    public void modelToJson() throws Exception {
        AnnotatedModel m = new AnnotatedModel();
        m.setStringAttr("hello");
        m.setHelloStringAttr("world!");
        m.setIgnoredStringAttr("ignored");
        m.setAliasedStringAttr("slim3");
        m.setAliasedStringAttr2("json");
        Assert.assertEquals(
            "{\"alias\":\"slim3\","
            + "\"alias.name\":\"json\","
            + "\"helloStringAttr\":\"hello\","
            + "\"stringAttr\":\"hello\"}"
            , meta.modelToJson(m));
    }

    @Test
    public void jsonToModel() throws Exception {
        AnnotatedModel m =
            meta.jsonToModel("{\"alias\":\"slim3\","
                + "\"alias.name\":\"json\","
                + "\"aliasedStringAttr\":\"slim4\","
                + "\"helloStringAttr\":\"world\","
                + "\"ignoredStringAttr\":\"ignored\","
                + "\"stringAttr\":\"hello\"}");
        Assert.assertEquals("hello", m.getStringAttr());
        Assert.assertEquals("hello", m.getHelloStringAttr());
        Assert.assertNull(m.getIgnoredStringAttr());
        Assert.assertEquals("slim3", m.getAliasedStringAttr());
        Assert.assertEquals("json", m.getAliasedStringAttr2());
    }

    private AnnotatedModelMeta meta = AnnotatedModelMeta.get();
}
