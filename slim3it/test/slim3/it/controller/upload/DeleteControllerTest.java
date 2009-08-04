package slim3.it.controller.upload;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Upload;

public class DeleteControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Upload upload = new Upload();
        makePersistentInTx(upload);
        param("key", upload.getKey());
        param("version", upload.getVersion());
        start("/upload/delete");
        DeleteController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/upload/", getDestinationPath());
        assertEquals(0, count(Upload.class));
    }
}
