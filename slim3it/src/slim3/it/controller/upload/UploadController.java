package slim3.it.controller.upload;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.RuntimeExceptionUtil;

public class UploadController extends Controller {

    @Override
    public Navigation run() {
        try {
            byte[] formFile = requestScope("formFile");
            System.out.println("formFile:" + new String(formFile, "Shift_JIS"));
            String[] aaaArray = requestScope("aaaArray");
            for (String aaa : aaaArray) {
                System.out.println("aaa:" + aaa);
            }
        } catch (Exception e) {
            RuntimeExceptionUtil.wrapAndThrow(e);
        }
        return forward("index.jsp");
    }
}
