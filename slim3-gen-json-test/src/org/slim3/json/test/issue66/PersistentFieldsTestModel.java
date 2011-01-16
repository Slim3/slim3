package org.slim3.json.test.issue66;

import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.CreationDate;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import com.google.appengine.api.datastore.Key;

@Model
public class PersistentFieldsTestModel{
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public ModelRef<PersistentFieldsTestModel> getRef() {
        return ref;
    }

    @Attribute(primaryKey=true)
    private Key key;
    @Attribute(persistent=false)
    private int value;
    @Attribute(persistent=false, listener=CreationDate.class)
    private Date createdAt;
    @Attribute(persistent=false)
    private ModelRef<PersistentFieldsTestModel> ref
        = new ModelRef<PersistentFieldsTestModel>(PersistentFieldsTestModel.class);
}
