package slim3.demo.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slim3.controller.upload.FileItem;
import org.slim3.datastore.Datastore;
import org.slim3.util.ByteUtil;

import slim3.demo.meta.UploadedDataFragmentMeta;
import slim3.demo.meta.UploadedDataMeta;
import slim3.demo.model.UploadedData;
import slim3.demo.model.UploadedDataFragment;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class UploadService {

    private static final int FRAGMENT_SIZE = 900000;

    private UploadedDataMeta d = UploadedDataMeta.get();

    private UploadedDataFragmentMeta f = UploadedDataFragmentMeta.get();

    public List<UploadedData> getDataList() {
        return Datastore.query(d).asList();
    }

    public UploadedData upload(FileItem formFile) {
        if (formFile == null) {
            return null;
        }
        List<Object> models = new ArrayList<Object>();
        UploadedData data = new UploadedData();
        models.add(data);
        data.setKey(Datastore.allocateId(d));
        data.setFileName(formFile.getShortFileName());
        data.setLength(formFile.getData().length);
        byte[] bytes = formFile.getData();
        byte[][] bytesArray = ByteUtil.split(bytes, FRAGMENT_SIZE);
        Iterator<Key> keys =
            Datastore
                .allocateIds(data.getKey(), f, bytesArray.length)
                .iterator();
        for (int i = 0; i < bytesArray.length; i++) {
            byte[] fragmentData = bytesArray[i];
            UploadedDataFragment fragment = new UploadedDataFragment();
            models.add(fragment);
            fragment.setKey(keys.next());
            fragment.setBytes(fragmentData);
            fragment.setIndex(i);
            fragment.getUploadDataRef().setModel(data);
        }
        Transaction tx = Datastore.beginTransaction();
        for (Object model : models) {
            Datastore.put(tx, model);
        }
        tx.commit();
        return data;
    }

    public UploadedData getData(Key key, Long version) {
        return Datastore.get(d, key, version);
    }

    public byte[] getBytes(UploadedData uploadedData) {
        if (uploadedData == null) {
            throw new NullPointerException(
                "The uploadedData parameter must not be null.");
        }
        List<UploadedDataFragment> fragmentList =
            uploadedData.getFragmentListRef().getModelList();
        byte[][] bytesArray = new byte[fragmentList.size()][0];
        for (int i = 0; i < fragmentList.size(); i++) {
            bytesArray[i] = fragmentList.get(i).getBytes();
        }
        return ByteUtil.join(bytesArray);
    }

    public void delete(Key key) {
        Transaction tx = Datastore.beginTransaction();
        List<Key> keys = new ArrayList<Key>();
        keys.add(key);
        keys.addAll(Datastore.query(f, key).asKeyList());
        Datastore.delete(tx, keys);
        tx.commit();
    }
}