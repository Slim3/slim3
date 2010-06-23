package slim3.demo.model;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.ShortBlob;

@Model
public class UploadedDataFragment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(lob = true)
    private byte[] bytes;

    private ShortBlob bytes2;

    /**
     * @return the bytes2
     */
    public ShortBlob getBytes2() {
        return bytes2;
    }

    /**
     * @param bytes2
     *            the bytes2 to set
     */
    public void setBytes2(ShortBlob bytes2) {
        this.bytes2 = bytes2;
    }

    private org.slim3.datastore.ModelRef<slim3.demo.model.UploadedData> uploadDataRef =
        new org.slim3.datastore.ModelRef<slim3.demo.model.UploadedData>(
            slim3.demo.model.UploadedData.class);

    private int index;

    /**
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * @return the array of bytes
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * @param bytes
     *            the array of bytes
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * @param index
     *            the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the uploadDataRef
     */
    public ModelRef<UploadedData> getUploadDataRef() {
        return uploadDataRef;
    }
}
