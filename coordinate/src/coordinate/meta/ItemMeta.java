package coordinate.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2011-03-17 16:42:13")
/** */
public final class ItemMeta extends org.slim3.datastore.ModelMeta<coordinate.model.Item> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<coordinate.model.Item, coordinate.enums.Category> category = new org.slim3.datastore.CoreAttributeMeta<coordinate.model.Item, coordinate.enums.Category>(this, "category", "category", coordinate.enums.Category.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<coordinate.model.Item, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<coordinate.model.Item, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<coordinate.model.Item, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<coordinate.model.Item, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final ItemMeta slim3_singleton = new ItemMeta();

    /**
     * @return the singleton
     */
    public static ItemMeta get() {
       return slim3_singleton;
    }

    /** */
    public ItemMeta() {
        super("Item", coordinate.model.Item.class);
    }

    @Override
    public coordinate.model.Item entityToModel(com.google.appengine.api.datastore.Entity entity) {
        coordinate.model.Item model = new coordinate.model.Item();
        model.setCategory(stringToEnum(coordinate.enums.Category.class, (java.lang.String) entity.getProperty("category")));
        model.setKey(entity.getKey());
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        coordinate.model.Item m = (coordinate.model.Item) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("category", enumToString(m.getCategory()));
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        coordinate.model.Item m = (coordinate.model.Item) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        coordinate.model.Item m = (coordinate.model.Item) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        coordinate.model.Item m = (coordinate.model.Item) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        coordinate.model.Item m = (coordinate.model.Item) model;
        long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
        m.setVersion(Long.valueOf(version + 1L));
    }

    @Override
    protected void prePut(Object model) {
    }

    @Override
    public String getSchemaVersionName() {
        return "slim3.schemaVersion";
    }

    @Override
    public String getClassHierarchyListName() {
        return "slim3.classHierarchyList";
    }

    @Override
    protected boolean isCipherProperty(String propertyName) {
        return false;
    }

    @Override
    protected void modelToJson(org.slim3.datastore.json.JsonWriter writer, java.lang.Object model, int maxDepth, int currentDepth) {
        coordinate.model.Item m = (coordinate.model.Item) model;
        writer.beginObject();
        org.slim3.datastore.json.JsonCoder encoder = null;
        if(m.getCategory() != null){
            writer.setNextPropertyName("category");
            encoder = new org.slim3.datastore.json.Default();
            encoder.encode(writer, m.getCategory());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder = new org.slim3.datastore.json.Default();
            encoder.encode(writer, m.getKey());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder = new org.slim3.datastore.json.Default();
            encoder.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    public coordinate.model.Item jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        coordinate.model.Item m = new coordinate.model.Item();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.JsonCoder decoder = null;
        reader = rootReader.newObjectReader("category");
        decoder = new org.slim3.datastore.json.Default();
        m.setCategory(decoder.decode(reader, m.getCategory(), coordinate.enums.Category.class));
        reader = rootReader.newObjectReader("key");
        decoder = new org.slim3.datastore.json.Default();
        m.setKey(decoder.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("version");
        decoder = new org.slim3.datastore.json.Default();
        m.setVersion(decoder.decode(reader, m.getVersion()));
    return m;
}
}