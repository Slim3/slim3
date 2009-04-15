package demo.controller;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Controller;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.annotation.SessionScope;

@Controller
public class MultiselectController {

    @SessionScope
    public String[] multiselect;

    @Execute(validate = false)
    public ActionForward index() {
        multiselect = new String[] { "2" };
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false, reset = "reset")
    public ActionForward submit() {
        return new ActionForward("index.jsp");
    }

    public void reset() {
        multiselect = new String[0];
    }
}