package slim3.demo.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.EntityNotFoundRuntimeException;
import org.slim3.tester.AppEngineTestCase;

import slim3.demo.model.Blog;

public class BlogServiceTest extends AppEngineTestCase {

    private BlogService service = new BlogService();

    @Test
    public void test() throws Exception {
        assertThat(service, is(notNullValue()));
    }

    @Test
    public void get() throws Exception {
        Blog blog = new Blog();
        Datastore.put(blog);
        assertThat(
            service.get(blog.getKey(), blog.getVersion()),
            is(notNullValue()));
    }

    @Test(expected = EntityNotFoundRuntimeException.class)
    public void getWhenModelIsNotFound() throws Exception {
        service.get(Datastore.createKey(Blog.class, 1), 1L);
    }

    @Test(expected = ConcurrentModificationException.class)
    public void getWhenOptimisticLockFailed() throws Exception {
        Blog blog = new Blog();
        Datastore.put(blog);
        service.get(blog.getKey(), blog.getVersion() + 1);
    }

    @Test
    public void getAll() throws Exception {
        int count = Datastore.query(Blog.class).count();
        Blog blog = new Blog();
        Datastore.put(blog);
        assertThat(service.getAll().size(), is(count + 1));
    }

    @Test
    public void insert() throws Exception {
        Blog blog = new Blog();
        service.insert(blog);
        assertThat(blog.getKey(), is(notNullValue()));
    }

    @Test
    public void update() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        Datastore.put(blog);
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("title", "bbb");
        input.put("content", "222");
        Blog updated = service.update(blog.getKey(), blog.getVersion(), input);
        assertThat(updated.getTitle(), is("bbb"));
        assertThat(updated.getContent(), is("222"));
    }

    @Test
    public void delete() throws Exception {
        Blog blog = new Blog();
        Datastore.put(blog);
        service.delete(blog.getKey(), blog.getVersion());
    }

    @Test(expected = EntityNotFoundRuntimeException.class)
    public void deleteWhenModelIsNotFound() throws Exception {
        service.delete(Datastore.createKey(Blog.class, 1), 1L);
    }

    @Test(expected = ConcurrentModificationException.class)
    public void deleteWhenOptimisticLockFailed() throws Exception {
        Blog blog = new Blog();
        Datastore.put(blog);
        service.delete(blog.getKey(), blog.getVersion() + 1);
    }
}
