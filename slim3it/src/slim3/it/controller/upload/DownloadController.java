package slim3.it.controller.upload;

import java.util.List;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.ByteUtil;

import slim3.it.model.Upload;
import slim3.it.model.UploadData;

public class DownloadController extends JDOController {

    @Override
    public Navigation run() {
        Upload upload = pm.getObjectById(Upload.class, requestScope("key"));
        List<UploadData> dataList = upload.getDataList();
        byte[][] bytesArray = new byte[dataList.size()][0];
        for (int i = 0; i < dataList.size(); i++) {
            UploadData data = dataList.get(i);
            bytesArray[i] = data.getBlob().getBytes();
        }
        byte[] bytes = ByteUtil.join(bytesArray);
        download(upload.getFileName(), bytes);
        return null;
    }
}