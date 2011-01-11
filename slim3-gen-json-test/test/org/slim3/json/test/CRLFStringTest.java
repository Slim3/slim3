package org.slim3.json.test;

import org.junit.Assert;
import org.junit.Test;
import org.slim3.json.test.CRLFStringTestModel;

import com.google.appengine.api.datastore.Text;

public class CRLFStringTest {
    @Test
    public void modelToJson() throws Exception{
        CRLFStringTestModel m = new CRLFStringTestModel();
        m.setStringValue("hello\b\f\n\r\t\"\u1234world");
        m.setTextValue(new Text("hello\b\f\n\r\t\"\u1234world"));
        Assert.assertEquals(
            "{\"stringValue\":\"hello\\b\\f\\n\\r\\t\\\"\u1234world\"," +
            "\"textValue\":\"hello\\b\\f\\n\\r\\t\\\"\u1234world\"}"
            , CRLFStringTestModelMeta.get().modelToJson(m));
    }

    @Test
    public void jsonToModel() throws Exception{
        CRLFStringTestModel m = meta.jsonToModel(
            "{\"stringValue\":\"hello\\b\\f\\n\\r\\t\\\"\u1234world\"," +
            "\"textValue\":\"hello\\b\\f\\n\\r\\t\\\"\u1234world\"}"
            );
        Assert.assertEquals("hello\b\f\n\r\t\"\u1234world", m.getStringValue());
        Assert.assertEquals("hello\b\f\n\r\t\"\u1234world", m.getTextValue().getValue());
    }
    
    private CRLFStringTestModelMeta meta = CRLFStringTestModelMeta.get();
}
