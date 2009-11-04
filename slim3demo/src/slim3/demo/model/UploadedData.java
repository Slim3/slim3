package slim3.demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class UploadedData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version = 0L;

    private String fileName;

    private int length;

    private List<Key> fragmentKeyList = new ArrayList<Key>();

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<Key> getFragmentKeyList() {
        return fragmentKeyList;
    }

    public void setFragmentKeyList(List<Key> fragmentKeyList) {
        this.fragmentKeyList = fragmentKeyList;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}