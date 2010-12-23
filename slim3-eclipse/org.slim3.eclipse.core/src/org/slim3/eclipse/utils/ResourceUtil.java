package org.slim3.eclipse.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.slim3.eclipse.core.Activator;

public class ResourceUtil {
	public static void openFile(IPath path) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();

		try {
			IDE.openEditor(page, pathToFile(path));
		} catch (PartInitException e) {
			e.printStackTrace();
		}		
	}
	
	public static IFile pathToFile(IPath path) {
		return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
	}
	
	public static IPath getFilePath(IProject projectHandle, IPath basePath, String fnRex, IProgressMonitor monitor) throws CoreException {
        Pattern pattern = Pattern.compile(fnRex);
		
		IFolder folder = projectHandle.getFolder(basePath);
		IResource[] members = folder.members();
		for(IResource m : members) {
			if(m.getType() == IResource.FILE) {
				String filename = m.getName();
				Matcher matcher = pattern.matcher(filename);
				if(matcher.matches()) {
					return m.getFullPath();
				}
			}
		}		
		throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Not found " + basePath.toString() + fnRex));
	}	
}
