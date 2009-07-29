package slim3.it.controller.guestbook;

import org.slim3.tester.JDOControllerTestCase;
import org.slim3.tester.TestEnvironment;

import slim3.it.model.Greeting;

import com.google.apphosting.api.ApiProxy;

public class SignControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment(
            "higayasuo@gmail.com"));
        param("content", "hello");
        start("/guestbook/sign");
        SignController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/guestbook/", getNextPath());
        Greeting greeting = from(Greeting.class).getFirstResult();
        assertNotNull(greeting);
        assertEquals("hello", greeting.getContent());
        assertEquals("higayasuo@gmail.com", greeting.getAuthor().getEmail());
    }
}
