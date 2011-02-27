package org.slim3.json.test;

import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.InverseModelRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.json.Expanded;
import org.slim3.datastore.json.Json;

import com.google.appengine.api.datastore.Key;

@Model
public class ModelRefAttrModel {
    public ModelRefAttrModel() {
    }

    public ModelRefAttrModel(String name) {
        this.key = Datastore.createKey(ModelRefAttrModel.class, name);
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

    public ModelRef<ModelRefAttrModel> getExpandedRef() {
        return expandedRef;
    }

    public ModelRef<ModelRefAttrModel> getRef() {
        return ref;
    }

    public List<ModelRef<ModelRefAttrModel>> getExpandedRefList() {
        return expandedRefList;
    }

    public void setExpandedRefList(List<ModelRef<ModelRefAttrModel>> refList) {
        this.expandedRefList = refList;
    }

    public List<ModelRef<ModelRefAttrModel>> getRefList() {
        return refList;
    }

    public void setRefList(List<ModelRef<ModelRefAttrModel>> refList2) {
        this.refList = refList2;
    }

    public InverseModelRef<ModelRefAttrModel, ModelRefAttrModel> getInvRef() {
        return invRef;
    }

    @Attribute(primaryKey = true)
    private Key key;
    private String name;
    @Attribute(json = @Json(coder = Expanded.class))
    private ModelRef<ModelRefAttrModel> expandedRef =
        new ModelRef<ModelRefAttrModel>(ModelRefAttrModel.class);
    private ModelRef<ModelRefAttrModel> ref = new ModelRef<ModelRefAttrModel>(
        ModelRefAttrModel.class);
    @Attribute(persistent = false)
    private InverseModelRef<ModelRefAttrModel, ModelRefAttrModel> invRef
        = new InverseModelRef<ModelRefAttrModel, ModelRefAttrModel>(
                ModelRefAttrModel.class, "invRef", this);
    @Attribute(json = @Json(coder = Expanded.class))
    private List<ModelRef<ModelRefAttrModel>> expandedRefList =
        new ArrayList<ModelRef<ModelRefAttrModel>>();
    private List<ModelRef<ModelRefAttrModel>> refList =
        new ArrayList<ModelRef<ModelRefAttrModel>>();
}
