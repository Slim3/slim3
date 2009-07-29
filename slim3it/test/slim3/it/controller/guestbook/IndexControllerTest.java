package slim3.it.controller.guestbook;

import org.slim3.tester.JDOControllerTestCase;
import org.slim3.tester.TestEnvironment;

import slim3.it.model.Greeting;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.apphosting.api.ApiProxy;

public class IndexControllerTest extends JDOControllerTestCase {

    public void testLogin() throws Exception {
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment(
            "higayasuo@gmail.com"));
        UserService service = UserServiceFactory.getUserService();
        Greeting greeting = new Greeting();
        greeting.setAuthor(service.getCurrentUser());
        greeting.setContent("hello");
        makePersistentInTx(greeting);
        start("/guestbook/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/guestbook/index.jsp", getNextPath());
        assertNotNull(requestScope("user"));
        assertNotNull(service.getCurrentUser());
        assertEquals(
            service.createLogoutURL("/guestbook/"),
            asString("logoutURL"));
        assertNotNull(requestScope("greetings"));
    }

    public void testNotLogin() throws Exception {
        start("/guestbook/");
        IndexController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        UserService service = UserServiceFactory.getUserService();
        assertNull(service.getCurrentUser());
        assertEquals(service.createLoginURL("/guestbook/"), getNextPath());
        assertEquals(
            service.createLoginURL("/guestbook/"),
            asString("loginURL"));
        assertNull(requestScope("user"));
    }
}
