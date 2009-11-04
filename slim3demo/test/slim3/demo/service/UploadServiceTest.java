package slim3.demo.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.controller.upload.FileItem;
import org.slim3.datastore.Datastore;
import org.slim3.tester.LocalServiceTestCase;

import slim3.demo.meta.UploadedDataFragmentMeta;
import slim3.demo.meta.UploadedDataMeta;
import slim3.demo.model.UploadedData;
import slim3.demo.model.UploadedDataFragment;

public class UploadServiceTest extends LocalServiceTestCase {

    private UploadService service = new UploadService();

    @Test
    public void test() throws Exception {
        assertThat(service, is(notNullValue()));
    }

    @Test
    public void getDataList() throws Exception {
        UploadedData data = new UploadedData();
        Datastore.put(data);
        assertThat(service.getDataList().size(), is(1));
    }

    @Test
    public void upload() throws Exception {
        UploadedDataMeta d = new UploadedDataMeta();
        UploadedDataFragmentMeta f = new UploadedDataFragmentMeta();
        FileItem formFile =
            new FileItem("aaa.txt", "text/html", new byte[] { 'a' });
        UploadedData data = Datastore.get(d, service.upload(formFile).getKey());
        assertThat(data.getFileName(), is("aaa.txt"));
        assertThat(data.getLength(), is(1));
        assertThat(data.getFragmentKeyList().size(), is(1));
        UploadedDataFragment fragment =
            Datastore.get(f, data.getFragmentKeyList().get(0));
        assertThat(fragment.getBytes(), is(new byte[] { 'a' }));
    }

    @Test
    public void getBytes() throws Exception {
        FileItem formFile =
            new FileItem("aaa.txt", "text/html", new byte[] { 'a' });
        UploadedData data = service.upload(formFile);
        assertThat(service.getBytes(data), is(new byte[] { 'a' }));
    }

    @Test
    public void getData() throws Exception {
        UploadedData data = new UploadedData();
        Datastore.put(data);
        assertThat(
            service.getData(data.getKey(), data.getVersion()),
            is(notNullValue()));
    }

    @Test
    public void delete() throws Exception {
        UploadedData data = new UploadedData();
        Datastore.put(data);
        service.delete(data.getKey());
        assertThat(tester.count(UploadedData.class), is(0));
    }
}
