package org.slim3.eclipse.core.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

public class NewS3ProjectWizard extends Wizard implements INewWizard {
	private NewS3ProjectWizardPage page1;

	/**
	 * Constructor for NewS3ProjectWizard.
	 */
	public NewS3ProjectWizard() {
		super();
	}
	
	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		page1 = new NewS3ProjectWizardPage();
		addPage(page1);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		page1.createProjects();
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
	}
}