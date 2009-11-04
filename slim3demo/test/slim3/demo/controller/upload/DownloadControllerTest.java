package slim3.demo.controller.upload;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.controller.upload.FileItem;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import slim3.demo.model.UploadedData;
import slim3.demo.service.UploadService;

public class DownloadControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        FileItem formFile =
            new FileItem("aaa.txt", "text/html", new byte[] { 'a' });
        UploadedData data = new UploadService().upload(formFile);
        tester.param("key", Datastore.keyToString(data.getKey()));
        tester.param("version", data.getVersion());
        tester.start("/upload/download");
        DownloadController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is(nullValue()));
        byte[] bytes = tester.response.getOutputAsByteArray();
        assertEquals(1, bytes.length);
        assertEquals('a', bytes[0]);
    }
}