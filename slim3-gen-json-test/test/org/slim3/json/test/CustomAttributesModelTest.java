package org.slim3.json.test;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

import org.junit.Assert;
import org.junit.Test;

public class CustomAttributesModelTest {
    @Test
    public void modelToJson() throws Exception {
        CustomAttributesModel m = new CustomAttributesModel();
        m.setPt(new Point(100, 200));
        m.setPt2d(new Point2D.Float(1.0f, 0.5f));
        m.setColor(Color.BLUE);

        Assert.assertEquals(
            "{"
            + "\"pt\":{\"x\":100,\"y\":200}"
            + ",\"point2d\":{\"x\":1.0,\"y\":0.5}"
            + "}",
            meta.modelToJson(m));
    }

    @Test
    public void jsonToModel() {
        CustomAttributesModel m = meta.jsonToModel(
            "{"
            + "\"color\":1000"
            + ",\"pt\":{\"x\":100,\"y\":200}"
            + ",\"point2d\":{\"x\":1.0,\"y\":0.5}"
            + "}"
            );
        Assert.assertEquals(new Point(100, 200), m.getPt());
        Assert.assertEquals(new Point2D.Float(1.0f, 0.5f), m.getPt2d());
        Assert.assertNull(m.getColor());
    }

    private static CustomAttributesModelMeta meta = CustomAttributesModelMeta.get();
}
