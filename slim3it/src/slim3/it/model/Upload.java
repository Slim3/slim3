package slim3.it.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

/**
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
public class Upload implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String key;

    @Persistent
    private Long version = 1L;

    @Persistent
    private String fileName;

    @Persistent
    private int length;

    @Persistent
    private List<UploadData> dataList = new ArrayList<UploadData>();

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length
     *            the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the dataList
     */
    public List<UploadData> getDataList() {
        return dataList;
    }

    /**
     * @param dataList
     *            the dataList to set
     */
    public void setDataList(List<UploadData> dataList) {
        this.dataList = dataList;
    }

    public Long getVersion() {
        return version;
    }
}