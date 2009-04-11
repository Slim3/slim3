package demo.action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.upload.FormFile;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.annotation.Required;
import org.slim3.struts.util.UploadUtil;

public class UploadAction {

    @Required
    public FormFile formFile;

    public String message;

    @Execute(validate = false)
    public ActionForward index() {
        UploadUtil.checkSizeLimit();
        return new ActionForward("index.jsp");
    }

    @Execute(input = "index.jsp")
    public ActionForward upload() {
        message = formFile.getFileName() + " is uploaded. The size is "
                + formFile.getFileSize() + " bytes.";
        return new ActionForward("index.jsp");
    }
}