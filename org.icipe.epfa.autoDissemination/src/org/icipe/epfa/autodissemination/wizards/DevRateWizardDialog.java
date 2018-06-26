package org.icipe.epfa.autodissemination.wizards;

import org.icipe.epfa.autodissemination.AutoDissemination;
import org.eclipse.jface.wizard.Wizard;

public class DevRateWizardDialog extends Wizard {

	public DevRateWizardDialog() {
		setWindowTitle("EPFA Motality");
	}

	@Override
	public void addPages() {
		addPage(new MainPageWizardPage());
		addPage(new SeveralModelsWizardPage());
		addPage(new OneModelWizardPage1());
		addPage(new OneModelWizardPage2());
		addPage(new SelectedModelWizardPage());
	}

	@Override
	public boolean performFinish() {
		if(AutoDissemination.saveModelSelected())
			return true;
		else
			return false;
	}

	@Override
	public boolean canFinish() {
		return true;
	}
}
