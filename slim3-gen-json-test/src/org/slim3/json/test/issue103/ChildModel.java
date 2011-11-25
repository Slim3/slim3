package org.slim3.json.test.issue103;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.json.Json;

import com.google.appengine.api.datastore.Key;

@Model
public class ChildModel {
    public ChildModel() {
    }

    public ChildModel(String name) {
        this.key = Datastore.createKey(ChildModel.class, name);
        this.name = name;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModelRef<ParentModel> getParent() {
        return parent;
    }

    @Attribute(primaryKey = true)
    @Json(ignore=true)
    private Key key;
    private String name;
    @Json(ignore=true)
    private ModelRef<ParentModel> parent
        = new ModelRef<ParentModel>(ParentModel.class);
}
