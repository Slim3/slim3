package com.google.appengine.api.datastore;

public class Key {

    private Key parentKey;

    private String kind;

    private String appId;

    private long id;

    private String name;

    private AppIdNamespace appIdNamespace;

    Key(String kind, Key parentKey, long id, String name,
        AppIdNamespace appIdNamespace) {
        this.name = name;
        this.id = id;
        this.parentKey = parentKey;
        this.kind = kind.intern();
        this.appIdNamespace = appIdNamespace;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(30);
        appendToString(buffer);
        return buffer.toString();
    }

    private void appendToString(StringBuffer buffer) {
        if (parentKey != null) {
            parentKey.appendToString(buffer);
            buffer.append("/");
        }
        buffer.append(kind);
        buffer.append("(");
        if(name != null) {
            buffer.append("\"").append(name).append("\"");
        } else {
            buffer.append(String.valueOf(id));
        }
        buffer.append(")");
    }
  
    public String getKind() {
        return kind;
    }

    public Key getParent() {
        return parentKey;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AppIdNamespace getAppIdNamespace() {
        return appIdNamespace;
    }
}