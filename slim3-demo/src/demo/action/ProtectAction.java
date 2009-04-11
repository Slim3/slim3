package demo.action;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Execute;

public class ProtectAction {

    @Execute(validate = false)
    public ActionForward index() {
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false, roles = "tomcat")
    public ActionForward tomcat() {
        return new ActionForward("tomcat.jsp");
    }
}