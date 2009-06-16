package slim3.it.controller.upload;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.Upload;

public class DeleteControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        Upload upload = new Upload();
        pm.makePersistent(upload);
        param("key", upload.getKey());
        start("/upload/delete");
        DeleteController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/upload/", getNextPath());
        assertEquals(0, count(Upload.class));
    }
}
