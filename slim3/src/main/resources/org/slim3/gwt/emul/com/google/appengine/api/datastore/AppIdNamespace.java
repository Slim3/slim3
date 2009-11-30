package com.google.appengine.api.datastore;

public class AppIdNamespace {

    private String appId;

    private String namespace;

    public AppIdNamespace(String appId, String namespace) {
        this.appId = appId;
        this.namespace = namespace;
    }

    public String getAppId() {
        return appId;
    }

    public String getNamespace() {
        return namespace;
    }
}