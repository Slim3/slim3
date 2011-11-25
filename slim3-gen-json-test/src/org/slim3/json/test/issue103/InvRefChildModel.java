package org.slim3.json.test.issue103;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.InverseModelRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.json.Json;

import com.google.appengine.api.datastore.Key;

@Model
public class InvRefChildModel {
    public InvRefChildModel() {
    }

    public InvRefChildModel(String name) {
        this.key = Datastore.createKey(InvRefChildModel.class, name);
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

    public InverseModelRef<InvRefParentModel, InvRefChildModel> getInvRef() {
        return invRef;
    }

    public ModelRef<InvRefParentModel> getParentRef() {
        return parentRef;
    }

    @Attribute(primaryKey = true)
    @Json(ignore=true)
    private Key key;
    private String name;
    @Attribute(persistent=false)
    private InverseModelRef<InvRefParentModel, InvRefChildModel> invRef
        = new InverseModelRef<InvRefParentModel, InvRefChildModel>(
                InvRefParentModel.class, "childRef", this);
    @Json(ignore=true)
    private ModelRef<InvRefParentModel> parentRef
        = new ModelRef<InvRefParentModel>(InvRefParentModel.class);
}
