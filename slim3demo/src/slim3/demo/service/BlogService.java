package slim3.demo.service;

import java.util.List;
import java.util.Map;

import org.slim3.datastore.Datastore;
import org.slim3.util.BeanUtil;

import slim3.demo.meta.BlogMeta;
import slim3.demo.model.Blog;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class BlogService {

    private BlogMeta b = BlogMeta.get();

    public Blog get(Key key, Long version) {
        return Datastore.get(b, key, version);
    }

    public List<Blog> getAll() {
        return Datastore.query(b).asList();
    }

    public void insert(Blog blog) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, blog);
        tx.commit();
    }

    public Blog update(Key key, Long version, Map<String, Object> input) {
        Transaction tx = Datastore.beginTransaction();
        Blog blog = Datastore.get(tx, b, key, version);
        BeanUtil.copy(input, blog);
        Datastore.put(tx, blog);
        tx.commit();
        return blog;
    }

    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Blog blog = Datastore.get(tx, b, key, version);
        Datastore.delete(tx, blog.getKey());
        tx.commit();
    }
}