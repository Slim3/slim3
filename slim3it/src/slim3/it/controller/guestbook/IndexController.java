package slim3.it.controller.guestbook;

import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.it.dao.GreetingDao;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class IndexController extends Controller {

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(IndexController.class.getName());

    @Override
    public Navigation run() {
        GreetingDao dao = new GreetingDao();
        UserService service = UserServiceFactory.getUserService();
        User user = service.getCurrentUser();
        requestScope("user", user);
        requestScope("greetings", dao.findAll());
        if (user != null) {
            requestScope("logoutURL", service.createLogoutURL(basePath));
            return forward("index.jsp");
        }
        requestScope("loginURL", service.createLoginURL(basePath));
        return redirect(service.createLoginURL(basePath));
    }
}
