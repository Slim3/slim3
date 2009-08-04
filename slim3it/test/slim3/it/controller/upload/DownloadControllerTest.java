package slim3.it.controller.upload;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Upload;
import slim3.it.model.UploadData;

public class DownloadControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Upload upload = new Upload();
        UploadData uploadData = new UploadData();
        uploadData.setBytes(new byte[] { 1 });
        upload.getDataList().add(uploadData);
        makePersistentInTx(upload);
        param("key", upload.getKey());
        param("version", upload.getVersion());
        start("/upload/download");
        DownloadController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertNull(getDestinationPath());
        byte[] bytes = response.getOutputAsByteArray();
        assertEquals(1, bytes.length);
        assertEquals(1, bytes[0]);
    }
}