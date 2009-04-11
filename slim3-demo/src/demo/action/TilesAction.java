package demo.action;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Execute;

public class TilesAction {

    public String message;

    @Execute(validate = false)
    public ActionForward index() {
        message = "Welcome Slim3!";
        return new ActionForward("index.jsp");
    }
}