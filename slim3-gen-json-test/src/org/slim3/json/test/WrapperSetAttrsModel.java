package org.slim3.json.test;

import java.util.Set;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class WrapperSetAttrsModel {
    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Set<Boolean> getBooleanSetAttr() {
        return booleanSetAttr;
    }

    public void setBooleanSetAttr(Set<Boolean> booleanSetAttr) {
        this.booleanSetAttr = booleanSetAttr;
    }

    public Set<Short> getShortSetAttr() {
        return shortSetAttr;
    }

    public void setShortSetAttr(Set<Short> shortSetAttr) {
        this.shortSetAttr = shortSetAttr;
    }

    public Set<Integer> getIntegerSetAttr() {
        return integerSetAttr;
    }

    public void setIntegerSetAttr(Set<Integer> integerSetAttr) {
        this.integerSetAttr = integerSetAttr;
    }

    public Set<Long> getLongSetAttr() {
        return longSetAttr;
    }

    public void setLongSetAttr(Set<Long> longSetAttr) {
        this.longSetAttr = longSetAttr;
    }

    public Set<Float> getFloatSetAttr() {
        return floatSetAttr;
    }

    public void setFloatSetAttr(Set<Float> floatSetAttr) {
        this.floatSetAttr = floatSetAttr;
    }

    public Set<Double> getDoubleSetAttr() {
        return doubleSetAttr;
    }

    public void setDoubleSetAttr(Set<Double> doubleSetAttr) {
        this.doubleSetAttr = doubleSetAttr;
    }

    @Attribute(primaryKey = true)
    private Key key;
    private Set<Boolean> booleanSetAttr;
    private Set<Short> shortSetAttr;
    private Set<Integer> integerSetAttr;
    private Set<Long> longSetAttr;
    private Set<Float> floatSetAttr;
    private Set<Double> doubleSetAttr;
}
