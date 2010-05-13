package slim3.demo.controller.blog;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import slim3.demo.model.Blog;

public class DeleteControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        int count = Datastore.query(Blog.class).count();
        Blog blog = new Blog();
        Datastore.put(blog);
        tester.param("key", Datastore.keyToString(blog.getKey()));
        tester.param("version", blog.getVersion());
        tester.start("/blog/delete");
        DeleteController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/blog/"));
        assertThat(Datastore.query(Blog.class).count(), is(count));
    }
}
