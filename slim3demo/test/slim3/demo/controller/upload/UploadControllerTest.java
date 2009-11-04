package slim3.demo.controller.upload;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.controller.upload.FileItem;
import org.slim3.tester.ControllerTestCase;

import slim3.demo.model.UploadedData;
import slim3.demo.model.UploadedDataFragment;

public class UploadControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        FileItem fileItem =
            new FileItem("aaa.txt", "text/plain", new byte[] { 1 });
        tester.requestScope("formFile", fileItem);
        tester.start("/upload/upload");
        UploadController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/upload/"));
        assertThat(tester.count(UploadedData.class), is(1));
        assertThat(tester.count(UploadedDataFragment.class), is(1));
    }
}
