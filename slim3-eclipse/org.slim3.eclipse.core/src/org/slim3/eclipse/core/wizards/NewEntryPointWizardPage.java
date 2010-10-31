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

import com.google.gwt.eclipse.core.util.Util;

@SuppressWarnings({ "unchecked", "restriction" })
public class NewEntryPointWizardPage extends NewContainerWizardPage {	
	public NewEntryPointWizardPage() {
		this(true, "EntryPoint");
	}
	public NewEntryPointWizardPage(boolean isClass, String pageName) {
		super(pageName);
	}

	protected IPath getTypePath(IProject projectHandle, String packageName,
			String typeName) {
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

		IPath moduleFilePath = rootPath.append(typeName);

		moduleFilePath = moduleFilePath.addFileExtension("java");

		return moduleFilePath;
	}

	public void createEntryPoint(IProject projectHandle, String packName, String typeName, 
			IProgressMonitor monitor) throws CoreException, InterruptedException {
		try {
			IPath newFilePath = getTypePath(projectHandle, packName, typeName);
			IFile newFileHandle = IDEWorkbenchPlugin.getPluginWorkspace()
					.getRoot().getFile(newFilePath);
			InputStream initialContents = getEntryPointInitialContents(projectHandle, packName, typeName);

			CreateFileOperation op = new CreateFileOperation(newFileHandle,
					null, initialContents,
					IDEWorkbenchMessages.WizardNewFileCreationPage_title);
			PlatformUI.getWorkbench().getOperationSupport()
					.getOperationHistory().execute(op, monitor,
							WorkspaceUndoUtil.getUIInfoAdapter(getShell()));
			initialContents.close();
			initialContents = null;
		} catch (Exception ex) {
			throw new CoreException(new Status(IStatus.ERROR,
					Activator.PLUGIN_ID, "Failed to create module", ex));
		}
	}
	
	private InputStream getEntryPointInitialContents(IProject projectHandle, String packName, String typeName) {
		List contents = new ArrayList();
		
		contents.add("package " + packName + ";");
		contents.add("");
		contents.add("import com.google.gwt.core.client.EntryPoint;");
		contents.add("");
		contents.add("public class Main implements EntryPoint {");
		contents.add("");
		contents.add("    public void onModuleLoad() {");
		contents.add("        // TODO Auto-generated method stub");
		contents.add("");
		contents.add("    }");
		contents.add("}");
		
		String source = Util.join(contents, System.getProperty("line.separator"));
		ByteArrayInputStream stream = new ByteArrayInputStream(source.getBytes());
		return stream;
	}

	public void createControl(Composite parent) {
	}
}
