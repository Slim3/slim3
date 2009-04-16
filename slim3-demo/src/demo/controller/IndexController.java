package demo.controller;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Execute;

public class IndexController {

    @Execute(validate = false)
    public ActionForward index() {
        return new ActionForward("index.jsp");
    }
}