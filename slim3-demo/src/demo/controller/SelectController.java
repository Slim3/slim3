package demo.controller;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Controller;
import org.slim3.struts.annotation.Execute;

@Controller
public class SelectController {

    public String select;

    @Execute(validate = false)
    public ActionForward index() {
        select = "3";
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false)
    public ActionForward submit() {
        return new ActionForward("index.jsp");
    }
}