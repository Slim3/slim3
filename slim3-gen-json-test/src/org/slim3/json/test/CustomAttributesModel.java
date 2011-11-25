package org.slim3.json.test;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.json.Json;

import com.google.appengine.api.datastore.Key;

@Model
public class CustomAttributesModel {
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    public Point getPt() {
        return pt;
    }
    public void setPt(Point pt) {
        this.pt = pt;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public Point2D getPt2d() {
        return pt2d;
    }
    public void setPt2d(Point2D pt2d) {
        this.pt2d = pt2d;
    }

    @Attribute(primaryKey = true)
    private Key key;
    @Json(coder=CustomAttributesModelCoder.class)
    @Attribute(persistent = false)
    private Point pt;
    @Attribute(persistent = false)
    private Color color;
    @Attribute(persistent = false)
    @Json(alias="point2d", coder=CustomAttributesModelCoder.class)
    private Point2D pt2d;
}
