package org.slim3.eclipse.core.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.slim3.eclipse.core.JdtUtil;

public class NewEntryPointWizardPage extends NewTypeWizardPage {
	private IPackageFragmentRoot root;
	
	public NewEntryPointWizardPage() {
		this(true, "");
	}
	public NewEntryPointWizardPage(boolean isClass, String pageName) {
		super(isClass, pageName);
	}

	public void createEntryPoint(IProject projectHandle,
			String rootPackage, String typeName, 
			IProgressMonitor monitor) throws CoreException, InterruptedException {
	    List initSuperinterfaces = new ArrayList();
	    initSuperinterfaces.add("com.google.gwt.core.client.EntryPoint");
	    setSuperInterfaces(initSuperinterfaces, true);

    	IPackageFragmentRoot root = JdtUtil.getSrcPackageFragmentRoot(projectHandle);
    	setPackageFragmentRoot(root);
    	IPackageFragment pack = root.createPackageFragment(rootPackage + ".client", true, monitor);
	    setPackageFragment(pack, true);
	    
	    //setEnclosingType(enclosingType, true);
	    setEnclosingTypeSelection(false, true);

	    setTypeName(typeName, true);
	    //setSuperClass(initSuperclass, true);
	    
	    createType(monitor);
	}
	
	public void setPackageFragmentRoot(IPackageFragmentRoot root) {
		this.root = root;
	}
	
	@Override
	public IPackageFragmentRoot getPackageFragmentRoot() {
		return root;
	}
	
	protected void createTypeMembers(IType newType, 
			NewTypeWizardPage.ImportsManager imports, 
			IProgressMonitor monitor) throws CoreException {
	    boolean doConstr = false;
	    boolean doInherited = true;
	    createInheritedMethods(newType, doConstr, doInherited, imports, 
	      new SubProgressMonitor(monitor, 1));
	
	    if (monitor != null)
	      monitor.done();
	}

	public void createControl(Composite parent) {
	}
}
