package org.slim3.eclipse.core.wizards;

import com.google.gdt.eclipse.core.JavaProjectUtilities;
import com.google.gwt.eclipse.core.modules.IModule;
import com.google.gwt.eclipse.core.modules.ModuleUtils;
import com.google.gwt.eclipse.core.nature.GWTNature;
import com.google.gwt.eclipse.core.runtime.GWTRuntime;
import com.google.gwt.eclipse.core.util.Util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.TextFieldNavigationHandler;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.ControlContentAssistHelper;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaPackageCompletionProcessor;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonStatusDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.wizards.NewContainerWizardPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.ide.undo.CreateFileOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.slim3.eclipse.core.Activator;
import org.slim3.eclipse.core.JdtUtil;

@SuppressWarnings( { "unchecked", "restriction" })
public class NewGwtModuleWizardPage extends NewContainerWizardPage {
	private static int ADD_INHERITS_BUTTON_GROUP_INDEX = 0;
	private static int CLIENT_PACKAGE_CHECKBOX_GROUP_INDEX = 1;
	private static int PUBLIC_PATH_CHECKBOX_GROUP_INDEX = 0;
	private static int REMOVE_INHERITS_BUTTON_GROUP_INDEX = 2;
	protected IStatus moduleContainerStatus;
	protected IStatus moduleNameStatus;
	protected IStatus modulePackageStatus;
	private SelectionButtonDialogFieldGroup moduleCreateElementsCheckboxes;
	private ListDialogField moduleInheritsDialogField;
	private StringDialogField moduleNameField;
	private JavaPackageCompletionProcessor modulePackageCompletionProcessor;
	private StringButtonStatusDialogField modulePackageField;

	public NewGwtModuleWizardPage() {
		super("newModuleWizardPage");
		setTitle("New GWT Module");
		setDescription("Create a new GWT Module.");

		ModuleDialogFieldAdapter adapter = new ModuleDialogFieldAdapter();

		this.modulePackageField = new StringButtonStatusDialogField(adapter);
		this.modulePackageField.setDialogFieldListener(adapter);
		this.modulePackageField.setLabelText("Package:");
		this.modulePackageField
				.setButtonLabel(NewWizardMessages.NewTypeWizardPage_package_button);
		this.modulePackageField
				.setStatusWidthHint(NewWizardMessages.NewTypeWizardPage_default);

		this.modulePackageCompletionProcessor = new JavaPackageCompletionProcessor();

		this.moduleNameField = new StringDialogField();
		this.moduleNameField.setDialogFieldListener(adapter);
		this.moduleNameField.setLabelText("Module name:");

		String[] addButtons = {
				NewWizardMessages.NewTypeWizardPage_interfaces_add, null,
				NewWizardMessages.NewTypeWizardPage_interfaces_remove };
		this.moduleInheritsDialogField = new ListDialogField(adapter,
				addButtons, new ModuleSelectionLabelProvider());
		this.moduleInheritsDialogField.setDialogFieldListener(adapter);
		this.moduleInheritsDialogField
				.setTableColumns(new ListDialogField.ColumnsDescription(1,
						false));
		this.moduleInheritsDialogField.setLabelText("Inherited modules:");
		this.moduleInheritsDialogField
				.setRemoveButtonIndex(REMOVE_INHERITS_BUTTON_GROUP_INDEX);

		String[] buttonNames = { "Create public resource path",
				"Create package for client source" };
		this.moduleCreateElementsCheckboxes = new SelectionButtonDialogFieldGroup(
				32, buttonNames, 1);

		this.moduleContainerStatus = new StatusInfo();
		this.modulePackageStatus = new StatusInfo();
		this.moduleNameStatus = new StatusInfo();
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, 0);
		composite.setFont(parent.getFont());
		int nColumns = 4;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createContainerControls(composite, nColumns);
		createPackageControls(composite, nColumns);
		createModuleNameControls(composite, nColumns);
		createModuleInheritsControls(composite, nColumns);

		setControl(composite);
	}

	public List<IModule> getModuleInherits() {
		return new ArrayList(this.moduleInheritsDialogField.getElements());
	}

	public String getModuleName() {
		return this.moduleNameField.getText();
	}

	public String getModulePackageName() {
		return this.modulePackageField.getText();
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible)
			setFocus();
	}

	public boolean shouldCreateClientPackage() {
		return true;
	}

	public boolean shouldCreatePublicPath() {
		return false;
	}

	protected void createModuleInheritsControls(Composite composite,
			int nColumns) {
		this.moduleInheritsDialogField.doFillIntoGrid(composite, nColumns);
	}

	protected void createModuleNameControls(Composite composite, int nColumns) {
		this.moduleNameField.doFillIntoGrid(composite, nColumns - 1);
		DialogField.createEmptySpace(composite);

		Text moduleNameText = this.moduleNameField.getTextControl(null);
		LayoutUtil.setWidthHint(moduleNameText, getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(moduleNameText);
	}

	protected void createPackageControls(Composite composite, int nColumns) {
		this.modulePackageField.doFillIntoGrid(composite, nColumns);
		Text modulePackageText = this.modulePackageField.getTextControl(null);
		LayoutUtil.setWidthHint(modulePackageText, getMaxFieldWidth());
		ControlContentAssistHelper.createTextContentAssistant(
				modulePackageText, this.modulePackageCompletionProcessor);
		TextFieldNavigationHandler.install(modulePackageText);
	}

	protected void doFieldChange(String fieldName, DialogField field) {
		if ("NewContainerWizardPage.container".equals(fieldName)) {
			this.moduleContainerStatus = moduleContainerChanged();
			this.modulePackageStatus = packageChanged();
			this.moduleNameStatus = nameChanged();
		} else if (field == this.modulePackageField) {
			this.modulePackageStatus = packageChanged();
			this.moduleNameStatus = nameChanged();
		} else if (field == this.moduleNameField) {
			this.moduleNameStatus = nameChanged();
		}

		doStatusUpdate();
	}

	protected IPath getModulePath(IProject projectHandle, String packageName,
			String moduleName) {
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

		IPath moduleFilePath = rootPath.append(moduleName);

		moduleFilePath = moduleFilePath.addFileExtension("gwt.xml");

		return moduleFilePath;
	}

	protected IPath getModulePath() {
		return getModulePath(null, getModulePackageName(), getModuleName());
	}

	protected IFile getModuleFile() {
		return ResourcesPlugin.getWorkspace().getRoot()
				.getFile(getModulePath());
	}

	protected void handleFieldChanged(String fieldName) {
		doFieldChange(fieldName, null);
	}

	protected void initModulePage(IStructuredSelection selection) {
		IJavaElement jelem = getInitialJavaElement(selection);

		initContainerPage(jelem);

		String pName = "";
		if (jelem != null) {
			IPackageFragment pf = (IPackageFragment) jelem.getAncestor(4);
			if (pf != null) {
				pName = pf.getElementName();
			}
		}

		this.modulePackageField.setText(pName);

		this.modulePackageCompletionProcessor
				.setPackageFragmentRoot(getPackageFragmentRoot());

		this.moduleCreateElementsCheckboxes.setSelection(
				CLIENT_PACKAGE_CHECKBOX_GROUP_INDEX, true);
		this.moduleCreateElementsCheckboxes.setSelection(
				PUBLIC_PATH_CHECKBOX_GROUP_INDEX, true);

		doFieldChange("NewContainerWizardPage.container", null);
	}

	private void addCommonGWTModulesAsDefaultInherits() {
		IJavaProject javaProject = getJavaProject();

		assert ((JavaProjectUtilities
				.isJavaProjectNonNullAndExists(javaProject)) && (GWTNature
				.isGWTProject(javaProject.getProject())));

		if (javaProject.getResource().isAccessible()) {
			IModule gwtUserModule = ModuleUtils.findModule(javaProject,
					"com.google.gwt.user.User", true);
			if (gwtUserModule != null)
				addModuleIfNotAlreadyInList(gwtUserModule);
		}
	}

	private void addModuleIfNotAlreadyInList(IModule module) {
		for (Iterator localIterator = this.moduleInheritsDialogField
				.getElements().iterator(); localIterator.hasNext();) {
			Object elem = localIterator.next();
			IModule curModule = (IModule) elem;
			if (curModule.getQualifiedName().equals(module.getQualifiedName())) {
				return;
			}
		}

		this.moduleInheritsDialogField.addElement(module);
	}

	private IPackageFragment choosePackage() {
		IPackageFragmentRoot root = getPackageFragmentRoot();
		IJavaElement[] packages = (IJavaElement[]) null;
		try {
			if ((root != null) && (root.exists()))
				packages = root.getChildren();
		} catch (JavaModelException e) {
			JavaPlugin.log(e);
		}
		if (packages == null) {
			packages = new IJavaElement[0];
		}

		ElementListSelectionDialog dialog = new ElementListSelectionDialog(
				getShell(), new JavaElementLabelProvider(
						JavaElementLabelProvider.SHOW_DEFAULT));
		dialog.setIgnoreCase(false);
		dialog
				.setTitle(NewWizardMessages.NewTypeWizardPage_ChoosePackageDialog_title);
		dialog
				.setMessage(NewWizardMessages.NewTypeWizardPage_ChoosePackageDialog_description);
		dialog
				.setEmptyListMessage(NewWizardMessages.NewTypeWizardPage_ChoosePackageDialog_empty);
		dialog.setElements(packages);
		dialog.setHelpAvailable(false);

		if (dialog.open() == 0) {
			return (IPackageFragment) dialog.getFirstResult();
		}
		return null;
	}

	private void doStatusUpdate() {
		IStatus[] status = { this.moduleContainerStatus,
				this.modulePackageStatus, this.moduleNameStatus };

		updateStatus(status);
	}

	private void moduleAddInheritsButtonPressed() {
		// IModule module = ModuleSelectionDialog.show(getShell(),
		// getPackageFragmentRoot().getJavaProject(), true);
		//
		// if (module == null) {
		// return;
		// }
		//
		// addModuleIfNotAlreadyInList(module);
	}

	private IStatus moduleContainerChanged() {
		if ((getPackageFragmentRoot() == null)
				|| (!getPackageFragmentRoot().exists())
				|| (!JavaProjectUtilities
						.isJavaProjectNonNullAndExists(getJavaProject()))
				|| (!GWTNature.isGWTProject(getJavaProject().getProject()))) {
			this.modulePackageField.enableButton(false);

			this.moduleInheritsDialogField.enableButton(
					ADD_INHERITS_BUTTON_GROUP_INDEX, false);
		} else {
			this.modulePackageField.enableButton(true);

			this.moduleInheritsDialogField.enableButton(
					ADD_INHERITS_BUTTON_GROUP_INDEX, true);
		}

		this.modulePackageCompletionProcessor
				.setPackageFragmentRoot(getPackageFragmentRoot());

		if (this.fContainerStatus.getSeverity() == 4) {
			return this.fContainerStatus;
		}

		if (this.fContainerStatus.getSeverity() == 2) {
			return Util.newErrorStatus(this.fContainerStatus.getMessage());
		}

		if ((JavaProjectUtilities
				.isJavaProjectNonNullAndExists(getJavaProject()))
				&& (!GWTNature.isGWTProject(getJavaProject().getProject()))) {
			return Util
					.newErrorStatus("The source folder is not part of a GWT Project.");
		}

		if (this.moduleInheritsDialogField.getElements().isEmpty()) {
			addCommonGWTModulesAsDefaultInherits();
		}

		return this.fContainerStatus;
	}

	private void moduleDialogFieldChanged(DialogField field) {
		doFieldChange(null, field);
	}

	private void modulePackageBrowseButtonPressed() {
		IPackageFragment result = choosePackage();
		if (result != null) {
			this.modulePackageField.setText(result.getElementName());
		}
		doFieldChange(null, this.modulePackageField);
	}

	private void moduleRemoveInheritsButtonPressed() {
		this.moduleInheritsDialogField
				.removeElements(this.moduleInheritsDialogField
						.getSelectedElements());
	}

	private IStatus nameChanged() {
		return ModuleUtils.validateSimpleModuleName(this.moduleNameField
				.getText());
	}

	private IStatus packageChanged() {
		String packName = this.modulePackageField.getText();
		IStatus validatePackageStatus = Util.validatePackageName(packName);

		if (validatePackageStatus.getSeverity() == 4) {
			return validatePackageStatus;
		}

		if (packName.length() == 0)
			this.modulePackageField
					.setStatus(NewWizardMessages.NewTypeWizardPage_default);
		else {
			this.modulePackageField.setStatus("");
		}

		IJavaProject project = getJavaProject();
		IPackageFragmentRoot root = getPackageFragmentRoot();

		if ((project != null) && (root != null) && (project.exists())
				&& (packName.length() > 0)) {
			try {
				IPath rootPath = root.getPath();
				IPath outputPath = project.getOutputLocation();
				if ((rootPath.isPrefixOf(outputPath))
						&& (!rootPath.equals(outputPath))) {
					IPath packagePath = rootPath.append(packName.replace('.',
							'/'));
					if (outputPath.isPrefixOf(packagePath)) {
						return Util
								.newErrorStatus(NewWizardMessages.NewTypeWizardPage_error_ClashOutputLocation);
					}
				}
			} catch (JavaModelException e) {
				JavaPlugin.log(e);
			}

		}

		return validatePackageStatus;
	}

	private void setFocus() {
		if (this.moduleNameField.isEnabled())
			this.moduleNameField.setFocus();
		else
			setFocusOnContainer();
	}

	private class ModuleDialogFieldAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {
		private ModuleDialogFieldAdapter() {
		}

		public void changeControlPressed(DialogField field) {
			if (field == NewGwtModuleWizardPage.this.modulePackageField)
				NewGwtModuleWizardPage.this.modulePackageBrowseButtonPressed();
		}

		public void customButtonPressed(ListDialogField field, int index) {
			if (index == NewGwtModuleWizardPage.ADD_INHERITS_BUTTON_GROUP_INDEX)
				NewGwtModuleWizardPage.this.moduleAddInheritsButtonPressed();
			else
				NewGwtModuleWizardPage.this.moduleRemoveInheritsButtonPressed();
		}

		public void dialogFieldChanged(DialogField field) {
			NewGwtModuleWizardPage.this.moduleDialogFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}

		public void selectionChanged(ListDialogField field) {
		}
	}

	public static class ModuleSelectionLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			assert ((element instanceof IModule));
			// return GWTPlugin.getDefault().getImage(GWTImages.MODULE_ICON);
			return null;
		}

		public String getText(Object element) {
			assert ((element instanceof IModule));
			return ((IModule) element).getQualifiedName();
		}
	}

	public boolean createModule() {
		try {
			WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
				@Override
				protected void execute(IProgressMonitor monitor)
						throws CoreException, InvocationTargetException,
						InterruptedException {
					createModule(monitor);
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

	private void createModule(IProgressMonitor monitor) throws CoreException {
		createModule(null, getModulePackageName(), getModuleName(), null, monitor);
	}

	public void createModule(IProject projectHandle, String packName,
			String moduleName, String entryPoint, IProgressMonitor monitor) throws CoreException {
		try {
			if(entryPoint != null) {
				entryPoint = packName + ".client." + entryPoint;
			}
			
			IPath newFilePath = getModulePath(projectHandle, packName, moduleName);
			IFile newFileHandle = IDEWorkbenchPlugin.getPluginWorkspace()
					.getRoot().getFile(newFilePath);
			InputStream initialContents = getModuleInitialContents(projectHandle, moduleName, entryPoint);

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

	protected InputStream getModuleInitialContents(IProject projectHandle, String moduleName, String entryPoint) {
		IJavaProject javaProject = null;
		if(projectHandle == null) {
			javaProject = getJavaProject();
		} else {
			javaProject = JdtUtil.getJavaProject(projectHandle);
		}
		List contents = new ArrayList();

		contents.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		String gwtModuleDtd = "<!-- Could not determine the version of your GWT SDK; using the module DTD from GWT 1.6.4. You may want to change this. -->\n<!DOCTYPE module PUBLIC \"-//Google Inc.//DTD Google Web Toolkit 1.6.4//EN\" \"http://google-web-toolkit.googlecode.com/svn/tags/1.6.4/distro-source/core/src/gwt-module.dtd\">";
		try {
			GWTRuntime runtime = GWTRuntime.findSdkFor(javaProject);
			if (runtime != null) {
				String versionNum = runtime.getVersion();

				if ((!versionNum.endsWith(".999"))
						&& (!versionNum.startsWith("0.0")))
					gwtModuleDtd = "<!DOCTYPE module PUBLIC \"-//Google Inc.//DTD Google Web Toolkit "
							+ versionNum
							+ "//EN\" \"http://google-web-toolkit.googlecode.com/svn/tags/"
							+ versionNum
							+ "/distro-source/core/src/gwt-module.dtd\">";
			}
		} catch (JavaModelException e) {
		}

		contents.add(gwtModuleDtd);

		contents.add("<module rename-to='" + moduleName.toLowerCase() + "'>");

		contents.add("\t<inherits name=\"com.google.gwt.user.User\" />");
		contents.add("\t<inherits name=\"org.slim3.gwt.emul.S3Emulation\" />");

		if(entryPoint != null) {
			contents.add("\t<entry-point class=\""+ entryPoint + "\"/>");	
		}

		contents.add("\t<source path=\"client\"/>");
		contents.add("\t<source path=\"shared\"/>");

		contents.add("</module>");

		String xml = Util.join(contents, System.getProperty("line.separator"));
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
		return stream;
	}
}
