package slim3.it.controller;

import java.util.Date;
import java.util.TimeZone;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class IndexController extends Controller {

    @Override
    public Navigation execute() {
        return forward();
    }

    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    public Date getNow() {
        return new Date();
    }
}