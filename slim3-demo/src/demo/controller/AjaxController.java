package demo.controller;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Controller;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.util.ResponseUtil;

@Controller
public class AjaxController {

    @Execute(validate = false)
    public ActionForward index() {
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false)
    public ActionForward hello() {
        ResponseUtil.write("Hello World");
        return null;
    }
}