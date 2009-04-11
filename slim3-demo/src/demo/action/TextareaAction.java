package demo.action;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Execute;

public class TextareaAction {

    public String textarea;

    @Execute(validate = false)
    public ActionForward index() {
        textarea = "initial value";
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false)
    public ActionForward submit() {
        return new ActionForward("index.jsp");
    }
}