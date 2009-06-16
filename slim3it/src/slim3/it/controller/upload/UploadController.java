package slim3.it.controller.upload;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.controller.upload.FileItem;
import org.slim3.util.ByteUtil;

import slim3.it.model.Upload;
import slim3.it.model.UploadData;

import com.google.appengine.api.datastore.Blob;

public class UploadController extends JDOController {

    private static final int SIZE = 9000000;

    @Override
    public Navigation run() {
        FileItem formFile = requestScope("formFile");
        Upload upload = new Upload();
        upload.setFileName(formFile.getFileName());
        upload.setLength(formFile.getData().length);
        byte[] bytes = formFile.getData();
        byte[][] bytesArray = ByteUtil.split(bytes, SIZE);
        for (byte[] data : bytesArray) {
            UploadData uploadData = new UploadData();
            uploadData.setBlob(new Blob(data));
            upload.getDataList().add(uploadData);
        }
        pm.makePersistent(upload);
        return redirect(basePath);
    }
}
