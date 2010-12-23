package org.slim3.eclipse.core.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;

import com.google.gwt.eclipse.core.util.Util;

@SuppressWarnings({"restriction", "unchecked", "rawtypes"})
public class NewFrontPageWizardPage extends AbstractNewSourceFileWizardPage {
	private static String FILE_NAME = "FrontPage.java";

	protected NewFrontPageWizardPage() {
		this("");
	}
	protected NewFrontPageWizardPage(String pageName) {
		super(pageName);
	}
	public void createControl(Composite parent) {
	}
	
	public void create(IProject projectHandle, String packageName, IProgressMonitor monitor) throws CoreException {
		create(projectHandle, packageName, FILE_NAME, monitor);
    }

	@Override
	protected InputStream getInitialContents(String packageName, String fileName) {
		List contents = new ArrayList();
		
		contents.add("package " + packageName + ";");
		contents.add("");
		contents.add("import org.slim3.controller.Navigation;");
		contents.add("");
		contents.add("import scenic3.ScenicPage;");
		contents.add("import scenic3.annotation.ActionPath;");
		contents.add("import scenic3.annotation.Default;");
		contents.add("import scenic3.annotation.Page;");
		contents.add("import scenic3.annotation.Var;");
		contents.add("");
		contents.add("@Page(\"/\")");
		contents.add("public class FrontPage extends ScenicPage {");
		contents.add("    // /view/100  /view/200");
		contents.add("    @ActionPath(\"view/{id}\")");
		contents.add("    public Navigation view(@Var(\"id\") String id) {");
		contents.add("        super.request.setAttribute(\"id\", id);");
		contents.add("        return forward(\"/view.jsp\");");
		contents.add("    }");
		contents.add("");
		contents.add("    // /");
		contents.add("    @Default");
		contents.add("    public Navigation index() {");
		contents.add("        return forward(\"/index.jsp\");");
		contents.add("    }");
		contents.add("}");
		
		String source = Util.join(contents, System.getProperty("line.separator"));
		ByteArrayInputStream stream = new ByteArrayInputStream(source.getBytes());
		return stream;
	}	
}
