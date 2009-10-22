package org.slim3.datastore.meta;

import org.slim3.datastore.model.Bar;

/**
 * 
 */
public final class BarMeta extends org.slim3.datastore.ModelMeta<Bar> {

    /**
     * 
     */
    public BarMeta() {
        super("Bar", Bar.class);
    }

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<Bar, com.google.appengine.api.datastore.Key> key =
        new org.slim3.datastore.CoreAttributeMeta<Bar, com.google.appengine.api.datastore.Key>(
            this,
            "__key__",
            com.google.appengine.api.datastore.Key.class);

    /**
     * 
     */
    public org.slim3.datastore.StringAttributeMeta<Bar> sortValue =
        new org.slim3.datastore.StringAttributeMeta<Bar>(this, "sortValue");

    @Override
    protected void setKey(Object model,
            com.google.appengine.api.datastore.Key key) {
        Bar m = (Bar) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        throw new IllegalStateException(
            "The version property of the model(slim3.demo.model.Bar) is not defined.");
    }

    @Override
    protected void incrementVersion(Object model) {
    }

    @Override
    protected Bar entityToModel(com.google.appengine.api.datastore.Entity entity) {
        Bar model = new Bar();
        model.setKey(entity.getKey());
        model.setSortValue((java.lang.String) entity.getProperty("sortValue"));
        return model;
    }

    @Override
    protected com.google.appengine.api.datastore.Entity modelToEntity(
            java.lang.Object model) {
        Bar m = (Bar) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity("Bar");
        }
        entity.setProperty("sortValue", m.getSortValue());
        return entity;
    }
}