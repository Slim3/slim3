package demo.controller;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Controller;
import org.slim3.struts.annotation.Execute;

@Controller
public class ProtectController {

    @Execute(validate = false)
    public ActionForward index() {
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false, roles = "tomcat")
    public ActionForward tomcat() {
        return new ActionForward("tomcat.jsp");
    }
}