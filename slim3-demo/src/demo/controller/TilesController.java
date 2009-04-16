package demo.controller;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Execute;

public class TilesController {

    public String message;

    @Execute(validate = false)
    public ActionForward index() {
        message = "Welcome Slim3!";
        return new ActionForward("index.jsp");
    }
}