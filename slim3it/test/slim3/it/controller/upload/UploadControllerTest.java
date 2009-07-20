package slim3.it.controller.upload;

import java.util.List;

import org.slim3.controller.upload.FileItem;
import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Upload;
import slim3.it.model.UploadData;

public class UploadControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        FileItem fileItem = new FileItem("aaa", "bbb", new byte[] { 1 });
        requestScope("formFile", fileItem);
        start("/upload/upload");
        UploadController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/upload/", getNextPath());
        Upload upload = from(Upload.class).getSingleResult();
        assertNotNull(upload);
        assertEquals(1, upload.getLength());
        List<UploadData> dataList = upload.getDataList();
        assertNotNull(dataList);
        assertEquals(1, dataList.size());
        UploadData uploadData = dataList.get(0);
        byte[] bytes = uploadData.getBytes();
        assertEquals(1, bytes.length);
        assertEquals(1, bytes[0]);
    }
}
