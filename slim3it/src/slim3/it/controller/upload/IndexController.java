package slim3.it.controller.upload;

import java.util.ArrayList;
import java.util.List;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanMap;
import org.slim3.util.BeanUtil;
import org.slim3.util.CopyOptions;

import slim3.it.model.Upload;
import slim3.it.model.UploadMeta;

public class IndexController extends JDOController {

    @Override
    public Navigation run() {
        UploadMeta u = new UploadMeta();
        List<Upload> list = from(u).getResultList();
        List<BeanMap> uploadList = new ArrayList<BeanMap>(list.size());
        for (Upload upload : list) {
            BeanMap map = new BeanMap();
            BeanUtil.copy(upload, map, new CopyOptions().exclude("dataList"));
            uploadList.add(map);
        }
        requestScope("uploadList", uploadList);
        return forward("index.jsp");
    }
}
