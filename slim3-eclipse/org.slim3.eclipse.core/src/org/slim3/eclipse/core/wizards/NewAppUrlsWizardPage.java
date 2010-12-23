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
public class NewAppUrlsWizardPage extends AbstractNewSourceFileWizardPage {
	private static String FILE_NAME = "AppUrls.java";
	
	protected NewAppUrlsWizardPage() {
		this("");
	}
	protected NewAppUrlsWizardPage(String pageName) {
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
		contents.add("import scenic3.UrlsImpl;");
		contents.add("import " + packageName + ".matcher.FrontPageMatcher;");
		contents.add("");
		contents.add("public class AppUrls extends UrlsImpl {");
		contents.add("");
		contents.add("    public AppUrls() {");
		contents.add("        excludes(\"/css/*\");");
		contents.add("        add(FrontPageMatcher.get());");
		contents.add("        // TODO Add your own new PageMatcher");
		contents.add("");
		contents.add("    }");
		contents.add("}");
		
		String source = Util.join(contents, System.getProperty("line.separator"));
		ByteArrayInputStream stream = new ByteArrayInputStream(source.getBytes());
		return stream;
	}	
}
