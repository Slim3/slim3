package slim3.demo.model;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;

import slim3.demo.meta.UploadedDataFragmentMeta;

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

    @Attribute(persistent = false)
    private InverseModelListRef<UploadedDataFragment> fragmentListRef =
        new InverseModelListRef<UploadedDataFragment>(
            UploadedDataFragmentMeta.get().uploadDataRef,
            this,
            UploadedDataFragmentMeta.get().index.asc);

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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * @return the fragmentListRef
     */
    public InverseModelListRef<UploadedDataFragment> getFragmentListRef() {
        return fragmentListRef;
    }
}