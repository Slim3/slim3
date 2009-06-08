package slim3.it.controller.upload;

import java.io.InputStream;

import org.apache.commons.fileupload.util.Streams;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.upload.FileItemIterator;
import org.slim3.controller.upload.FileItemStream;
import org.slim3.controller.upload.FileUpload;
import org.slim3.util.RuntimeExceptionUtil;

public class UploadController extends Controller {

    @Override
    public Navigation run() {
        try {
            FileUpload upload = new FileUpload();
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    System.out.println("Form field "
                        + name
                        + " with value "
                        + Streams.asString(stream)
                        + " detected.");
                } else {
                    System.out.println("File field "
                        + name
                        + " with file name "
                        + item.getName()
                        + " "
                        + Streams.asString(stream, "Shift_JIS")
                        + " detected.");
                }
            }
        } catch (Exception e) {
            RuntimeExceptionUtil.wrapAndThrow(e);
        }
        return null;
    }
}
