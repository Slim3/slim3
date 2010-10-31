package org.slim3.eclipse.core.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.CreateFileOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.slim3.eclipse.core.Activator;

@SuppressWarnings("restriction")
public class NewHostPageWizardPage extends WizardPage {
	protected NewHostPageWizardPage() {
		this("");
	}
	protected NewHostPageWizardPage(String pageName) {
		super(pageName);
	}

	public void createHostPage(IProject projectHandle, String moduleName, IProgressMonitor monitor) throws CoreException {
	    IPath newFilePath = getHostPageFilePath(projectHandle);
	    IFile newFileHandle
	    	= IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getFile(newFilePath);
	    InputStream initialContents = getHostPageInitialContents(moduleName);

        CreateFileOperation op
        	= new CreateFileOperation(newFileHandle, null, 
		        initialContents, 
		        IDEWorkbenchMessages.WizardNewFileCreationPage_title);
        try {
	          PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().execute(
	            op, monitor, WorkspaceUndoUtil.getUIInfoAdapter(getShell()));
        } catch (Exception ex) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to create module", ex));
        }
    }

	private IPath getHostPageFilePath(IProject projectHandle) {
    	IPath rootPath = projectHandle.getFullPath();
    	rootPath = rootPath.append("war/");
    	IPath newFilePath = rootPath.append("index.html");
    	return newFilePath;
	}

	private InputStream getHostPageInitialContents(String moduleName) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!doctype html>\n");
        sb.append("<html>\n");
        sb.append("  <head>\n");
        sb.append("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n");

        sb.append("    <title>" + moduleName + "</title>\n");

        String startupScriptPath = moduleName.toLowerCase() + "/" + moduleName.toLowerCase() + ".nocache.js";
        sb.append("    <script type=\"text/javascript\" language=\"javascript\" src=\"" + 
            startupScriptPath + "\"></script>\n");

        sb.append("  </head>\n");
        sb.append("\n");
        sb.append("  <body>\n");
        sb.append("    <iframe src=\"javascript:''\" id=\"__gwt_historyFrame\" tabIndex='-1' style=\"position:absolute;width:0;height:0;border:0\"></iframe>\n");
        sb.append("\n");
        sb.append("  </body>\n");
        sb.append("</html>");

        String html = sb.toString();
        ByteArrayInputStream stream = new ByteArrayInputStream(html.getBytes());
        return stream;
	}

	public void createControl(Composite parent) {
	}
}
