package org.slim3.json.test;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@Model
public class CRLFStringTestModel{
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    public String getStringValue() {
        return stringValue;
    }
    public void setStringValue(String value) {
        this.stringValue = value;
    }
    public Text getTextValue() {
        return textValue;
    }
    public void setTextValue(Text textValue) {
        this.textValue = textValue;
    }

    @Attribute(primaryKey=true)
    private Key key;
    private String stringValue;
    private Text textValue;
}
