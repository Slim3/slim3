package org.slim3.json.test;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.slim3.datastore.json.Default;
import org.slim3.datastore.json.JsonReader;
import org.slim3.datastore.json.JsonWriter;

public class CustomAttributesModelCoder extends Default{
    @Override
    public void encode(JsonWriter writer, Object value) {
        if(value instanceof Point){
            Point pt = (Point)value;
            writer.beginObject();
            writer.writeValueProperty("x", pt.x);
            writer.writeValueProperty("y", pt.y);
            writer.endObject();
            return;
        } else if(value instanceof Point2D){
            Point2D pt = (Point2D)value;
            writer.beginObject();
            writer.writeValueProperty("x", pt.getX());
            writer.writeValueProperty("y", pt.getY());
            writer.endObject();
            return;
        }
        super.encode(writer, value);
    }

    @Override
    public <T> T decode(JsonReader reader, T defaultValue, Class<T> clazz){
        try{
            if(Point.class.isAssignableFrom(clazz)){
                int x = Integer.parseInt(reader.readProperty("x"));
                int y = Integer.parseInt(reader.readProperty("y"));
                return clazz.cast(new Point(x, y));
            } else if(Point2D.class.isAssignableFrom(clazz)){
                float x = Float.parseFloat(reader.readProperty("x"));
                float y = Float.parseFloat(reader.readProperty("y"));
                return clazz.cast(new Point2D.Float(x, y));
            }
        } catch(NumberFormatException e){
        }
        return defaultValue;
    }
}
