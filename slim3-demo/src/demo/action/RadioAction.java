package demo.action;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Execute;

public class RadioAction {

    public String radio;

    @Execute(validate = false)
    public ActionForward index() {
        radio = "3";
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false)
    public ActionForward submit() {
        return new ActionForward("index.jsp");
    }
}