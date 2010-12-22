package org.slim3.json.test;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;
import net.arnx.jsonic.JSON;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.TestEnvironment;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.apphosting.api.ApiProxy;

public class ModelRefAttrModelTest {
    @BeforeClass
    public static void staticSetUp() {
        Datastore.setGlobalCipherKey("0654813216578941");
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment() {
            @Override
            public String getAppId() {
                return "slim3-gen";
            }
        });
    }

    @Test
    public void modelToJson() throws Exception {
        String json = meta.modelToJson(getModel());
        System.out.println(json);
        JSON.decode(json);

        assertEquals(
            "{\"expandedRef\":\"aglzbGltMy1nZW5yJQsSEU1vZGVsUmVmQXR0ck1vZGVsIg5leHBhbmRlZCBjaGlsZAw\","
                + "\"expandedRefList\":[],"
                + "\"key\":\"aglzbGltMy1nZW5yHQsSEU1vZGVsUmVmQXR0ck1vZGVsIgZwYXJlbnQM\","
                + "\"name\":\"parent\",\"ref\":\"aglzbGltMy1nZW5yKgsSEU1vZGVsUmVmQXR0ck1vZGVsIhNjaGlsZChub3QgZXhwYW5kZWQpDA\","
                + "\"refList\":[]}",
            json);
    }

    @Test
    public void modelToJson_1() throws Exception {
        String json = meta.modelToJson(getModel(), 1);
        System.out.println(json);
        JSON.decode(json);

        assertEquals(
            "{\"expandedRef\":{"
                + "\"expandedRef\":\"aglzbGltMy1nZW5yLAsSEU1vZGVsUmVmQXR0ck1vZGVsIhVleHBhbmRlZCBncm91bmQgY2hpbGQM\","
                + "\"expandedRefList\":[],"
                + "\"key\":\"aglzbGltMy1nZW5yJQsSEU1vZGVsUmVmQXR0ck1vZGVsIg5leHBhbmRlZCBjaGlsZAw\","
                + "\"name\":\"expanded child\","
                + "\"refList\":[]},"
                + "\"expandedRefList\":[],"
                + "\"key\":\"aglzbGltMy1nZW5yHQsSEU1vZGVsUmVmQXR0ck1vZGVsIgZwYXJlbnQM\","
                + "\"name\":\"parent\","
                + "\"ref\":\"aglzbGltMy1nZW5yKgsSEU1vZGVsUmVmQXR0ck1vZGVsIhNjaGlsZChub3QgZXhwYW5kZWQpDA\","
                + "\"refList\":[]}",
            json);
    }

    @Test
    public void modelToJson_infiniti() throws Exception {
        String json = meta.modelToJson(getModel(), -1);
        System.out.println(json);
        JSON.decode(json);

        assertEquals(
            "{\"expandedRef\":{"
                + "\"expandedRef\":{"
                + "\"expandedRef\":{"
                + "\"expandedRefList\":[],"
                + "\"key\":\"aglzbGltMy1nZW5yPgsSEU1vZGVsUmVmQXR0ck1vZGVsIidleHBhbmRlZCBjaGlsZCBvZiBleHBhbmRlZCBncm91bmQgY2hpbGQM\","
                + "\"name\":\"expanded child of expanded ground child\","
                + "\"refList\":[]"
                + "},"
                + "\"expandedRefList\":[],"
                + "\"key\":\"aglzbGltMy1nZW5yLAsSEU1vZGVsUmVmQXR0ck1vZGVsIhVleHBhbmRlZCBncm91bmQgY2hpbGQM\","
                + "\"name\":\"expanded ground child\","
                + "\"refList\":[]"
                + "},"
                + "\"expandedRefList\":[],"
                + "\"key\":\"aglzbGltMy1nZW5yJQsSEU1vZGVsUmVmQXR0ck1vZGVsIg5leHBhbmRlZCBjaGlsZAw\","
                + "\"name\":\"expanded child\","
                + "\"refList\":[]},"
                + "\"expandedRefList\":[],"
                + "\"key\":\"aglzbGltMy1nZW5yHQsSEU1vZGVsUmVmQXR0ck1vZGVsIgZwYXJlbnQM\","
                + "\"name\":\"parent\","
                + "\"ref\":\"aglzbGltMy1nZW5yKgsSEU1vZGVsUmVmQXR0ck1vZGVsIhNjaGlsZChub3QgZXhwYW5kZWQpDA\","
                + "\"refList\":[]}",
            json);
    }

    @Test
    public void modelToJson_null() {
        String json = meta.modelToJson(new ModelRefAttrModel());
        assertEquals("{\"expandedRefList\":[],\"refList\":[]}", json);
    }

    @Test
    public void jsonToModel() {
        ModelRefAttrModel model =
            meta
                .jsonToModel("{\"expandedRef\":\"aglzbGltMy1nZW5yJQsSEU1vZGVsUmVmQXR0ck1vZGVsIg5leHBhbmRlZCBjaGlsZAw\","
                    + "\"expandedRefList\":[],"
                    + "\"key\":\"aglzbGltMy1nZW5yHQsSEU1vZGVsUmVmQXR0ck1vZGVsIgZwYXJlbnQM\","
                    + "\"name\":\"parent\","
                    + "\"ref\":\"aglzbGltMy1nZW5yKgsSEU1vZGVsUmVmQXR0ck1vZGVsIhNjaGlsZChub3QgZXhwYW5kZWQpDA\","
                    + "\"refList\":[]}");
        Assert
            .assertEquals(
                "aglzbGltMy1nZW5yJQsSEU1vZGVsUmVmQXR0ck1vZGVsIg5leHBhbmRlZCBjaGlsZAw",
                KeyFactory.keyToString(model.getExpandedRef().getKey()));
        Assert.assertEquals(
            "aglzbGltMy1nZW5yHQsSEU1vZGVsUmVmQXR0ck1vZGVsIgZwYXJlbnQM",
            KeyFactory.keyToString(model.getKey()));
        Assert.assertEquals("parent", model.getName());
        Assert
            .assertEquals(
                "aglzbGltMy1nZW5yKgsSEU1vZGVsUmVmQXR0ck1vZGVsIhNjaGlsZChub3QgZXhwYW5kZWQpDA",
                KeyFactory.keyToString(model.getRef().getKey()));
    }

    @Test
    public void jsonToModel_1() {
        ModelRefAttrModel model =
            meta
                .jsonToModel(
                    "{\"expandedRef\":{"
                        + "\"expandedRef\":\"aglzbGltMy1nZW5yLAsSEU1vZGVsUmVmQXR0ck1vZGVsIhVleHBhbmRlZCBncm91bmQgY2hpbGQM\","
                        + "\"expandedRefList\":[],"
                        + "\"key\":\"aglzbGltMy1nZW5yJQsSEU1vZGVsUmVmQXR0ck1vZGVsIg5leHBhbmRlZCBjaGlsZAw\","
                        + "\"name\":\"expanded child\","
                        + "\"refList\":[]},"
                        + "\"expandedRefList\":[],"
                        + "\"key\":\"aglzbGltMy1nZW5yHQsSEU1vZGVsUmVmQXR0ck1vZGVsIgZwYXJlbnQM\","
                        + "\"name\":\"parent\","
                        + "\"ref\":\"aglzbGltMy1nZW5yKgsSEU1vZGVsUmVmQXR0ck1vZGVsIhNjaGlsZChub3QgZXhwYW5kZWQpDA\","
                        + "\"refList\":[]}",
                    1);
        Assert
            .assertEquals(
                "aglzbGltMy1nZW5yLAsSEU1vZGVsUmVmQXR0ck1vZGVsIhVleHBhbmRlZCBncm91bmQgY2hpbGQM",
                KeyFactory.keyToString(model
                    .getExpandedRef()
                    .getModel()
                    .getExpandedRef()
                    .getKey()));
        Assert
            .assertEquals(
                "aglzbGltMy1nZW5yJQsSEU1vZGVsUmVmQXR0ck1vZGVsIg5leHBhbmRlZCBjaGlsZAw",
                KeyFactory.keyToString(model
                    .getExpandedRef()
                    .getModel()
                    .getKey()));
        Assert.assertEquals("expanded child", model
            .getExpandedRef()
            .getModel()
            .getName());
        Assert.assertEquals(
            "aglzbGltMy1nZW5yHQsSEU1vZGVsUmVmQXR0ck1vZGVsIgZwYXJlbnQM",
            KeyFactory.keyToString(model.getKey()));
        Assert.assertEquals("parent", model.getName());
        Assert
            .assertEquals(
                "aglzbGltMy1nZW5yKgsSEU1vZGVsUmVmQXR0ck1vZGVsIhNjaGlsZChub3QgZXhwYW5kZWQpDA",
                KeyFactory.keyToString(model.getRef().getKey()));
    }

    private ModelRefAttrModel getModel() {
        ModelRefAttrModel m = new ModelRefAttrModel("parent");
        ModelRefAttrModel child = new ModelRefAttrModel("expanded child");
        ModelRefAttrModel child2 = new ModelRefAttrModel("child(not expanded)");
        ModelRefAttrModel gchild =
            new ModelRefAttrModel("expanded ground child");
        ModelRefAttrModel ggchild =
            new ModelRefAttrModel("expanded child of expanded ground child");
        m.getExpandedRef().setModel(child);
        m.getRef().setModel(child2);
        child.getExpandedRef().setModel(gchild);
        gchild.getExpandedRef().setModel(ggchild);
        return m;
    }

    private ModelRefAttrModelMeta meta = ModelRefAttrModelMeta.get();
}
