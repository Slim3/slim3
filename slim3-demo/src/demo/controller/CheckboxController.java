package demo.controller;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Controller;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.annotation.SessionScope;

@Controller
public class CheckboxController {

    @SessionScope
    public Boolean check1;

    @SessionScope
    public Boolean check2;

    @Execute(validate = false)
    public ActionForward index() {
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false, reset = "reset")
    public ActionForward submit() {
        return new ActionForward("submit.jsp");
    }

    @Execute(validate = false)
    public ActionForward moveToIndexPage() {
        return new ActionForward("", true);
    }

    public void reset() {
        check1 = false;
        check2 = false;
    }
}