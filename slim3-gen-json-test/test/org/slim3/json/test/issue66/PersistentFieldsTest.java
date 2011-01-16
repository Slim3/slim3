package org.slim3.json.test.issue66;

import org.junit.Assert;
import org.junit.Test;
import org.slim3.tester.TestEnvironment;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.apphosting.api.ApiProxy;

public class PersistentFieldsTest {
    @Test
    public void test() throws Exception{
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment() {
            @Override
            public String getAppId() {
                return "slim3-gen";
            }
        });
        PersistentFieldsTestModel m = new PersistentFieldsTestModel();
        m.setValue(10);
        m.getRef().setKey(KeyFactory.createKey(meta.getKind(), "name"));
        System.out.println(meta.modelToJson(m));
        Assert.assertEquals("{" +
        		"\"ref\":\"aglzbGltMy1nZW5yIwsSGVBlcnNpc3RlbnRGaWVsZHNUZXN0TW9kZWwiBG5hbWUM\"," +
        		"\"value\":10}", meta.modelToJson(m));
    }
    
    private PersistentFieldsTestModelMeta meta = PersistentFieldsTestModelMeta.get();
}
