package org.slim3.eclipse.core.wizards;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.wizards.NewContainerWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.osgi.framework.Bundle;
import org.slim3.eclipse.core.Activator;

@SuppressWarnings("restriction")
public class NewS3ProjectWizardPage extends  NewContainerWizardPage implements IOverwriteQuery {
	private static String DEFAULT_MODULE_NAME = "Main";
	private static String DEFAULT_ENTRY_POINT_NAME = "Main";
	
	public NewS3ProjectWizardPage() {
		this("Slim3 New Project");
	}
	
	public NewS3ProjectWizardPage(String name) {
		super(name);
	}


	private Text txtProjectName;
    private Text txtRootPackage;
	private Button cbIsSlim3;
	private Button cbIsScenic3;
	private Button cbIsGWT;
	private Button cbIsAutoGen;
	
    private Listener nameModifyListener = new Listener() {
        public void handleEvent(Event e) {
            boolean valid = validatePage();
            setPageComplete(valid);
        }
    };

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite localComposite = new Composite(composite, SWT.NULL);
		localComposite.setLayout(new GridLayout(2, false));
		localComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label lblProjectName = new Label(localComposite, SWT.NULL);
		lblProjectName.setText("Project name:");
		txtProjectName = new Text(localComposite, SWT.BORDER);
		txtProjectName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtProjectName.addListener(SWT.Modify, nameModifyListener);
		
		Label lblRootPackage = new Label(localComposite, SWT.NULL);
		lblRootPackage.setText("Root Package:");
		txtRootPackage = new Text(localComposite, SWT.BORDER);
		txtRootPackage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtRootPackage.addListener(SWT.Modify, nameModifyListener);

		Group uig = new Group(localComposite, SWT.NONE);
		uig.setLayout(new GridLayout(2, false));
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		uig.setLayoutData(gd);
		
		cbIsSlim3 = new Button(uig, SWT.RADIO);
		cbIsSlim3.setText("Use MVC of Slim3.");
		cbIsSlim3.setSelection(true);
		
		Link slim3Link = new Link(uig, SWT.NONE);
		slim3Link.setText("<a>http://sites.google.com/site/slim3appengine/</a>");
		slim3Link.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
	        public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
	        	IWorkbenchBrowserSupport browserSupport = 
	        		PlatformUI.getWorkbench().getBrowserSupport();
			    try {
			        IWebBrowser browser = browserSupport.getExternalBrowser();
			        browser.openURL(new URL("http://sites.google.com/site/slim3appengine/"));
			    } catch (PartInitException ex) {
			        ex.printStackTrace();
			    } catch (MalformedURLException ex) {
			        ex.printStackTrace();
			    }
	        }
	    });
		
		cbIsScenic3 = new Button(uig, SWT.RADIO);
		cbIsScenic3.setText("Use MVC of Scenic3.");

		Link link = new Link(uig, SWT.NONE);
		link.setText("You can create multiple action methods into a Page class. <a>http://code.google.com/p/scenic3/</a>");
        link.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
	        public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
	        	IWorkbenchBrowserSupport browserSupport = 
	        		PlatformUI.getWorkbench().getBrowserSupport();
			    try {
			        IWebBrowser browser = browserSupport.getExternalBrowser();
			        browser.openURL(new URL("http://code.google.com/p/scenic3/"));
			    } catch (PartInitException ex) {
			        ex.printStackTrace();
			    } catch (MalformedURLException ex) {
			        ex.printStackTrace();
			    }
	        }
	    });
		 
		cbIsGWT = new Button(uig, SWT.RADIO);
		cbIsGWT.setText("Use Google Web Toolkit.");
		
		cbIsGWT.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
		        Button bChk = (Button)e.widget;
		        if(bChk == cbIsGWT) {
		        	cbIsAutoGen.setEnabled(bChk.getSelection());
		        }
			}
		});

		cbIsAutoGen = new Button(uig, SWT.CHECK);
		cbIsAutoGen.setText("Generate a Module, an Entry Point and a Host Page.");
		cbIsAutoGen.setSelection(true);
		cbIsAutoGen.setEnabled(false);
		
		setControl(composite);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(visible) {
			setPageComplete(validatePage());
		}
	}
	
	protected boolean validatePage() {
        String projectName = txtProjectName.getText();
        if(! validateProjectName(projectName)) {
        	return false;
        }
                
        String rootPackage = txtRootPackage.getText();
        if(! validatePackageName(rootPackage)) {
        	return false;
        }
        
        return true;
	}

	private boolean validateProjectName(String name) {
		final IWorkspace workspace = JavaPlugin.getWorkspace();

		if (name.length() == 0) { 
			setErrorMessage(null);
			setMessage(NewWizardMessages.NewJavaProjectWizardPageOne_Message_enterProjectName); 
			return false;
		}

		final IStatus nameStatus = workspace.validateName(name, IResource.PROJECT);
		if (!nameStatus.isOK()) {
			setErrorMessage(nameStatus.getMessage());
			return false;
		}

		final IProject handle = getProjectHandle(name);
		if (handle.exists()) {
			setErrorMessage(NewWizardMessages.NewJavaProjectWizardPageOne_Message_projectAlreadyExists); 
			return false;
		}

		IPath projectLocation= ResourcesPlugin.getWorkspace().getRoot().getLocation().append(name);
		if (projectLocation.toFile().exists()) {
			try {
				String canonicalPath= projectLocation.toFile().getCanonicalPath();
				projectLocation= new Path(canonicalPath);
			} catch (IOException e) {
				JavaPlugin.log(e);
			}

			String existingName= projectLocation.lastSegment();
			if (!existingName.equals(name)) {
				setErrorMessage(Messages.format(NewWizardMessages.NewJavaProjectWizardPageOne_Message_invalidProjectNameForWorkspaceRoot, BasicElementLabels.getResourceName(existingName)));
				return false;
			}
		}

        setErrorMessage(null);
        setMessage(null);
		return true;
	}
	
	private boolean validatePackageName(String packName) {
	    if (packName.length() > 0) {
	        IStatus val = JavaConventions.validatePackageName(packName, 
	        					CompilerOptions.VERSION_1_3, CompilerOptions.VERSION_1_3);
	        if (val.getSeverity() == IStatus.ERROR) {
	        	setErrorMessage(Messages.format(NewWizardMessages.NewPackageWizardPage_error_InvalidPackageName, val.getMessage()));
	        	return false;
	        }
	        if (val.getSeverity() == IStatus.WARNING) {
	        	setMessage(Messages.format(NewWizardMessages.NewPackageWizardPage_warning_DiscouragedPackageName, val.getMessage()));
	        	return false;
	        }
	    } else {
	    	setMessage(NewWizardMessages.NewPackageWizardPage_error_EnterName);
	        return false;
	    }

	    setErrorMessage(null);
        setMessage(null);
	    return true;
	}
	  
    public boolean createProjects() {
    	final String projectName = txtProjectName.getText();
    	final String rootPackage = txtRootPackage.getText();
    	final boolean isGwtProject = cbIsGWT.getSelection();
    	final boolean isScenic3Project = cbIsScenic3.getSelection();
    	final boolean isAutoGen = cbIsAutoGen.getSelection();
    	
    	
		try {
			WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
				@Override
				protected void execute(IProgressMonitor monitor)
						throws CoreException, InvocationTargetException,
						InterruptedException {
					if(! isGwtProject) {
						createExistingProject(projectName, rootPackage, isScenic3Project, monitor);
					} else {
						createExistingGwtProject(projectName, rootPackage, isAutoGen, monitor);
					}
				}
			};
			
			getContainer().run(true, true, op);
		} catch (InvocationTargetException e) {
			JavaPlugin.log(e);
			return false;
		} catch (InterruptedException e) {
			JavaPlugin.log(e);
			return false;
		} finally {
		}

		BasicNewProjectResourceWizard.updatePerspective(null);
		return true;
	}

	protected void createExistingProject(String projectName, String rootPackage, 
			boolean isScenic3Project, IProgressMonitor monitor) throws InvocationTargetException {
		try {
			IProject projectHandle = createProjectHandle(projectName, monitor);
			importBlankProject(projectHandle, monitor);
			if(isScenic3Project) {
				importScenic3BlankProject(projectHandle, monitor);
			}
			changeFactoryPath(projectHandle, isScenic3Project, monitor);
			changeGenSrcDir(projectHandle, rootPackage, isScenic3Project, monitor);
			changeRootPackage(projectHandle, rootPackage, monitor);
			//copyAppEngineJarToWebInfLib(projectHandle, monitor);
			updateClassPath(projectHandle, false, isScenic3Project, monitor);
			if(isScenic3Project) {
				generateFrontPage(projectHandle, rootPackage, monitor);
				generateAppUrls(projectHandle, rootPackage, monitor);
			}
		} catch(Exception ex) {
			throw new InvocationTargetException(ex);
		}
	}

	protected void createExistingGwtProject(String projectName, String rootPackage, 
    		boolean isAutoGen, IProgressMonitor monitor) throws InvocationTargetException {
		try {
			IProject projectHandle = createProjectHandle(projectName, monitor);
			importBlankProject(projectHandle, monitor);
			importGwtBlankProject(projectHandle, monitor);
			changeFactoryPath(projectHandle, false, monitor);
			changeGenSrcDir(projectHandle, rootPackage, false, monitor);
			changeRootPackage(projectHandle, rootPackage, monitor);
			//copyAppEngineJarToWebInfLib(projectHandle, monitor);
			if(isAutoGen) {
				generateModule(projectHandle, rootPackage, monitor);			
				generateEntryPoint(projectHandle, rootPackage, monitor);
				generateHostPage(projectHandle, monitor);
			}
			updateClassPath(projectHandle, true, false, monitor);
		} catch(Exception ex) {
			throw new InvocationTargetException(ex);
		}
	}

	private IProject createProjectHandle(String projectName, IProgressMonitor monitor) throws CoreException {
		IProject projectHandle = getProjectHandle(projectName);
		projectHandle.create(monitor);
		projectHandle.open(monitor);
		return projectHandle;
	}
	
	private void importBlankProject(IProject projectHandle, IProgressMonitor monitor) throws Exception {
		importProject(projectHandle, "slim3-sdk", monitor);
	}

	private void importScenic3BlankProject(IProject projectHandle, IProgressMonitor monitor) throws Exception {
		importProject(projectHandle, "scenic3-sdk", monitor);
	}

	private void importGwtBlankProject(IProject projectHandle, IProgressMonitor monitor) throws Exception {
		importProject(projectHandle, "slim3-gwt-sdk", monitor);
	}

	private void importProject(IProject projectHandle, String sdkname, IProgressMonitor monitor) throws Exception {
		Bundle bundle = Activator.getDefault().getBundle();
		if(bundle == null) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "bundle not found"));
		}	
		
		URL installLocation = bundle.getEntry("/");
		URL local = FileLocator.toFileURL(installLocation);
		File root = new File(local.getPath(), sdkname);
		RelativityFileSystemStructureProvider structureProvider
			= new RelativityFileSystemStructureProvider(root);

		ImportOperation operation = 
			new ImportOperation(projectHandle.getFullPath(), 
								root,
								structureProvider, 
								this,
								structureProvider.getChildren(root));
		
		operation.setContext(getShell());
		operation.run(monitor);
	}
	
	private void changeFactoryPath(IProject projectHandle, boolean isScenic3Project, IProgressMonitor monitor) throws CoreException {
		IFile factoryPath = projectHandle.getFile(".factorypath");
		StringBuilder sb = new StringBuilder();
		sb.append("<factorypath>\r\n");
		sb.append("   <factorypathentry kind=\"WKSPJAR\" id=\""
				+ getGenJarFileName(projectHandle, monitor)
				+ "\" enabled=\"true\" runInBatchMode=\"false\"/>\r\n");
		if(isScenic3Project) {
			sb.append("   <factorypathentry kind=\"WKSPJAR\" id=\""
					+ getScenic3JarFileName(projectHandle, monitor)
					+ "\" enabled=\"true\" runInBatchMode=\"false\"/>\r\n");		}
		sb.append("</factorypath>\r\n");
		
		ByteArrayInputStream source = new ByteArrayInputStream(sb.toString().getBytes());
		if(factoryPath.exists()) {
			factoryPath.setContents(source, true, true, monitor);
		} else {
			factoryPath.create(source, true, monitor);
		}
		source = null;
	}

	private String getGenJarFileName(IProject projectHandle, IProgressMonitor monitor) throws CoreException {
		IFolder libFolder = projectHandle.getFolder("lib");
		IResource[] members = libFolder.members();
		for(IResource m : members) {
			if((m.getType() == IResource.FILE) && (m.getFileExtension().equals("jar"))) {
				String filename = m.getName();
				if(filename.startsWith("slim3-gen-")) {
					return m.getFullPath().toString();
				}
			}
		}
		
		throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Not found lib/slim3-gen-xxx.jar"));
	}
	
	private IPath getScenic3JarFilePath(IProject projectHandle, IProgressMonitor monitor) throws CoreException {
		IPath warlib = new Path("war/WEB-INF/lib");
		IFolder libFolder = projectHandle.getFolder(warlib);
		IResource[] members = libFolder.members();
		for(IResource m : members) {
			if((m.getType() == IResource.FILE) && (m.getFileExtension().equals("jar"))) {
				String filename = m.getName();
				if(filename.startsWith("scenic3-")) {
					return m.getFullPath();
				}
			}
		}		
		throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Not found war/WEB-INF/lib/scenic3-xxx.jar"));
	}
	
	private String getScenic3JarFileName(IProject projectHandle, IProgressMonitor monitor) throws CoreException {
		return getScenic3JarFilePath(projectHandle, monitor).toString();
	}
	
	private void changeGenSrcDir(IProject projectHandle, String rootPackageName, boolean isScenic3Project, IProgressMonitor monitor) throws CoreException {
		IFile aptCorePref = projectHandle.getFolder(".settings").getFile("org.eclipse.jdt.apt.core.prefs");
		if(aptCorePref.exists()) {
			InputStream is = aptCorePref.getContents();
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(ir);
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				while((line = br.readLine()) != null) {
					if(line.startsWith("org.eclipse.jdt.apt.aptEnabled=")) {
						sb.append("org.eclipse.jdt.apt.aptEnabled=true\r\n");
					} else if(line.startsWith("org.eclipse.jdt.apt.genSrcDir=")) {
						sb.append("org.eclipse.jdt.apt.genSrcDir=src\r\n");
					} else {
						sb.append(line);
						sb.append("\r\n");
					}
				}
				if(isScenic3Project) {
					sb.append("org.eclipse.jdt.apt.processorOptions/slim3.rootPackage=" + rootPackageName);
				}
				ByteArrayInputStream source = new ByteArrayInputStream(sb.toString().getBytes());
				aptCorePref.setContents(source, true, true, monitor);
			} catch (IOException e) {
				throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Cannot read .settings/org.eclipse.jdt.apt.core.prefs"));
			} finally {
				try {
					is.close();
					ir.close();
					br.close();
				} catch (IOException e) {
				}
				is = null;
				ir = null;
				br = null;
			}
		} else {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Not found .settings/org.eclipse.jdt.apt.core.prefs"));
		}
	}
		
	private void changeRootPackage(IProject projectHandle, String rootPackage, IProgressMonitor monitor) throws CoreException {
		IFile webConfig = projectHandle.getFolder("war").getFolder("WEB-INF").getFile("web.xml");
		if(webConfig.exists()) {
			InputStream is = webConfig.getContents();
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(ir);
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				while((line = br.readLine()) != null) {
					if(line.contains("slim3.rootPackage")) {
						sb.append(line);
						sb.append("\r\n");
						line = br.readLine();
						sb.append("        <param-value>");
						sb.append(rootPackage);
						sb.append("</param-value>\r\n");
					} else {
						sb.append(line);
						sb.append("\r\n");
					}
				}
				ByteArrayInputStream source = new ByteArrayInputStream(sb.toString().getBytes());
				webConfig.setContents(source, true, true, monitor);
			} catch (IOException e) {
				throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Cannot read .settings/org.eclipse.jdt.apt.core.prefs"));
			} finally {
				try {
					is.close();
					ir.close();
					br.close();
				} catch (IOException e) {
				}
				is = null;
				ir = null;
				br = null;
			}
		} else {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Not found war/WEB-INF/web.xml"));
		}
		createPackage(projectHandle, rootPackage, monitor);
	}

	private void createPackage(IProject projectHandle, String rootPackage, IProgressMonitor monitor) throws CoreException {
		IJavaProject project = JavaCore.create(projectHandle);
		IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();
		for(IPackageFragmentRoot root : roots) {
			if(root.getKind() == IPackageFragmentRoot.K_SOURCE) {
				root.createPackageFragment(rootPackage, true, monitor);
			}
		}
	}
	
//	private void copyAppEngineJarToWebInfLib(IProject projectHandle, IProgressMonitor monitor) throws CoreException {
//		
//		Path dest = new Path(projectHandle.getFullPath().toPortableString() + "/war/WEB-INF/lib");
//		IJavaProject project = JavaCore.create(projectHandle);
//		IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();
//		for(IPackageFragmentRoot root : roots) {
//			if(root.getElementName().startsWith("appengine-api")) {
//				copy(((JavaElement) root).getPath(), dest.append(root.getElementName()), monitor);
//			}
//		}
//	}
	
//	private void copy(IPath srcPath, IPath destPath, IProgressMonitor monitor)
//			throws CoreException {
//		File src = new File(srcPath.toOSString());
//	    IFile newFileHandle
//    		= IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getFile(destPath);
//	    try {
//	        CreateFileOperation op
//		    	= new CreateFileOperation(newFileHandle, null, 
//			        new FileInputStream(src), 
//			        IDEWorkbenchMessages.WizardNewFileCreationPage_title);
//	
//	        PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().execute(
//	            op, monitor, WorkspaceUndoUtil.getUIInfoAdapter(getShell()));
//	        
//	    } catch (Exception ex) {
//			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to create module", ex));
//	    }	    
//	}
	
	private void generateModule(IProject projectHandle, String rootPackage,
			IProgressMonitor monitor) throws CoreException {
		NewGwtModuleWizardPage page = new NewGwtModuleWizardPage();
		page.createModule(projectHandle, rootPackage, DEFAULT_MODULE_NAME, 
				DEFAULT_ENTRY_POINT_NAME, monitor);
		page.dispose();
		page = null;
	}
	
	private void generateEntryPoint(IProject projectHandle, String rootPackage,
			IProgressMonitor monitor) throws CoreException, InterruptedException {
		NewEntryPointWizardPage page = new NewEntryPointWizardPage();
		page.createEntryPoint(projectHandle, rootPackage + ".client", DEFAULT_ENTRY_POINT_NAME, monitor);
		page.dispose();
		page = null;
	}

	private void generateHostPage(IProject projectHandle, IProgressMonitor monitor) throws CoreException {
		NewHostPageWizardPage page = new NewHostPageWizardPage();
		page.createHostPage(projectHandle, DEFAULT_MODULE_NAME, monitor);
		page.dispose();
		page = null;
	}

	private void generateFrontPage(IProject projectHandle, String rootPackage, IProgressMonitor monitor)  throws CoreException {
		NewFrontPageWizardPage page = new NewFrontPageWizardPage();
		page.create(projectHandle, rootPackage, monitor);
		page.dispose();
		page = null;
	}

	private void generateAppUrls(IProject projectHandle,  String rootPackage, IProgressMonitor monitor) throws CoreException {
		NewAppUrlsWizardPage page = new NewAppUrlsWizardPage();
		page.create(projectHandle, rootPackage, monitor);
		page.dispose();
		page = null;
	}
	
	public IProject getProjectHandle(String prjname) {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(prjname);
    }

    public String queryOverwrite(String pathString) {
		return ALL;
	}

    private String GAE_CONTAINER_ID = "com.google.appengine.eclipse.core.GAE_CONTAINER";
    private String GWT_CONTAINER_ID = "com.google.gwt.eclipse.core.GWT_CONTAINER";
    
	private void updateClassPath(IProject projectHandle, boolean isGWT, boolean isScenic3, IProgressMonitor monitor) throws CoreException {
		IJavaProject project = JavaCore.create(projectHandle);
		IClasspathEntry[] classpaths = project.getRawClasspath();
		IClasspathEntry[] newClassPaths;
		if(isGWT || isScenic3) {
			newClassPaths = new IClasspathEntry[classpaths.length + 2];
		} else {
			newClassPaths = new IClasspathEntry[classpaths.length + 1];
		}
		System.arraycopy(classpaths, 0, newClassPaths, 0, classpaths.length);
		newClassPaths[classpaths.length] = JavaCore.newContainerEntry(new Path(GAE_CONTAINER_ID));
		if(isGWT) {
			newClassPaths[classpaths.length + 1] = JavaCore.newContainerEntry(new Path(GWT_CONTAINER_ID));
		}
		if(isScenic3) {
			newClassPaths[classpaths.length + 1] = JavaCore.newLibraryEntry(
					getScenic3JarFilePath(projectHandle, monitor), null, null);
		}
		project.setRawClasspath(newClassPaths, monitor);
	}
}