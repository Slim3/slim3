package org.slim3.json.test;

import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class NonPersistentAttrsModel {
    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getStringAttr() {
        return stringAttr;
    }

    public void setStringAttr(String stringProp) {
        this.stringAttr = stringProp;
    }

    public void setNoGetterStringAttr(String noGetterStringAttr) {
        this.noGetterStringAttr = noGetterStringAttr;
    }
    
    public String getNoSetterStringAttr() {
        return noSetterStringAttr;
    }

    public void setNoGetterBooleanAttr(boolean noGetterBooleanAttr) {
        this.noGetterBooleanAttr = noGetterBooleanAttr;
    }

    public boolean isNoSetterBooleanAttr() {
        return noSetterBooleanAttr;
    }

    public void setNoGetterComparableAttr(
            Comparable<Integer> noGetterComparableAttr) {
        this.noGetterComparableAttr = noGetterComparableAttr;
    }
    
    public Comparable<Integer> getNoSetterComparableAttr() {
        return noSetterComparableAttr;
    }
    
    public void setNoGetterIntArrayAttr(int[] noGetterIntArrayAttr) {
        this.noGetterIntArrayAttr = noGetterIntArrayAttr;
    }
    
    public int[] getNoSetterIntArrayAttr() {
        return noSetterIntArrayAttr;
    }
    
    public void setNoGetterIntegerListAttr(List<Integer> noGetterIntegerListAttr) {
        this.noGetterIntegerListAttr = noGetterIntegerListAttr;
    }
    
    public List<Integer> getNoSetterIntegerListAttr() {
        return noSetterIntegerListAttr;
    }

    
    @Attribute(primaryKey = true)
    private Key key;
    @Attribute(persistent = false)
    private String stringAttr;
    @Attribute(persistent = false)
    private String noGetterStringAttr;
    @Attribute(persistent = false)
    private String noSetterStringAttr;
    @Attribute(persistent = false)
    private String noGetterAndSetterStringAttr;
    @Attribute(persistent = false)
    private boolean noGetterBooleanAttr;
    @Attribute(persistent = false)
    private boolean noSetterBooleanAttr;
    @Attribute(persistent = false)
    private boolean noGetterAndSetterBooleanAttr;
    @Attribute(persistent = false)
    private Comparable<Integer> noGetterComparableAttr;
    @Attribute(persistent = false)
    private Comparable<Integer> noSetterComparableAttr;
    @Attribute(persistent = false)
    private Comparable<Integer> noGetterAndSetterComparableAttr;
    @Attribute(persistent = false)
    private int[] noGetterIntArrayAttr;
    @Attribute(persistent = false)
    private int[] noSetterIntArrayAttr;
    @Attribute(persistent = false)
    private int[] noGetterAndSetterIntArrayAttr;
    @Attribute(persistent = false)
    private List<Integer> noGetterIntegerListAttr;
    @Attribute(persistent = false)
    private List<Integer> noSetterIntegerListAttr;
    @Attribute(persistent = false)
    private List<Integer> noGetterAndSetterIntegerListAttr;
}
