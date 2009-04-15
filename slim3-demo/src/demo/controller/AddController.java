package demo.controller;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Controller;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.annotation.IntegerType;
import org.slim3.struts.annotation.Required;

@Controller
public class AddController {

    @Required
    @IntegerType
    public String arg1;

    @Required
    @IntegerType
    public String arg2;

    public Integer result;

    @Execute(validate = false)
    public ActionForward index() {
        return new ActionForward("index.jsp");
    }

    @Execute(input = "index.jsp")
    public ActionForward submit() {
        result = Integer.valueOf(arg1) + Integer.valueOf(arg2);
        return new ActionForward("index.jsp");
    }
}