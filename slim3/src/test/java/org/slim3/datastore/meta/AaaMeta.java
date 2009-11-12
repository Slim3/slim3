package org.slim3.datastore.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "null" }, date = "2009-11-09 15:30:15")
/**
 * @author higa
 * 
 */
public final class AaaMeta extends
        org.slim3.datastore.ModelMeta<org.slim3.datastore.model.Aaa> {

    /**
     * 
     */
    public AaaMeta() {
        super("Aaa", org.slim3.datastore.model.Aaa.class);
    }

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Aaa, com.google.appengine.api.datastore.Key> key =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Aaa, com.google.appengine.api.datastore.Key>(
            this,
            "__key__",
            com.google.appengine.api.datastore.Key.class);

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Aaa, java.lang.Integer> schemaVersion =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Aaa, java.lang.Integer>(
            this,
            "schemaVersion",
            java.lang.Integer.class);

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Aaa, java.lang.Long> version =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Aaa, java.lang.Long>(
            this,
            "version",
            java.lang.Long.class);

    @Override
    protected void setKey(Object model,
            com.google.appengine.api.datastore.Key key) {
        org.slim3.datastore.model.Aaa m = (org.slim3.datastore.model.Aaa) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        org.slim3.datastore.model.Aaa m = (org.slim3.datastore.model.Aaa) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void incrementVersion(Object model) {
        org.slim3.datastore.model.Aaa m = (org.slim3.datastore.model.Aaa) model;
        long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
        m.setVersion(Long.valueOf(version + 1L));
    }

    @Override
    protected org.slim3.datastore.model.Aaa entityToModel(
            com.google.appengine.api.datastore.Entity entity) {
        org.slim3.datastore.model.Aaa model =
            new org.slim3.datastore.model.Aaa();
        model.setKey(entity.getKey());
        model.setSchemaVersion(longToInteger((java.lang.Long) entity
            .getProperty("schemaVersion")));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    protected com.google.appengine.api.datastore.Entity modelToEntity(
            java.lang.Object model) {
        org.slim3.datastore.model.Aaa m = (org.slim3.datastore.model.Aaa) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("schemaVersion", m.getSchemaVersion());
        entity.setProperty("version", m.getVersion());
        return entity;
    }
}