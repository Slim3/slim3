package org.slim3.eclipse.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

public class JdtUtil {
	public static IProject getProjectHandle(String prjname) {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(prjname);
    }
	
	public static IJavaProject getJavaProject(String prjname) {
		return getJavaProject(getProjectHandle(prjname));
	}
	
	public static IJavaProject getJavaProject(IProject projectHandle) {
		return JavaCore.create(projectHandle);
	}
	
	public static IPackageFragmentRoot getSrcPackageFragmentRoot(String prjname) throws CoreException {
		return getSrcPackageFragmentRoot(getJavaProject(prjname));
	}
	
	public static IPackageFragmentRoot getSrcPackageFragmentRoot(IProject projectHandle) throws CoreException {
		return getSrcPackageFragmentRoot(getJavaProject(projectHandle));
	}
	
	public static IPackageFragmentRoot getSrcPackageFragmentRoot(IJavaProject project) throws CoreException {
		IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();
		for(IPackageFragmentRoot root : roots) {
			if(root.getKind() == IPackageFragmentRoot.K_SOURCE) {
				return root;
			}
		}
		return null;
	}
	
}
