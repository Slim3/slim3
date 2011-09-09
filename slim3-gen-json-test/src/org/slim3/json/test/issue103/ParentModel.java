package org.slim3.json.test.issue103;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.json.Expanded;
import org.slim3.datastore.json.Json;

import com.google.appengine.api.datastore.Key;

@Model
public class ParentModel {
    public ParentModel() {
    }

    public ParentModel(String name) {
        this.key = Datastore.createKey(ParentModel.class, name);
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

    public InverseModelListRef<ChildModel, ParentModel> getChildren() {
        return children;
    }

    @Attribute(primaryKey = true, json=@Json(ignore=true))
    private Key key;
    private String name;

    @Attribute(persistent=false, json=@Json(ignore=false, coder=Expanded.class))
    private InverseModelListRef<ChildModel, ParentModel> children
        = new InverseModelListRef<ChildModel, ParentModel>(ChildModel.class, "parent", this);
}
