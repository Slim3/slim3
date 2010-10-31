package org.slim3.eclipse.core.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class NewGwtModuleWizard extends Wizard implements INewWizard {
	private NewGwtModuleWizardPage page1;
	IStructuredSelection selection;

	/**
	 * Constructor for NewS3ProjectWizard.
	 */
	public NewGwtModuleWizard() {
		super();
	}
	
	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		page1 = new NewGwtModuleWizardPage();
		page1.initModulePage(selection);
		addPage(page1);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		page1.createModule();
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();

		try {
			IEditorPart editorPart = IDE.openEditor(page, page1.getModuleFile());
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
		page1.dispose();
		page1 = null;
		return true;
	}
		
	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setNeedsProgressMonitor(true);
		this.selection = selection;
	}
}
