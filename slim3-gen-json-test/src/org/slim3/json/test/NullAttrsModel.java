package org.slim3.json.test;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.json.Json;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@Model
public class NullAttrsModel {
    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getNullString() {
        return nullString;
    }

    public void setNullString(String nullString) {
        this.nullString = nullString;
    }

    public Blob getNullBlob() {
        return nullBlob;
    }

    public void setNullBlob(Blob nullBlob) {
        this.nullBlob = nullBlob;
    }

    public Blob getNullBytesBlob() {
        return nullBytesBlob;
    }

    public void setNullBytesBlob(Blob nullBytesBlob) {
        this.nullBytesBlob = nullBytesBlob;
    }

    public Text getNullText() {
        return nullText;
    }

    public void setNullText(Text nullText) {
        this.nullText = nullText;
    }

    public Text getNullValueText() {
        return nullValueText;
    }

    public void setNullValueText(Text nullValueText) {
        this.nullValueText = nullValueText;
    }

    @Attribute(primaryKey = true)
    private Key key;
    @Json(ignoreNull = false)
    private String nullString;
    @Json(ignoreNull = false)
    private Blob nullBlob;
    @Json(ignoreNull = false)
    private Blob nullBytesBlob = new Blob(null);
    @Json(ignoreNull = false)
    private Text nullText;
    @Json(ignoreNull = false)
    private Text nullValueText = new Text(null);
}
