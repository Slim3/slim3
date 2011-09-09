package org.slim3.json.test.issue103;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.json.Expanded;
import org.slim3.datastore.json.Json;

import com.google.appengine.api.datastore.Key;

@Model
public class InvRefParentModel {
    public InvRefParentModel() {
    }

    public InvRefParentModel(String name) {
        this.key = Datastore.createKey(InvRefParentModel.class, name);
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
    
    public ModelRef<InvRefChildModel> getRef() {
        return ref;
    }

    public InverseModelListRef<InvRefChildModel, InvRefParentModel> getInvListRef() {
        return invListRef;
    }

    @Attribute(primaryKey = true, json=@Json(ignore=true))
    private Key key;
    private String name;

    private ModelRef<InvRefChildModel> ref
        = new ModelRef<InvRefChildModel>(InvRefChildModel.class);
    @Attribute(persistent=false, json=@Json(ignore=false, coder=Expanded.class))
    private InverseModelListRef<InvRefChildModel, InvRefParentModel> invListRef
        = new InverseModelListRef<InvRefChildModel, InvRefParentModel>(InvRefChildModel.class, "parentRef", this);
}
