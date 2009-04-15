package demo.controller;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Controller;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.util.ResponseUtil;

@Controller
public class DownloadController {

    @Execute(validate = false)
    public ActionForward index() {
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false)
    public ActionForward download() {
        ResponseUtil.download("sample.txt", "Hello world".getBytes());
        return null;
    }
}