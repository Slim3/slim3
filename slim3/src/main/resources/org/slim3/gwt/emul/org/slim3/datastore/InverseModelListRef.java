package org.slim3.datastore;

import java.util.List;

//import com.google.appengine.api.datastore.Query.SortPredicate;

public class InverseModelListRef<M, O> extends AbstractInverseModelRef<M, O> {

    private static final long serialVersionUID = 1L;

    //protected SortPredicate[] defaultSortPredicates;

    protected List<M> modelList;

    protected InverseModelListRef() {
    }

    public InverseModelListRef(Class<M> modelClass, String mappedPropertyName,
            O owner) {
        super(modelClass, mappedPropertyName, owner);
        //this.defaultSortPredicates = defaultSortPredicates;
    }

    public List<M> getModelList() {
        return modelList;
    }

    public ModelListQuery query() {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }
    
    public void clear() {
        modelList = null;
    }
    
    public class ModelListQuery {
    }
}