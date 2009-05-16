package slim3.it.controller.blog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.jdo.JDOTemplate;
import org.slim3.util.BeanUtil;

import slim3.it.model.Blog;

public class UpdateController extends Controller {

    private String title;

    private String content;

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Navigation execute() {
        final Blog blog = removeSessionAttribute("blog");
        if (blog == null) {
            throw new IllegalStateException("The blog instance is not found.");
        }
        BeanUtil.copy(this, blog);
        new JDOTemplate() {
            @Override
            public void doRun() {
                pm.makePersistent(blog);
            }
        }.run();
        return redirect("/blog/");
    }
}
