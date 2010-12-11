package org.slim3.eclipse.core.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.ui.wizards.NewContainerWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.CreateFileOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.slim3.eclipse.core.Activator;
import org.slim3.eclipse.core.JdtUtil;

@SuppressWarnings({"restriction"})
public abstract class AbstractNewSourceFileWizardPage extends NewContainerWizardPage {
	protected AbstractNewSourceFileWizardPage() {
		this("");
	}
	protected AbstractNewSourceFileWizardPage(String pageName) {
		super(pageName);
	}
	
	public void create(IProject projectHandle, String packageName, String fileName, IProgressMonitor monitor) throws CoreException {
	    IPath newFilePath = getFilePath(projectHandle, packageName, fileName);
	    IFile newFileHandle
	    	= IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getFile(newFilePath);
	    InputStream initialContents = getInitialContents(packageName, fileName);

        CreateFileOperation op
        	= new CreateFileOperation(newFileHandle, null, 
		        initialContents, 
		        IDEWorkbenchMessages.WizardNewFileCreationPage_title);
        try {
	          PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().execute(
	            op, monitor, WorkspaceUndoUtil.getUIInfoAdapter(getShell()));
        } catch (Exception ex) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to create " + packageName + "." + fileName, ex));
        }
    }

	protected IPath getFilePath(IProject projectHandle, String packageName, String fileName) {
		IPackageFragmentRoot root = getPackageFragmentRoot();
		if (root == null && projectHandle != null) {
			try {
				root = JdtUtil.getSrcPackageFragmentRoot(projectHandle);
			} catch (CoreException e) {
			}
		}
		if (root == null) {
			return null;
		}

		IPath rootPath = root.getPath();

		if ((packageName != null) && (packageName.length() > 0)) {
			rootPath = rootPath.append(packageName.replace('.', '/'));
		}

		return rootPath.append(fileName);
	}

	protected abstract InputStream getInitialContents(String packageName, String fileName);
}
