package slim3.it.controller;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.slim3.mvc.MvcConstants;
import org.slim3.mvc.controller.Controller;
import org.slim3.mvc.controller.Navigation;

public class IndexController extends Controller {

    @Override
    public Navigation execute() {
        request.getSession().setAttribute(
            MvcConstants.LOCALE_KEY,
            Locale.JAPANESE);
        return forward();
    }

    public Locale getLocale() {
        return Locale.getDefault();
    }

    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    public Date getNow() {
        return new Date();
    }
}