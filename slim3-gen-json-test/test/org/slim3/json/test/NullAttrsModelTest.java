package org.slim3.json.test;

import junit.framework.Assert;

import org.junit.Test;

public class NullAttrsModelTest {
    @Test
    public void modelToJson() throws Exception {
        Assert.assertEquals(
            "{\"nullBlob\":null,\"nullBytesBlob\":null,\"nullString\":\"null\","
                + "\"nullText\":null,\"nullValueText\":null}",
            meta.modelToJson(new NullAttrsModel()));
    }

    private NullAttrsModelMeta meta = NullAttrsModelMeta.get();
}
