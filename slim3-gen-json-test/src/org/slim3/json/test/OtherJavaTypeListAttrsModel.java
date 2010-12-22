package org.slim3.json.test;

import java.util.Date;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class OtherJavaTypeListAttrsModel {
    public enum WeekDay {
        Mon, Tue, Wed, Thi, Fri, Sat, Sun
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public List<String> getStringListAttr() {
        return stringListAttr;
    }

    public void setStringListAttr(List<String> stringListAttr) {
        this.stringListAttr = stringListAttr;
    }

    public List<Date> getDateListAttr() {
        return dateListAttr;
    }

    public void setDateListAttr(List<Date> dateListAttr) {
        this.dateListAttr = dateListAttr;
    }

    public List<WeekDay> getEnumListAttr() {
        return enumListAttr;
    }

    public void setEnumListAttr(List<WeekDay> enumListAttr) {
        this.enumListAttr = enumListAttr;
    }

    @Attribute(primaryKey = true)
    private Key key;
    private List<String> stringListAttr;
    private List<Date> dateListAttr;
    private List<WeekDay> enumListAttr;
}
