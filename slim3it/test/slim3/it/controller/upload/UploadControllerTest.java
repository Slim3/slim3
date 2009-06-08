package slim3.it.controller.upload;

import org.slim3.tester.ControllerTestCase;

public class UploadControllerTest extends ControllerTestCase {

    public void testRun() throws Exception {
        request
            .setContentType("multipart/form-data; boundary=---------------------------253022096932392");
        start("/upload/upload");
        UploadController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertNull(getNextPath());
    }
}
