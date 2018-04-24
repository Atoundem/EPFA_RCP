package org.icipe.epfa.project.wizards;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.wb.swt.SWTResourceManager;

public class RegistrationPage extends WizardPage {
	
	public static Text txtTextprojetname;
	public static Text textSpeciesName;
	public static Text textAuthor;
	public static Text textDate;
	public static Text textObservation;
//	public static Text textProjectLocation;	
	public static Text textPathworkspace;
	String strFont = "Comic Sans MS";
	
	
	
	protected RegistrationPage() {
		
		super("RegistrationPage");
//		WizardPage.setTitle("TTTTT");
//		WizardDialog.setWindowTitle("sdf");
	    setTitle("EPFA project creation");
//	    setTitle("dddddddddd");
	    setDescription("Fill the fields to create the project");
	    setPageComplete(true);
	}
	
	

	@Override
	public void createControl(Composite parent) {
		Composite epfaProjet = new Composite(parent, SWT.None);
		epfaProjet.setLayout(new FormLayout());
		

		final Group grpProjectInfo = new Group(epfaProjet, SWT.NONE);
		grpProjectInfo.setLayout(new FormLayout());
		final FormData formData_1 = new FormData();
		formData_1.bottom = new FormAttachment(0, 213);
		formData_1.top = new FormAttachment(0, 0);
		formData_1.right = new FormAttachment(0, 550);
		formData_1.left = new FormAttachment(0, 0);
		grpProjectInfo.setLayoutData(formData_1);
		grpProjectInfo.setFont(SWTResourceManager.getFont(strFont, 10, SWT.NONE));
		grpProjectInfo.setText("Project Info");
		
		final Label projectNameLabel = new Label(grpProjectInfo, SWT.NONE);
		projectNameLabel.setFont(SWTResourceManager.getFont(strFont, 10, SWT.NONE));
		final FormData formData = new FormData();
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 5);
		projectNameLabel.setLayoutData(formData);
		projectNameLabel.setText("Project Name :");

		txtTextprojetname = new Text(grpProjectInfo, SWT.BORDER);
		final FormData formData_7 = new FormData();
		formData_7.left = new FormAttachment(0, 120);
		formData_7.top = new FormAttachment(projectNameLabel, 0, SWT.TOP);
		formData_7.right = new FormAttachment(projectNameLabel, 393, SWT.RIGHT);
		txtTextprojetname.setLayoutData(formData_7);
		
		final Label speciesNameLabel = new Label(grpProjectInfo, SWT.NONE);
		speciesNameLabel.setFont(SWTResourceManager.getFont(strFont, 10, SWT.NONE));
		final FormData formData_8 = new FormData();
		formData_8.top = new FormAttachment(projectNameLabel, 10);
		formData_8.left = new FormAttachment(0, 5);
		speciesNameLabel.setLayoutData(formData_8);
		speciesNameLabel.setText("Species(Fungi):");
		
		textSpeciesName = new Text(grpProjectInfo, SWT.BORDER);
		final FormData formData_9 = new FormData();
		formData_9.left = new FormAttachment(txtTextprojetname,0, SWT.LEFT);
		formData_9.top = new FormAttachment(speciesNameLabel, 0, SWT.TOP);
		formData_9.right = new FormAttachment(speciesNameLabel, 395, SWT.RIGHT);		
		textSpeciesName.setLayoutData(formData_9);
		
		final Label authorLabel = new Label(grpProjectInfo, SWT.NONE);
		authorLabel.setFont(SWTResourceManager.getFont(strFont, 10, SWT.NONE));
		final FormData formData_12 = new FormData();
		formData_12.top = new FormAttachment(speciesNameLabel, 5, SWT.BOTTOM);
		formData_12.left = new FormAttachment(speciesNameLabel, 0, SWT.LEFT);
		authorLabel.setLayoutData(formData_12);
		authorLabel.setText("Author :");
		
		textAuthor = new Text(grpProjectInfo, SWT.BORDER);
		final FormData formData_13 = new FormData();
		formData_13.left = new FormAttachment(textSpeciesName,0, SWT.LEFT);
		formData_13.top = new FormAttachment(authorLabel, 0, SWT.TOP);		
		formData_13.right = new FormAttachment(authorLabel, 450, SWT.RIGHT);
		textAuthor.setLayoutData(formData_13);
		
		final Label dateLabel = new Label(grpProjectInfo, SWT.NONE);
		dateLabel.setFont(SWTResourceManager.getFont(strFont, 10, SWT.NONE));
		final FormData formData_14 = new FormData();
		formData_14.top = new FormAttachment(authorLabel, 10);
		formData_14.left = new FormAttachment(0, 5);
		dateLabel.setLayoutData(formData_14);
		dateLabel.setText("Date :");
		
		textDate = new Text(grpProjectInfo, SWT.BORDER);
		final FormData formData_15 = new FormData();
		formData_15.right = new FormAttachment(textAuthor, 0, SWT.RIGHT);
		formData_15.top = new FormAttachment(textAuthor, 7, SWT.BOTTOM);
		formData_15.left = new FormAttachment(textAuthor, 0, SWT.LEFT);
		textDate.setLayoutData(formData_15);
		Calendar cal = new GregorianCalendar();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        textDate.setText(day + " / " + (month+1) + " / " + year);
		
		final Label obsLabel = new Label(grpProjectInfo, SWT.NONE);
		obsLabel.setFont(SWTResourceManager.getFont(strFont, 10, SWT.NONE));
		final FormData formData_16 = new FormData();
		formData_16.top = new FormAttachment(dateLabel, 10);
		formData_16.left = new FormAttachment(0, 5);
		obsLabel.setLayoutData(formData_16);
		obsLabel.setText("Observation :");
		
		textObservation = new Text(grpProjectInfo, SWT.BORDER);
		final FormData formData_17 = new FormData();
		formData_17.right = new FormAttachment(textDate, 0, SWT.RIGHT);
		formData_17.top = new FormAttachment(textDate, 10, SWT.BOTTOM);
		formData_17.left = new FormAttachment(textDate, 0, SWT.LEFT);
		textObservation.setLayoutData(formData_17);
		
		final Group grpProjectLocation = new Group(epfaProjet, SWT.NONE);
		formData_1.right = new FormAttachment(grpProjectLocation, 0, SWT.RIGHT);
		grpProjectLocation.setText("Project Location");
		grpProjectLocation.setFont(SWTResourceManager.getFont(strFont, 10, SWT.NONE));
		final FormData fd_grpProjectLocation = new FormData();
		fd_grpProjectLocation.bottom = new FormAttachment(100, -25);
		fd_grpProjectLocation.top = new FormAttachment(grpProjectInfo, 13);
		fd_grpProjectLocation.right = new FormAttachment(100, -20);
		fd_grpProjectLocation.left = new FormAttachment(0);
		grpProjectLocation.setLayoutData(fd_grpProjectLocation);
		grpProjectLocation.setLayout(new FormLayout());
		
		final Label lblEpfasWorkspace = new Label(grpProjectLocation, SWT.NONE);
		lblEpfasWorkspace.setFont(SWTResourceManager.getFont(strFont, 10, SWT.NONE));
		lblEpfasWorkspace.setBounds(10, 36, 133, 20);
		lblEpfasWorkspace.setText("EPFA's workspace :");
		FormData fd_lblEpfasWorkspace = new FormData();
		fd_lblEpfasWorkspace.top = new FormAttachment(0, 10);
		fd_lblEpfasWorkspace.left = new FormAttachment(0, 10);
		lblEpfasWorkspace.setLayoutData(fd_lblEpfasWorkspace);
		
		textPathworkspace = new Text(grpProjectLocation, SWT.BORDER);
		textPathworkspace.setEnabled(false);
		textPathworkspace.setText(Platform.getLocation().toString());
		FormData fd_textPathworkspace = new FormData();
		fd_textPathworkspace.top = new FormAttachment(lblEpfasWorkspace, 0, SWT.TOP);
		fd_textPathworkspace.left = new FormAttachment(lblEpfasWorkspace, 8);
		fd_textPathworkspace.right = new FormAttachment(100, -7);
		textPathworkspace.setLayoutData(fd_textPathworkspace);
		
		setControl(epfaProjet);


	}



	

}
