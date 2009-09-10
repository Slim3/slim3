package slim3.it.controller.upload;

import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.ByteUtil;

import slim3.it.dao.UploadDao;
import slim3.it.model.Upload;
import slim3.it.model.UploadData;

public class DownloadController extends Controller {

    private UploadDao dao = new UploadDao();

    @Override
    public Navigation run() {
        Upload upload = dao.getObjectById(key(), version());
        List<UploadData> dataList = upload.getDataList();
        byte[][] bytesArray = new byte[dataList.size()][0];
        for (int i = 0; i < dataList.size(); i++) {
            UploadData data = dataList.get(i);
            bytesArray[i] = data.getBytes();
        }
        byte[] bytes = ByteUtil.join(bytesArray);
        download(upload.getFileName(), bytes);
        return null;
    }
}